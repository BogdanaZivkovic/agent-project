package agentmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import agents.AID;
import agents.Agent;
import agents.CachedAgentsRemote;
import connnectionmanager.ConnectionManager;
import models.AgentType;
import util.JNDILookup;

@Stateless
@LocalBean
public class AgentManagerBean implements AgentManagerRemote {
	
	@EJB
	private CachedAgentsRemote cachedAgents;
	@EJB 
	private ConnectionManager connectionManager;
	
    public AgentManagerBean() {
        
    }

	@Override
	public AID startAgent(String name, AID aid) {
		Agent agent = (Agent) JNDILookup.lookUp(name, Agent.class);
		aid = agent.init(aid);
		connectionManager.agentRunningNofityNodes();
		return aid;
	}

	@Override
	public Agent getAgentById(AID aid) {
		return cachedAgents.getRunningAgents().get(aid);
	}

	@Override
	public void stopAgent(AID aid) {
		cachedAgents.removeAgent(aid);
		connectionManager.agentRunningNofityNodes();
	}

	@Override
	public Collection<Agent> getRunningAgents() {
		return cachedAgents.getRunningAgents().values();
	}
	
	@Override
	public HashMap<AID, Agent> getRunningAgentsHashMap() {
		return cachedAgents.getRunningAgents();
	}
	
	@Override
	public void addAgentType(AgentType agentType) {
		cachedAgents.addAgentType(agentType);
	}

	@Override
	public void setRemoteRunningAgents(List<AID> agents) {
		cachedAgents.setRemoteRunningAgents(agents);
	}

	@Override
	public List<AID> getRemoteRunningAgents() {
		return cachedAgents.getRemoteRunningAgentsAIDS();
	}

	@Override
	public List<AgentType> getAvailableAgentTypes() {
		return cachedAgents.getAgentTypes();
	}

	@Override
	public void setAgentTypes(List<AgentType> agentTypes) {
		cachedAgents.setAgentTypes(agentTypes);
	}
	
	@Override
	public void addAgentTypes(List<AgentType> agentTypes) {
		cachedAgents.addAgentTypes(agentTypes);
	}

	@Override
	public void removeAgentType(AgentType agentType) {
		cachedAgents.removeAgentType(agentType);
	}

	@Override
	public void removeAgentTypes(String nodeAlias) {
		cachedAgents.removeAgentTypes(nodeAlias);
		
	}
}
