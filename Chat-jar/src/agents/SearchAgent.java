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

import dto.SearchDTO;
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
		SearchDTO searchDTO = (SearchDTO) message.userArgs.get("filter");
		
		List<ClothingItem> list = filter(clothingItems, searchDTO); 
		
		String content = "";
		
		for(ClothingItem clothingItem: list) {
			content +=  clothingItem.getProductName() + "," + clothingItem.getProductPrice() + "," + clothingItem.getProductDescription() + "," + clothingItem.getProductColorsNumber() + "|";
		}
		
		reply(message.replyTo, content);
		System.out.println(message.replyTo.getType().getName());
	}

	private List<ClothingItem> filter(List<ClothingItem> clothingItems, SearchDTO searchDTO) {
		
		List<ClothingItem> clothingItemslist = new ArrayList<ClothingItem>();
		String name = searchDTO.getProductName();
		String description = searchDTO.getProductDescription();
		Double minPrice = searchDTO.getMinPrice();
		Double maxPrice = searchDTO.getMaxPrice();
		Integer minColorNumber = searchDTO.getMinColorNumber();
		Integer maxColorNumber = searchDTO.getMaxColorNumber();
		
		System.out.println(name);
		System.out.println(minPrice);
		
		
		for(ClothingItem clothingItem: clothingItems) {

			if((minPrice == null || clothingItem.getProductPrice() >= minPrice ) && (maxPrice == null || clothingItem.getProductPrice() <= maxPrice) 
					&& (minColorNumber == null || clothingItem.getProductColorsNumber() >= minColorNumber) && (maxColorNumber == null || clothingItem.getProductColorsNumber() <= maxColorNumber)
					&& (name == null || clothingItem.getProductName().contains(name)) && (description == null || clothingItem.getProductDescription().contains(description))) {
				
				clothingItemslist.add(clothingItem);		
			}		
		}
		return clothingItemslist;
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
