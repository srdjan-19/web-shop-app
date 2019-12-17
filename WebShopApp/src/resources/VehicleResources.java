package resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.data.Data;
import beans.entities.Deliverer;
import beans.entities.Messages;
import beans.entities.User;
import beans.entities.Vehicle;
import beans.enums.Roles;
import services.VehicleService;

@Path("/vehicles")
public class VehicleResources {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	private VehicleService vehicleService = new VehicleService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getVehicles() {
		ctx.setAttribute("vehicles", vehicleService.getVehicles());

		return Response.ok(ctx.getAttribute("vehicles")).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Vehicle newVehicle) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.DELIVERER) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a ADMIN")).build();

		String path = ctx.getRealPath("/");

		Vehicle vehicle = vehicleService.create(newVehicle, path);
		if (vehicle == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Vehicle already exists with that registration number.")).build();
		} else {
			ctx.setAttribute("vehicles", vehicleService.getVehicles());

			return Response.ok(vehicle).build();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Vehicle update) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.DELIVERER) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a ADMIN")).build();

		if (update == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Fill the field first.")).build();

		String path = ctx.getRealPath("/");

		if (vehicleService.update(update, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Vehicle already exists with that registration number.")).build();
		} else {
			ctx.setAttribute("vehicles", vehicleService.getVehicles());

			return Response.ok(ctx.getAttribute("vehicles")).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.DELIVERER) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a ADMIN")).build();

		String path = ctx.getRealPath("/");

		if (vehicleService.remove(id, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Vehicle with id " + id + " does not exists.")).build();
		} else {
			ctx.setAttribute("vehicles", vehicleService.getVehicles());

			return Response.ok(ctx.getAttribute("vehicles")).build();
		}
	}

	@POST
	@Path("/assign/{delivererUN}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response assign(@PathParam("delivererUN") String delivererUsername, Vehicle vehicle) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.DELIVERER) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a ADMIN")).build();

		if (delivererUsername.equals("undefined"))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Please select DELIVERER first.")).build();

		String path = ctx.getRealPath("/");

		if (vehicleService.assign(delivererUsername.trim(), vehicle, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Vehicle is not available or deliverer already has his vehicle.")).build();
		} else {
			ctx.setAttribute("vehicles", vehicleService.getVehicles());
			ctx.setAttribute("deliverers", Data.getInstance().getDeliverers());

			return Response.ok(ctx.getAttribute("vehicles")).build();
		}

	}

	@POST
	@Path("/take")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response take(Vehicle vehicle) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.ADMIN) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be DELIVERER")).build();

		Deliverer deliverer = (Deliverer) user;
		String path = ctx.getRealPath("/");

		if (vehicleService.take(vehicle, deliverer, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Vehicle is not available.")).build();
		} else {
			ctx.setAttribute("vehicles", vehicleService.getVehicles());
			ctx.setAttribute("deliverers", Data.getInstance().getDeliverers());

			return Response.ok(ctx.getAttribute("vehicles")).build();
		}
	}
}
