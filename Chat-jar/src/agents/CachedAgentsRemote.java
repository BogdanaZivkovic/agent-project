package agents;

import java.util.HashMap;
import java.util.List;

import models.AgentType;

public interface CachedAgentsRemote {

	public HashMap<AID, Agent> getRunningAgents();
	
	public List<AID> getRemoteRunningAgentsAIDS();
	
	public void addRunningAgent(AID key, Agent agent);
	
	public Agent getByAID(AID aid);
	
	public void setRemoteRunningAgents(List<AID> agents);
	
	public void removeAgent(AID aid);

	void addAgentType(AgentType agentType);

	List<AgentType> getAgentTypes();

	public void setAgentTypes(List<AgentType> agentTypes);

	void addAgentTypes(List<AgentType> agentTypes);
}
