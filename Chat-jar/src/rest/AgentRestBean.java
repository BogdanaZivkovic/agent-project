package rest;

import java.lang.management.ManagementFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.Agent;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.AgentCenter;
import models.AgentType;
import models.User;
import util.JNDILookup;

@Stateless
@Path("/agents")
public class AgentRestBean implements AgentRest{	

	@EJB
	AgentManagerRemote agentManager;

	@EJB
	MessageManagerRemote messageManager;
	
	@EJB
	ChatManagerRemote chatManager;

	@Override
	public void getAvailableAgentTypes(String username) {
		User user = chatManager.findByUsername(username);
		
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_AGENT_TYPES");
		
		messageManager.post(message);
		
	}
	
	@Override
	public void getRunningAgents(String username) {
		User user = chatManager.findByUsername(username);
		
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_RUNNING_AGENTS");
		
		messageManager.post(message);
	}

	@Override
	public Response startAgent(String type, String name) {
		
		String address = getNodeAddress();
		String alias = System.getProperty("jboss.node.name") + ":8080";
		
		String agentName = "ejb:Chat-ear/Chat-jar//" + type + "!" + Agent.class.getName() + "?stateful";
		AID aid = new AID(name, new AgentCenter(address, alias) ,new AgentType(type));	
		agentManager.startAgent(agentName, aid);
		
		ACLMessage message = new ACLMessage();			
		message.userArgs.put("command", "GET_RUNNING_AGENTS");
		
		for (User loggedInUser : chatManager.loggedInUsers()) {
			if(loggedInUser.getHost().getAlias().equals(System.getProperty("jboss.node.name") + ":8080")) {	
				AID receiverAID = new AID(loggedInUser.getUsername(), loggedInUser.getHost(), new AgentType("UserAgent"));
				message.receivers.add(receiverAID);
			}
		}	
		
		messageManager.post(message);
		
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public Response stopAgent(AID aid) {
		agentManager.stopAgent(aid);
		
		ACLMessage message = new ACLMessage();
		message.userArgs.put("command", "GET_RUNNING_AGENTS");
		
		for (User loggedInUser : chatManager.loggedInUsers()) {
			AID receiverAID = new AID(loggedInUser.getUsername(), loggedInUser.getHost(), new AgentType("UserAgent"));
			message.receivers.add(receiverAID);
		}
		
		messageManager.post(message);
		
		return Response.status(Response.Status.OK).build();
	}

	@Override
	public void sendACLMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPerformatives(String username) {
		User user = chatManager.findByUsername(username);
		
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_PERFORMATIVES");
		
		messageManager.post(message);
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
