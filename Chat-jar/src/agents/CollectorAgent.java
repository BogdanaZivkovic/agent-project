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
		
		String website = (String) message.userArgs.get("command");
		List<ClothingItem> clothingItems = new ArrayList<>();
		try {
			clothingItems = webScraping(website);
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
	
	private List<ClothingItem> webScraping(String website) throws IOException {
		List<ClothingItem> clothingItems = new ArrayList<>();
			
		if(website.equals("Website 1")) {
			Document doc = Jsoup.connect("https://www.nike.com/gb/w/womens-clothing-5e1x6z6ymx6").timeout(6000).get();	
			Elements div = doc.select("div.product-grid__items");
			
			for(Element e : div.select("div.product-card__info")) {		
							
					String productPrice = "";
					String productDescription = "";
					String productColorsNumber = "";
					String productName = "";
					
					productPrice = e.select("div.product-price").text();			
					productDescription = e.select("div.product-card__subtitle").text();
					productColorsNumber = e.select("div.product-card__product-count").text();
					productName = e.select("div.product-card__title").text();
					
					System.out.println("NIKE: " + productName + " " + productPrice + " " + productDescription + " " + productName);
	
					ClothingItem clothingItem = new ClothingItem(productName, productPrice, productDescription, productColorsNumber);
					clothingItems.add(clothingItem);
			}
		}
		else {
			Document doc = Jsoup.connect("https://www.converse.com/shop/womens?prefn1=pillar&prefv1=Clothing").timeout(6000).get();
			Elements div = doc.select("div.plp-grid");
			
			for(Element e : div.select("div.product-tile")) {		
							
					String productPrice = "";
					String productDescription = "";
					String productColorsNumber = "";
					String productName = "";
					
					productPrice = e.select("span.product-price--sales").text();			
					productDescription = e.select("p.product-tile__secondary-badge").text();
					productColorsNumber = e.select("p.product-tile__swatch-text").text();
					if(productColorsNumber.equals(null) || productColorsNumber.equals("")) {
						productColorsNumber = "1 color available";
					}
					productName = e.select("a.product-tile__url").text();
					
					System.out.println("CONVERSE:" + productName + " " + productPrice + " " + productDescription + " " + productName);
	
					ClothingItem clothingItem = new ClothingItem(productName, productPrice, productDescription, productColorsNumber);
					clothingItems.add(clothingItem);
			}
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
