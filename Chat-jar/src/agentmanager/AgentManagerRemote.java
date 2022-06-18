package agentmanager;

import java.util.Collection;
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
	
	public List<AgentType> getAvailableAgentTypes();
}
