package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import agents.AID;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.AgentCenter;
import models.AgentType;
import models.Message;
import models.User;

@Stateless
@Path("/messages")
public class MessageRestBean implements MessageRest{

	@EJB
	private MessageManagerRemote messageManager;
	@EJB
	private ChatManagerRemote chatManager;
	
	@Override
	public void sendMessageToAll(Message message) {			
		for(User user : chatManager.loggedInUsers()) {
			message.setReceiver(user);
			sendMessageToUser(message);
		}	
	}

	@Override
	public void sendMessageToUser(Message message) {
		User receiver = message.getReceiver();
		String username = receiver.getUsername();
		User user = chatManager.findByUsername(username);
		AgentCenter agentCenter = user.getHost();
		String hostAlias = agentCenter.getAlias();
		
		if(hostAlias.equals(System.getProperty("jboss.node.name") + ":8080")) {
			ACLMessage aclMessage = new ACLMessage();
			aclMessage.receivers.add(new AID(user.getUsername(), user.getHost(), new AgentType("UserAgent", hostAlias)));
			aclMessage.sender = new AID(message.getSender().getUsername(), message.getSender().getHost(), new AgentType("UserAgent", hostAlias));
			aclMessage.contentObj = message;
			aclMessage.content = message.getContent();
			aclMessage.userArgs.put("command", "MESSAGE");
			messageManager.post(aclMessage);				
		}
		else {
			System.out.println("Host " + hostAlias + " receives a message to distribute");
			ResteasyClient resteasyClient = new ResteasyClientBuilder().build();
			ResteasyWebTarget rtarget = resteasyClient.target("http://" + hostAlias + "/Chat-war/api/messages");
			MessageRest rest = rtarget.proxy(MessageRest.class);
			rest.sendMessageToUser(message);
			resteasyClient.close();
		}
	}


	@Override
	public void getUserMessages(String username) {
		
		User user = chatManager.findByUsername(username);
		
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent", user.getHost().getAlias())));
		message.userArgs.put("command", "GET_MESSAGES");
		
		messageManager.post(message);
	
	}
}
