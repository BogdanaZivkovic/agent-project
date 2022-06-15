package agents;

import java.util.HashMap;

public interface CachedAgentsRemote {

	public HashMap<AID, Agent> getRunningAgents();
	public void addRunningAgent(AID key, Agent agent);
	public Agent getByAID(AID aid);
}
