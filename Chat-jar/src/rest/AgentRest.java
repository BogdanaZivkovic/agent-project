package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import agents.AID;
import messagemanager.ACLMessage;

@Remote
public interface AgentRest {
	
	@GET
	@Path("/classes")
	@Produces(MediaType.APPLICATION_JSON)
	public void getAvailableAgentClasses(@HeaderParam("Authorization") String username);

	@GET
	@Path("/running")
	public void getRunningAgents(@HeaderParam("Authorization") String username);
	
	@PUT
	@Path("/running/{type}/{name}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response startAgent(@PathParam("type") String type, @PathParam("name") String name);
	
	@PUT
	@Path("/running")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response stopAgent(AID aid);
	
	@POST
	@Path("/messages")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendACLMessage(ACLMessage message);
	
	@GET
	@Path("/messages")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getPerformatives(@HeaderParam("Authorization") String username);

}
