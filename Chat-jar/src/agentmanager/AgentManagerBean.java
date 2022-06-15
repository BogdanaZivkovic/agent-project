package agentmanager;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import agents.AID;
import agents.Agent;
import agents.CachedAgentsRemote;
import util.JNDILookup;

/**
 * Session Bean implementation class AgentManagerBean
 */
@Stateless
@LocalBean
public class AgentManagerBean implements AgentManagerRemote {
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	
    public AgentManagerBean() {
        
    }

	@Override
	public AID startAgent(String name, AID aid) {
		Agent agent = (Agent) JNDILookup.lookUp(name, Agent.class);
		return agent.init(aid);
	}

	@Override
	public Agent getAgentById(AID aid) {
		return cachedAgents.getRunningAgents().get(aid);
	}

	@Override
	public void stopAgent(AID aid) {
		cachedAgents.getRunningAgents().remove(aid);
	}
}
