package connnectionmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
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
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.AgentCenter;
import models.AgentType;
import models.User;
import util.FileUtils;
import ws.WSChat;

@Singleton
@Startup
@Remote(ConnectionManager.class)
@LocalBean
@Path("/connection")
public class ConnectionManagerBean implements ConnectionManager{

	private AgentCenter localNode = new AgentCenter();
	private List<String> connectedNodes = new ArrayList<String>();
	
	@EJB 
	private ChatManagerRemote chatManager;
	
	@EJB 
	private MessageManagerRemote messageManager;
	
	@EJB 
	private AgentManagerRemote agentManager;
	
	@EJB WSChat ws;
	
	@PostConstruct
	private void init() {
		try {	
			String address = getNodeAddress();
			String alias = System.getProperty("jboss.node.name") + ":8080";
			String master = getMaster() + ":8080"; 
			
			localNode.setAddress(address);
			localNode.setAlias(alias);
			
			System.out.println(("MASTER ADDR: " + master + ", node name: " + alias + ", node address: " + address));
			if (master != null && !master.equals(":8080")) {
				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = client.target("http://" + master + "/Chat-war/api/connection");
				ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
				connectedNodes = rest.registerNode(alias);
				connectedNodes.remove(alias);
				connectedNodes.add(master);
				System.out.println("Handshake successful. Connected nodes: " + connectedNodes);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String> registerNode(String nodeAlias) {
		System.out.println("New node registered: " + nodeAlias);
		for (String c : connectedNodes) {
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = client.target("http://" + c + "/Chat-war/api/connection");
			ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
			rest.addNode(nodeAlias);
		}
		
		connectedNodes.add(nodeAlias);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = resteasyClient.target("http://"  + nodeAlias + "/Chat-war/api/connection");
				ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
				rest.loggedInForNodes(chatManager.loggedInUsers());
				rest.registeredForNodes(chatManager.registeredUsers());
				rest.runningAgentsForNodes(agentManager.getRemoteRunningAgents());
				List<AgentType> agentTypes = rest.getAgentTypes();
				agentManager.addAgentTypes(agentTypes);
				rest.agentTypesForNodes(agentManager.getAvailableAgentTypes());
				resteasyClient.close();
			}
		}).start();
		
		return connectedNodes;
	}
	
	@Override
	public void addNode(String nodeAlias) {
		System.out.println("Added node: " + nodeAlias);
		connectedNodes.add(nodeAlias);	
	}
	
	@Override
	public void deleteNode(String nodeAlias) {
		System.out.println("Deleted node: " + nodeAlias);		
		connectedNodes.remove(nodeAlias);	
	}
	
	@Override
	public void removeLoggedInUser(String nodeAlias) {
		List<User> users = chatManager.loggedInUsers();
		for(User user: users) {
			if(user.getHost().getAlias().equals(nodeAlias)) {
				chatManager.logout(user.getUsername());
			}
		}
	}
	
	@Override
	public void removeAgent(String nodeAlias) {
		List<AID> aids = agentManager.getRemoteRunningAgents();
		for(AID aid : aids) {
			if(aid.getHost().getAlias().equals(nodeAlias)) {
				agentManager.stopAgent(aid);
			}
		}
	}
	
