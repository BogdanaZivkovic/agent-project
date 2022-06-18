package agents;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

@Singleton
@LocalBean
@Remote(CachedAgentsRemote.class)
public class CachedAgents implements CachedAgentsRemote{

	HashMap<AID, Agent> runningAgents;
	List<AID> runningAgentsAIDS;

	public CachedAgents() {
		runningAgents = new HashMap<>();
		runningAgentsAIDS = new ArrayList<>();
	}

	@Override
	public HashMap<AID, Agent> getRunningAgents() {
		return runningAgents;
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
			System.out.print("AGENT" + address);
			System.out.print("AID" + aid.getHost().getAddress());
			
			String alias = agent.getAid().getHost().getAlias();
			System.out.print("AGENT" + alias);
			System.out.print("AID" + aid.getHost().getAlias());
			
			String name = agent.getAid().getName();
			System.out.print("AGENT" + name);
			System.out.print("AID" + aid.getName());
			
			String agentType = agent.getAid().getType().getName();
			System.out.print("AGENT" + agentType);
			System.out.print("AID" + aid.getType().getName());
			
			if(name.equals(aid.getName()) && address.equals(aid.getHost().getAddress()) && alias.equals(aid.getHost().getAlias()) && agentType.equals(aid.getType().getName())) {
				return agent;
			}
		}
		return null;
	}
	
	private String getNodeAddress() {
		try {
			MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
			ObjectName http = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
			return (String) mBeanServer.getAttribute(http, "boundAddress");
		} catch (MalformedObjectNameException | InstanceNotFoundException | AttributeNotFoundException | ReflectionException | MBeanException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public void setRemoteRunningAgents(List<AID> aids) {
		runningAgentsAIDS = aids;
	}

	@Override
	public List<AID> getRemoteRunningAgentsAIDS() {
		return runningAgentsAIDS;
	}
}
