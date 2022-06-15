package agents;

import java.util.HashMap;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;

@Singleton
@LocalBean
@Remote(CachedAgentsRemote.class)
public class CachedAgents implements CachedAgentsRemote{

	HashMap<AID, Agent> runningAgents;

	public CachedAgents() {
		runningAgents = new HashMap<>();
	}

	@Override
	public HashMap<AID, Agent> getRunningAgents() {
		return runningAgents;
	}

	@Override
	public void addRunningAgent(AID key, Agent agent) {
		runningAgents.put(key, agent);
	}
	
	@Override
	public Agent getByAID(AID aid) {
		
		for(Agent agent : runningAgents.values()) {
			
			String address = agent.getAid().getHost().getAddress();
			System.out.print("AGENT" + address);
			System.out.print("AID" + aid.getHost().getAddress());
			
			String alias = agent.getAid().getHost().getAlias();
			System.out.print("AGENT" + alias);
			System.out.print("AID" + aid.getHost().getAlias());
			
			String name = agent.getAid().getName();
			System.out.print("AGENT" + name);
			System.out.print("AID" + aid.getName());
			
			String agentType = agent.getAid().getType().getName();
			System.out.print("AGENT" + agentType);
			System.out.print("AID" + aid.getType().getName());
			
			if(name.equals(aid.getName()) && address.equals(aid.getHost().getAddress()) && alias.equals(aid.getHost().getAlias()) && agentType.equals(aid.getType().getName())) {
				return agent;
			}
		}
		return null;
	}
}
