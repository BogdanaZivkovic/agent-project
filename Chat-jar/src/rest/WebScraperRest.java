package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import dto.SupplyDTO;


public interface WebScraperRest {

	@GET
	@Path("/clothingItems/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void getClothingItems(@PathParam("user") String username);

	@POST
	@Path("/supplyClothingItems")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	void supplyClothingItems(SupplyDTO supplyDTO);
}
