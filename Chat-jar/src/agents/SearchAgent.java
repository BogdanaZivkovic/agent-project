package agents;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.ClothingItem;

@Stateful
@Remote(Agent.class)
public class SearchAgent implements Agent  {
	
	private static final long serialVersionUID = 1L;
	private AID id;
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	
	@EJB
	private MessageManagerRemote messageManager;

	@Override
	public AID init(AID aid) {
		this.id = aid;
		cachedAgents.addRunningAgent(aid, this);
		return id;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		List<ClothingItem> clothingItems = read();
		String content = "";
		
		for(ClothingItem clothingItem: clothingItems) {
			content +=  clothingItem.getProductName() + "," + clothingItem.getProductPrice() + "," + clothingItem.getProductDescription() + "," + clothingItem.getProductColorsNumber() + "|";
		}
		
		System.out.println(message.replyTo.getType().getName());
		reply(message.replyTo, content );
	}

	@Override
	public AID getAid() {
		return id;
	}
	
	private void reply(AID receiver, String content) {
		ACLMessage message = new ACLMessage();
		message.sender = id;
		List<AID> receivers = new ArrayList<>();
		receivers.add(receiver);
		message.receivers = receivers;
		message.content = content;
		messageManager.post(message);
	}
	
	private List<ClothingItem> read() {
		ObjectMapper objectMapper = new ObjectMapper();

		String path = "C:\\Users\\bogdana\\Desktop\\agent-project\\Clothing\\clothing.json";
		
		File file = new File(path);

		List<ClothingItem> clothingItems = new ArrayList<ClothingItem>();
		try {
			clothingItems = objectMapper.readValue(file, new TypeReference<List<ClothingItem>>() { });

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return clothingItems;
	}
}
