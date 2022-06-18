package agents;

import java.util.HashMap;
import java.util.List;

public interface CachedAgentsRemote {

	public HashMap<AID, Agent> getRunningAgents();
	
	public List<AID> getRemoteRunningAgentsAIDS();
	
	public void addRunningAgent(AID key, Agent agent);
	
	public Agent getByAID(AID aid);
	
	public void setRemoteRunningAgents(List<AID> agents);
}
