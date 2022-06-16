package rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import agentmanager.AgentManagerRemote;
import agents.AID;
import chatmanager.ChatManagerRemote;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.AgentType;
import models.User;

@Stateless
@Path("/agents")
public class AgentRestBean implements AgentRest{	

	@EJB
	AgentManagerRemote agentManager;

	@EJB
	MessageManagerRemote messageManager;
	
	@EJB
	ChatManagerRemote chatManager;

	@Override
	public void getAvailableAgentClasses(String username) {
		User user = chatManager.findByUsername(username);
		
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_AGENT_TYPES");
		
		messageManager.post(message);
		
	}
	
	@Override
	public void getRunningAgents(String username) {
		User user = chatManager.findByUsername(username);
		
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_RUNNING_AGENTS");
		
		messageManager.post(message);
	}

	@Override
	public Response startAgent(String type, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response stopAgent(AID aid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendACLMessage(ACLMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPerformatives(String username) {
		// TODO Auto-generated method stub
		
	}

}
