	package rest;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import agentmanager.AgentManagerRemote;
import agents.AID;
import agents.Agent;
import chatmanager.ChatManagerRemote;
import connnectionmanager.ConnectionManager;
import messagemanager.ACLMessage;
import messagemanager.MessageManagerRemote;
import models.AgentType;
import models.User;
import util.JNDILookup;

@Stateless
@LocalBean
@Path("/users")
public class ChatRestBean implements ChatRest {

	@EJB
	private MessageManagerRemote messageManager;	
	@EJB
	private ChatManagerRemote chatManager;
	@EJB
	private AgentManagerRemote agentManager;
	@EJB
	private ConnectionManager connectionManager;
	
	@Override
	public Response register(User user) {
		
		if(chatManager.register(user).equals(null)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		for (User loggedInUser : chatManager.loggedInUsers()) {
			if(loggedInUser.getHost().getAlias().equals(System.getProperty("jboss.node.name") + ":8080")) {
				ACLMessage message = new ACLMessage();
				message.receivers.add(new AID(loggedInUser.getUsername(), loggedInUser.getHost(), new AgentType()));
				message.userArgs.put("command", "GET_LOGGEDIN");
				messageManager.post(message);
			}
		}
		return Response
				.status(Response.Status.CREATED).entity("SUCCESS")
				.entity(user)
				.build();	
	}

	@Override
	public Response login(User user) {
		
		if(!chatManager.login(user)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		User currentUser = chatManager.findByUsername(user.getUsername());	
		
		String agentName = "ejb:Chat-ear/Chat-jar//" + "UserAgent" + "!" + Agent.class.getName() + "?stateful";
		agentManager.startAgent(agentName, new AID(currentUser.getUsername(), currentUser.getHost(), new AgentType("UserAgent")));
		
		for (User loggedInUser : chatManager.loggedInUsers()) {
			
			if(loggedInUser.getHost().getAlias().equals(System.getProperty("jboss.node.name") + ":8080")) {	
				ACLMessage messageLoggedInUsers = new ACLMessage();
				ACLMessage messageRunningAgents = new ACLMessage();
				
				AID aid = new AID(loggedInUser.getUsername(), loggedInUser.getHost(), new AgentType("UserAgent"));
				
				messageLoggedInUsers.receivers.add(aid);
				messageRunningAgents.receivers.add(aid);
				
				messageLoggedInUsers.userArgs.put("command", "GET_LOGGEDIN");
				messageRunningAgents.userArgs.put("command", "GET_RUNNING_AGENTS");
				
				messageManager.post(messageLoggedInUsers);
				messageManager.post(messageRunningAgents);
			}
		}
		return Response
				.status(Response.Status.OK).entity("SUCCESS")
				.entity(user)
				.build();
	}

	@Override
	public void getloggedInUsers(String username) {	
		
		User user = chatManager.findByUsername(username);
		ACLMessage message = new ACLMessage();
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_LOGGEDIN");
		
		messageManager.post(message);
	}
	
	@Override
	public void getRegisteredUsers(String username) {
		
		User user = chatManager.findByUsername(username);
		ACLMessage message = new ACLMessage();
		System.out.println(user.getHost().getAddress());
		message.receivers.add(new AID(username, user.getHost(), new AgentType("UserAgent")));
		message.userArgs.put("command", "GET_REGISTERED");
		
		messageManager.post(message);
	}

	@Override
	public Response logout(String username) {
		
		if(!chatManager.logout(username)) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		AID aid = new AID();
		
		User user = chatManager.findByUsername(username);
		
		if (user != null) {
			aid.setHost(user.getHost());		
			aid.setName(username);
			aid.setType(new AgentType("UserAgent"));
		}
		
		agentManager.stopAgent(aid);
		
		for (User loggedInUser : chatManager.loggedInUsers()) {
			
			ACLMessage message = new ACLMessage();
			message.receivers.add(new AID(loggedInUser.getUsername(), loggedInUser.getHost(), new AgentType("UserAgent")));
			message.userArgs.put("command", "GET_LOGGEDIN");
			
			messageManager.post(message);
		}
		
		return Response.status(Response.Status.OK).build();
	}
}
