package agents;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import chatmanager.ChatManagerRemote;
import messagesrepo.MessagesRepoRemote;
import models.Host;
import models.User;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class UserAgent implements Agent {

	private static final long serialVersionUID = 1L;
	private String agentId;
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private MessagesRepoRemote messagesRepo;
	@EJB
	private WSChat ws;

	
	@PostConstruct
	public void postConstruct() {
		System.out.println("Created User Agent!");
	}
	
	@Override
	public void handleMessage(Message message) {
		TextMessage tmsg = (TextMessage) message;

		String receiver;
		try {
			receiver = (String) tmsg.getObjectProperty("receiver");
			if (agentId.equals(receiver)) {
				String option = "";
				String response = "";
				try {
					option = (String) tmsg.getObjectProperty("command");
					switch (option) {
					case "GET_LOGGEDIN":
						response = "LOGGEDIN!";
						List<User> users = chatManager.loggedInUsers();
						for (User u : users) {
							response += u.toString() + "|";
						}

						break;
					case "GET_REGISTERED":
						response = "REGISTERED!";
						users = chatManager.registeredUsers();
						for (User u : users) {
							response += u.toString() + "|";
						}

						break;
					case "MESSAGE":
						response = "MESSAGE!";
						String sender = (String) tmsg.getObjectProperty("sender");
						String subject = (String) tmsg.getObjectProperty("subject");
						String content = (String) tmsg.getObjectProperty("content");
						models.Message msg = new models.Message(new User(receiver, "", new Host()), new User(sender, "", new Host()), LocalDateTime.now(), subject, content);
						messagesRepo.addMessage(msg);
						response += msg.toString();
						break;
					case "GET_MESSAGES":
						response = "MESSAGES!";
						for(models.Message m : messagesRepo.getMessages()) {
							response += m.toString() + "|";
						}
						break;
					default:
						response = "ERROR!Option: " + option + " does not exist.";
						break;
					}
					System.out.println(response);
					ws.onMessage(agentId, response);
					
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String init(String agentId) {
		this.agentId = agentId;
		cachedAgents.addRunningAgent(agentId, this);
		return agentId;
	}

	@Override
	public String getAgentId() {
		return agentId;
	}
}
