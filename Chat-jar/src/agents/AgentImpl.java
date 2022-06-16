package agents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagesrepo.MessagesRepoRemote;
import models.AgentCenter;
import models.User;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class AgentImpl implements Agent {

	private static final long serialVersionUID = 1L;
	private AID id;
	
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
	public void handleMessage(ACLMessage message) {

		String receiver = (String) message.receivers.get(0).getName();
		if (id.getName().equals(receiver)) {
			String option = "";
			String response = "";
			option = (String) message.userArgs.get("command");
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
				String sender = (String) message.sender.getName();
				String content = (String) message.content;
				models.Message msg = new models.Message(new User(receiver, "", new AgentCenter()), new User(sender, "", new AgentCenter()), LocalDateTime.now(), "NASLOV", content);
				messagesRepo.addMessage(msg);
				response += msg.toString();
				break;
			case "GET_MESSAGES":
				response = "MESSAGES!";
				for(models.Message m : messagesRepo.getMessages()) {
					response += m.toString() + "|";
				}
				break;
			case "GET_RUNNING_AGENTS":
				response = "RUNNING_AGENTS!";
				
				Collection<Agent> agents = cachedAgents.getRunningAgents().values();
				
				for (Agent agent : agents) {
					response += agent.toString() + "|";
				}
				break;
			case "GET_AGENT_TYPES":
				response = "AGENT_TYPES!";
				
				agents = cachedAgents.getRunningAgents().values();
				
				List<String> agentTypes = new ArrayList<>();
				
				for (Agent agent : agents) {
					String agentTypeName = agent.getAid().getType().getName();
					if(!agentTypes.contains(agentTypeName)) {
						agentTypes.add(agentTypeName);
						response += agentTypeName + "|";
					}
				}
				break;
			default:
				response = "ERROR!Option: " + option + " does not exist.";
				break;
			}
			System.out.println(response);
			ws.onMessage(id.getName(), response);
		}
	}

	@Override
	public AID init(AID aid) {
		this.id = aid;
		cachedAgents.addRunningAgent(aid, this);
		return id;
	}

	@Override
	public AID getAid() {
		return id;
	}

	@Override
	public String toString() {
		return id.getName() + "," + id.getType().getName() + "," + id.getHost().getAddress() + "," + id.getHost().getAlias();
	}
}
