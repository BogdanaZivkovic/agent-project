package rest;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
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

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.Agent;
import chatmanager.ChatManagerRemote;
import connnectionmanager.ConnectionManager;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import messagemanager.Performative;
import models.AgentCenter;
import models.AgentType;

@Stateless
@Path("/webScrape")
@Remote(WebScraperRest.class)
public class WebScraperBean implements WebScraperRest {

	String websites[] = new String[] {"Website 1", "Website 2"};
	
	@EJB 
	private AgentManagerRemote agentManager;
	
	@EJB 
	private MessageManagerRemote messageManager;
	
	@EJB
	private ChatManagerRemote chatManager;
	
	@EJB
	private ConnectionManager connectionManager;

	@Override
	public void getClothingItems(String username) {
		System.out.println("getClothingItems");
		
		List<String> nodes = connectionManager.getNodes();
		String alias = System.getProperty("jboss.node.name") + ":8080";	
		nodes.add(alias);
		
		String address = getNodeAddress();
		AgentCenter agentCenter = new AgentCenter(address, alias);
		
		AID aid = new AID(username, agentCenter, new AgentType("UserAgent", alias));
		AID master = startMasterAgent(agentCenter, username);
		
		for(int i=0; i<websites.length; i++) {
			
			int index = i % nodes.size();	
			String nodeAlias = nodes.get(index);
				
			if(nodeAlias.equals(alias)) {
				AID searcher = startSearchAgent(agentCenter);
				AID collector = startCollectorAgent(agentCenter);
				
				ACLMessage message = new ACLMessage();
				message.sender = master;
				List<AID> collectors = new ArrayList<>();
				collectors.add(collector);
				message.receivers = collectors;
				message.replyTo = searcher;
				message.performative = Performative.COLLECT;
				message.userArgs.put("command", websites[i]);
				messageManager.post(message);
			}
			else {
				ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = resteasyClient.target("http://"  + nodeAlias + "/Chat-war/api/webScrape");
				WebScraperRest rest = rtarget.proxy(WebScraperRest.class);
				rest.supplyClothingItems(aid, websites[i]);
			}
		}		
	}
	
	@Override
	public void supplyClothingItems (AID aid, String website) {
		
		String alias = System.getProperty("jboss.node.name") + ":8080";	
		String address = getNodeAddress();
		
		AgentCenter agentCenter = new AgentCenter(address, alias);

		AID searcher = startSearchAgent(agentCenter);
		AID collector = startCollectorAgent(agentCenter);
		
		ACLMessage message = new ACLMessage();
		message.sender = aid;
		List<AID> collectors = new ArrayList<>();
		collectors.add(collector);
		message.receivers = collectors;
		message.replyTo = searcher;
		message.performative = Performative.COLLECT;
		message.userArgs.put("command", website);
		messageManager.post(message);
		
	}
	
	private AID startMasterAgent(AgentCenter agentCenter, String username) {
		System.out.println("MasterAgent");
		AgentType type = new AgentType("MasterAgent", agentCenter.getAlias());
		String agentName = "ejb:Chat-ear/Chat-jar//" + "MasterAgent" + "!" + Agent.class.getName() + "?stateful";
		AID aid = new AID(username, agentCenter, type);
		
		Collection<Agent> runningAgents = agentManager.getRunningAgentsHashMap().values();
		
		for(Agent agent : runningAgents) {
			if(agent.getAid().getName().equals(username) && agent.getAid().getType().getName().equals("MasterAgent")) {
				return aid;
			}
		}
		
		agentManager.startAgent(agentName, aid);
		return aid;
	}
	
	private AID startSearchAgent(AgentCenter agentCenter) {
		System.out.println("SearchAgent");
		AgentType type = new AgentType("SearchAgent", agentCenter.getAlias());
		String agentName = "ejb:Chat-ear/Chat-jar//" + "SearchAgent" + "!" + Agent.class.getName() + "?stateful";
		AID aid = new AID("SearchAgent", agentCenter, type);
		
		Collection<Agent> runningAgents = agentManager.getRunningAgentsHashMap().values();
		
		for(Agent agent : runningAgents) {
			if(agent.getAid().getName().equals("SearchAgent")) {
				return aid;
			}
		}
		
		agentManager.startAgent(agentName, aid);
		return aid;
	}
	
	private AID startCollectorAgent(AgentCenter agentCenter) {
		System.out.println("CollectorAgent");
		AgentType type = new AgentType("CollectorAgent", agentCenter.getAlias());
		String agentName = "ejb:Chat-ear/Chat-jar//" + "CollectorAgent" + "!" + Agent.class.getName() + "?stateful";
		AID aid = new AID("CollectorAgent", agentCenter, type);
		
		Collection<Agent> runningAgents = agentManager.getRunningAgentsHashMap().values();
		
		for(Agent agent : runningAgents) {
			
			if(agent.getAid().getName().equals("CollectorAgent")) {
				return aid;
			}
		}
		
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

	