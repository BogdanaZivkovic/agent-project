package agents;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.ClothingItem;

@Stateful
@Remote(Agent.class)
public class CollectorAgent implements Agent {
	
	private static final long serialVersionUID = 1L;
	private AID id;

	@EJB
	private CachedAgentsRemote cachedAgents;
	
	@EJB
	private MessageManagerRemote messageManager;

	@Override
	public void handleMessage(ACLMessage message) {
		
		List<ClothingItem> clothingItems = new ArrayList<>();
		try {
			clothingItems = webScraping();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		reply(message.replyTo, message.sender, clothingItems);
	}

	@Override
	public AID getAid() {
		return id;
	}
	

	@Override
	public AID init(AID aid) {
		this.id = aid;
		cachedAgents.addRunningAgent(aid, this);
		return id;
	}
	
	private List<ClothingItem> webScraping() throws IOException {
		Document doc = Jsoup.connect("https://www.nike.com/gb/w/womens-clothing-5e1x6z6ymx6").timeout(6000).get();
		Elements div = doc.select("div.product-grid__items");
		
		List<ClothingItem> clothingItems = new ArrayList<>();
		
		for(Element e : div.select("div.product-card__info")) {		
						
				String productPrice = "";
				String productDescription = "";
				String productColorsNumber = "";
				String productName = "";
				
				productPrice = e.select("div.product-price").text();			
				productDescription = e.select("div.product-card__subtitle").text();
				productColorsNumber = e.select("div.product-card__product-count").text();
				productName = e.select("div.product-card__title").text();
				
				System.out.println(productName + " " + productPrice + " " + productDescription + " " + productColorsNumber);
				
				ClothingItem clothingItem = new ClothingItem(productName, productPrice, productDescription, productColorsNumber);
				clothingItems.add(clothingItem);
		}
		
		return clothingItems;
	}
	
	private void reply(AID receiver, AID replyTo, List<ClothingItem> clothingItems) {
		ACLMessage message = new ACLMessage();
		message.sender = id;
		List<AID> receivers = new ArrayList<>();
		receivers.add(receiver);
		message.receivers = receivers;
		message.replyTo = replyTo;
		save(clothingItems);
		messageManager.post(message);
	}
	

	private void save(List<ClothingItem> clothingItems) {

		String path = "C:\\Users\\bogdana\\Desktop\\agent-project\\Clothing\\clothing.json";

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new FileOutputStream(path), clothingItems);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
