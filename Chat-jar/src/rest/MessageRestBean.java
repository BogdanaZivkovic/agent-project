package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import chatmanager.ChatManagerRemote;
import messagemanager.AgentMessage;
import messagemanager.MessageManagerRemote;
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
		String hostAlias = chatManager.findByUsername(message.getReceiver().getUsername()).getHost().getAlias();
		if(hostAlias.equals(System.getProperty("jboss.node.name") + ":8080")) {
			AgentMessage agentMessage = new AgentMessage();
			agentMessage.userArgs.put("receiver", message.getReceiver().getUsername());
			agentMessage.userArgs.put("sender", message.getSender().getUsername());
			agentMessage.userArgs.put("subject", message.getSubject());
			agentMessage.userArgs.put("content", message.getContent());	
			agentMessage.userArgs.put("command", "MESSAGE");
			messageManager.post(agentMessage);				
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
		AgentMessage agentMessage = new AgentMessage();
		agentMessage.userArgs.put("receiver", username);
		agentMessage.userArgs.put("command", "GET_MESSAGES");
		messageManager.post(agentMessage);		
	}
}
