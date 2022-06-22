package agentmanager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Remote;

import agents.AID;
import agents.Agent;
import models.AgentType;

@Remote
public interface AgentManagerRemote {
	
	public AID startAgent(String name, AID aid);
	
	public void stopAgent(AID aid);
	
	public Agent getAgentById(AID aid);
	
	public Collection<Agent> getRunningAgents();
	
	public HashMap<AID, Agent> getRunningAgentsHashMap();
	
	public List<AID> getRemoteRunningAgents();
	
	public List<AgentType> getAvailableAgentTypes();

	public void setRemoteRunningAgents(List<AID> agents);

	void addAgentType(AgentType agentType);

	public void setAgentTypes(List<AgentType> agentTypes);

}
