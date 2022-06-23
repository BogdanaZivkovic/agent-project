package rest;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.ws.rs.Path;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.Agent;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import messagemanager.Performative;
import models.AgentCenter;
import models.AgentType;

@Stateless
@Path("/webScrape")
@Remote(WebScraperRest.class)
public class WebScraperBean implements WebScraperRest {
	
	@EJB 
	private AgentManagerRemote agentManager;
	
	@EJB 
	private MessageManagerRemote messageManager;
	
	@EJB
	private ChatManagerRemote chatManager;

	@Override
	public void getClothingItems(String username) {
		
		System.out.println("getClothingItems");
		
		String address = getNodeAddress();
		String alias = System.getProperty("jboss.node.name") + ":8080";
		
		AgentCenter agentCenter = new AgentCenter(address, alias);
		
		AID master = startMasterAgent(agentCenter, username);
		AID searcher = startSearchAgent(agentCenter, username);
		AID collector = startCollectorAgent(agentCenter, username);
		
		ACLMessage message = new ACLMessage();
		message.sender = master;
		List<AID> collectors = new ArrayList<>();
		collectors.add(collector);
		message.receivers = collectors;
		message.replyTo = searcher;
		message.performative = Performative.COLLECT;
		message.userArgs.put("command", "Website 1");
		messageManager.post(message);
		
		ACLMessage message2 = new ACLMessage();
		message2.sender = master;
		List<AID> collectors2 = new ArrayList<>();
		collectors2.add(collector);
		message2.receivers = collectors2;
		message2.replyTo = searcher;
		message2.performative = Performative.COLLECT;
		message2.userArgs.put("command", "Website 2");
		messageManager.post(message2);
		
	}
	
	private AID startMasterAgent(AgentCenter agentCenter, String username) {
		System.out.println("MasterAgent");
		AgentType type = new AgentType("MasterAgent", agentCenter.getAlias());
		String agentName = "ejb:Chat-ear/Chat-jar//" + "MasterAgent" + "!" + Agent.class.getName() + "?stateful";
		AID aid = new AID(username, agentCenter, type);
		agentManager.startAgent(agentName, aid);
		return aid;
	}
	
	private AID startSearchAgent(AgentCenter agentCenter, String username) {
		System.out.println("SearchAgent");
		AgentType type = new AgentType("SearchAgent", agentCenter.getAlias());
		String agentName = "ejb:Chat-ear/Chat-jar//" + "SearchAgent" + "!" + Agent.class.getName() + "?stateful";
		AID aid = new AID(username, agentCenter, type);
		agentManager.startAgent(agentName, aid);
		return aid;
	}
	
	private AID startCollectorAgent(AgentCenter agentCenter, String username) {
		System.out.println("CollectorAgent");
		AgentType type = new AgentType("CollectorAgent", agentCenter.getAlias());
		String agentName = "ejb:Chat-ear/Chat-jar//" + "CollectorAgent" + "!" + Agent.class.getName() + "?stateful";
		AID aid = new AID(username, agentCenter, type);
		agentManager.startAgent(agentName, aid);
		return aid;
	}
	
	private String getNodeAddress() {
		try {
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
			return (String) mBeanServer.getAttribute(http, "boundAddress");
		} catch (MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException | ReflectionException | MBeanException e) {
			e.printStackTrace();
			return null;
		}	
	}
}

	