	@Override
	public String pingNode() {
		System.out.println("Ping");
		return "Ok";
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
	
	private String getMaster() {
		try {
			File f = FileUtils.getFile(ConnectionManager.class, "", "connections.properties");
			FileInputStream fileInput = new FileInputStream(f);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();
			return properties.getProperty("master");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<String> getNodes() {
		return connectedNodes;
	}
	
	@PreDestroy
	private void shutDown() {
		notifyAllToShutDownNode(localNode.getAlias());
	}	
	
	private void notifyAllToShutDownNode(String alias) {
		for (String cn: connectedNodes) {
			ResteasyClient client = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = client.target("http://" + cn + "/Chat-war/api/connection");
			ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
			rest.deleteNode(alias);
			rest.removeLoggedInUser(alias);
			rest.removeAgent(alias);
			client.close();	
		}
	}
	
	@Schedule(hour = "*", minute="*", second="*/60", persistent=false)
	private void heartbeat() {
		System.out.println("Heartbeat protocol");
		for(String cn : connectedNodes) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					boolean nodeAlive = isNodeAlive(cn);
					if(!nodeAlive) {
						System.out.println("Deleting unresponsive node" + cn);
						connectedNodes.remove(cn);
						notifyAllToShutDownNode(cn);
					}
				}
			}).start();
		}
	}
	
	private boolean isNodeAlive(String nodeAlias) {
		
		int pingTriesCount = 2;
		boolean nodeAlive = false;
		
		for(int i=0; i <pingTriesCount; i++) {
			try {
				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget rtarget = client.target("http://" + nodeAlias + "/Chat-war/api/connection");
				ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
				String response = rest.pingNode();
				client.close();
				if(response.equals("Ok")) {
					nodeAlive = true;
					break;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}	
		}
		return nodeAlive;
	}
	
	public String getNodeName() {
		return localNode.getAlias();
	}


	@Override
	public void loginNotifyNodes() {
		for (String cn: connectedNodes) {
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = resteasyClient.target("http://" + cn + "/Chat-war/api/connection");
			ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
			rest.loggedInForNodes(chatManager.loggedInUsers());
			resteasyClient.close();
		}	
	}


	@Override
	public void registerNotifyNodes() {
		for (String cn: connectedNodes) {
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = resteasyClient.target("http://" + cn + "/Chat-war/api/connection");
			ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
			rest.registeredForNodes(chatManager.registeredUsers());
			resteasyClient.close();
		}		
	}


	@Override
	public void loggedInForNodes(List<User> users) {
		chatManager.setLoggedIn(users);
		ACLMessage message = new ACLMessage();
		message.userArgs.put("command", "GET_LOGGEDIN");
		for(User user : chatManager.loggedInUsers()) {
			if(!user.getHost().getAlias().equals(localNode.getAlias())) {
				continue;
			}
			message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent", user.getHost().getAlias())));				
		}
		messageManager.post(message);
	}


	@Override
	public void registeredForNodes(List<User> users) {
		chatManager.setRegistered(users);
		for(User user : chatManager.loggedInUsers()) {
			if(!user.getHost().getAlias().equals(localNode.getAlias())) {
				continue;
			}
						
			ACLMessage message = new ACLMessage();
			message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent", user.getHost().getAlias())));
			message.userArgs.put("command", "GET_REGISTERED");

			messageManager.post(message);
		}
	}

	@Override
	public void agentRunningNofityNodes() {
		for (String cn: connectedNodes) {
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = resteasyClient.target("http://" + cn + "/Chat-war/api/connection");
			ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
			
			List<AID> agents = agentManager.getRemoteRunningAgents();

			rest.runningAgentsForNodes(agents);
			
			resteasyClient.close();
		}		
	}

	@Override
	public void runningAgentsForNodes(List<AID> agents) {
		agentManager.setRemoteRunningAgents(agents);
		
		ACLMessage message = new ACLMessage();
		message.userArgs.put("command", "GET_RUNNING_AGENTS");
		
		for(User user : chatManager.loggedInUsers()) {
			if(!user.getHost().getAlias().equals(localNode.getAlias())) {
				continue;
			}
						
			message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent", user.getHost().getAlias())));
		}
		
		messageManager.post(message);
	}

	@Override
	public void agentTypesNofityNodes() {
		for (String cn: connectedNodes) {
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = resteasyClient.target("http://" + cn + "/Chat-war/api/connection");
			ConnectionManager rest = rtarget.proxy(ConnectionManager.class);
			
			List<AgentType> agentTypes = agentManager.getAvailableAgentTypes();

			rest.agentTypesForNodes(agentTypes);
			
			resteasyClient.close();
		}	
	}

	@Override
	public void agentTypesForNodes(List<AgentType> agentTypes) {
		agentManager.setAgentTypes(agentTypes);
		
		ACLMessage message = new ACLMessage();
		message.userArgs.put("command", "GET_AGENT_TYPES");
		
		for(User user : chatManager.loggedInUsers()) {
			if(!user.getHost().getAlias().equals(localNode.getAlias())) {
				continue;
			}
						
			message.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent", user.getHost().getAlias())));
			
		}
		
		messageManager.post(message);
	}

	@Override
	public List<AgentType> getAgentTypes() {
		return agentManager.getAvailableAgentTypes();
	}
}
