package agentmanager;

import javax.ejb.Remote;

import agents.AID;
import agents.Agent;

@Remote
public interface AgentManagerRemote {
	public AID startAgent(String name, AID aid);
	public void stopAgent(AID aid);
	public Agent getAgentById(AID aid);
}
