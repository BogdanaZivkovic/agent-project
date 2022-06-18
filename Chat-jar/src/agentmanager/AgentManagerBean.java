package agentmanager;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.naming.NamingException;

import agents.AID;
import agents.Agent;
import agents.CachedAgentsRemote;
import models.AgentType;
import util.JNDILookup;
import util.JndiTreeParser;

@Stateless
@LocalBean
public class AgentManagerBean implements AgentManagerRemote {
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB
	private JndiTreeParser jndiTreeParser;
	
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

	@Override
	public Collection<Agent> getRunningAgents() {
		return cachedAgents.getRunningAgents().values();
	}
	
	@Override
	public List<AgentType> getAvailableAgentTypes() {
		try {
			return jndiTreeParser.parse();
		} catch (NamingException ex) {
			throw new IllegalStateException(ex);
		}
	}
}
