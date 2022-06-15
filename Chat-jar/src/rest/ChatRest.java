package rest;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import models.User;

@Remote
public interface ChatRest {
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(User user);
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(User user);
	
	@GET
	@Path("/loggedIn")
	public void getloggedInUsers(@HeaderParam("Authorization") String username);
	
	@GET
	@Path("/registered")
	public void getRegisteredUsers(@HeaderParam("Authorization") String username);
	
	@DELETE
	@Path("/loggedIn/{user}")
	public Response logout(@PathParam("user") String username);
	
}
