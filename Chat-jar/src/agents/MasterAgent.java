package agents;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import messagemanager.ACLMessage;
import ws.WSChat;

@Stateful
@Remote(Agent.class)
public class MasterAgent implements Agent {
	
	private static final long serialVersionUID = 1L;
	private AID id;
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	
	@EJB
	private WSChat ws;

	@PostConstruct
	public void postConstruct() {
		System.out.println("Created Master Agent!");
	}

	@Override
	public AID init(AID aid) {
		this.id = aid;
		cachedAgents.addRunningAgent(aid, this);
		return id;
	}

	@Override
	public void handleMessage(ACLMessage message) {
		System.out.println(message.content);
		String response = "CLOTHING!";
		response += message.content;
		ws.onMessage(id.getName(), response);
	}

	@Override
	public AID getAid() {
		return id;
	}
}
