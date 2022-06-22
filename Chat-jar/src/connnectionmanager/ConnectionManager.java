package connnectionmanager;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import agents.AID;
import models.AgentType;
import models.User;

public interface ConnectionManager {
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<String> registerNode(String nodeAlias);
	
	@POST
	@Path("/node")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addNode(String nodeAlias);
	
	@POST
	@Path("/nodes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> getNodes();
	
	@DELETE
	@Path("/node/{alias}")
	public void deleteNode(@PathParam("alias") String nodeAlias);
	
	@DELETE
	@Path("/user/{alias}")
	public void removeLoggedInUser(@PathParam("alias") String nodeAlias);
	
	@DELETE
	@Path("/agent/{alias}")
	public void removeAgent(@PathParam("alias") String nodeAlias);
	
	@GET
	@Path("/node")
	@Produces(MediaType.TEXT_PLAIN)
	public String pingNode();
	
	@GET
	@Path("/agents/agentTypes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<AgentType> getAgentTypes();
	
	@POST
	@Path("/node/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void loginNotifyNodes();
	
	@POST
	@Path("/node/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void registerNotifyNodes();
	
	@POST
	@Path("/node/startAgent")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void agentRunningNofityNodes();
	
	@POST
	@Path("/node/agentTypes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void agentTypesNofityNodes();

	@POST
	@Path("/users/loggedIn")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void loggedInForNodes(List<User> users);

	@POST
	@Path("/users/registered")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void registeredForNodes(List<User> users);
	
	@POST
	@Path("/agents/runningAgents")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void runningAgentsForNodes(List<AID> agents);
	
	@POST
	@Path("/agents/agentTypes")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void agentTypesForNodes(List<AgentType> agentTypes);
	
}
