package agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.naming.NamingException;

import models.AgentType;
import util.JndiTreeParser;

@Singleton
@LocalBean
@Remote(CachedAgentsRemote.class)
public class CachedAgents implements CachedAgentsRemote{

	HashMap<AID, Agent> runningAgents;
	List<AID> runningAgentsAIDS;
	List<AgentType> agentTypes;
	
	@EJB
	private JndiTreeParser jndiTreeParser;

	public CachedAgents() {
		runningAgents = new HashMap<>();
		runningAgentsAIDS = new ArrayList<>();
		agentTypes = new ArrayList<>();
	}
	
	@PostConstruct
	public void postConstruct(){
		agentTypes = getAvailableAgentTypes();
	}

	private List<AgentType> getAvailableAgentTypes() {
		try {
			return jndiTreeParser.parse();
		} catch (NamingException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	@Override
	public List<AgentType> getAgentTypes() {
		return agentTypes;
	}
	

	@Override
	public void setAgentTypes(List<AgentType> agentTypes) {
		this.agentTypes = agentTypes;
		
	}
	
	@Override
	public void addAgentTypes(List<AgentType> agentTypes) {
		this.agentTypes.addAll(agentTypes);
	}

	@Override
	public HashMap<AID, Agent> getRunningAgents() {
		return runningAgents;
	}
	
	@Override
	public void addAgentType(AgentType agentType) {
		agentTypes.add(agentType);
	}

	@Override
	public void addRunningAgent(AID key, Agent agent) {
		runningAgents.put(key, agent);
		runningAgentsAIDS.add(key);
	}
	
	@Override
	public Agent getByAID(AID aid) {
		
		for(Agent agent : runningAgents.values()) {
			
			String address = agent.getAid().getHost().getAddress();			
			String alias = agent.getAid().getHost().getAlias();			
			String name = agent.getAid().getName();			
			String agentType = agent.getAid().getType().getName();
			
			if(name.equals(aid.getName()) && address.equals(aid.getHost().getAddress()) && alias.equals(aid.getHost().getAlias()) && agentType.equals(aid.getType().getName())) {
				return agent;
			}
		}
		return null;
	}
	
	@Override
	public void setRemoteRunningAgents(List<AID> aids) {
		runningAgentsAIDS = aids;
	}

	@Override
	public List<AID> getRemoteRunningAgentsAIDS() {
		return runningAgentsAIDS;
	}

	@Override
	public void removeAgent(AID aid) {
		String address = aid.getHost().getAddress();
		String alias = aid.getHost().getAlias();
		String agentTypeName = aid.getType().getName();
		String name = aid.getName();
		
		for(AID agentAID: runningAgentsAIDS) {
			
			String runningAgentAddress = agentAID.getHost().getAddress();
			String runningAgentAlias = agentAID.getHost().getAlias();
			String runningAgentAgentTypeName = agentAID.getType().getName();
			String runningAgentName = agentAID.getName();
			
			if(address.equals(runningAgentAddress) && alias.equals(runningAgentAlias) && agentTypeName.equals(runningAgentAgentTypeName) && name.equals(runningAgentName)) {
				runningAgents.remove(agentAID);
				runningAgentsAIDS.remove(agentAID);
				break;
			}
		}
	}

	@Override
	public void removeAgentType(AgentType agentType) {
		agentTypes.remove(agentType);
		/*for(AgentType at: agentTypes) {
			if(at.getName().equals(agentType.getName()) && at.getHostAlias().equals(anObject)) {
				agentTypes.remove(agentType);
				
			}
		}*/
	}
}
