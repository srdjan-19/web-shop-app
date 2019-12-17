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

import beans.entities.Article;
import beans.entities.Messages;
import beans.entities.Restaraunt;
import beans.entities.User;
import beans.enums.Roles;
import services.RestarauntServices;

@Path("/restaraunts")
public class RestarauntResources {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	private RestarauntServices restarauntServices = new RestarauntServices();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		ctx.setAttribute("restaraunts", restarauntServices.getRestaraunts());

		return Response.ok(ctx.getAttribute("restaraunts")).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Restaraunt newRestaraunt) {
		if (newRestaraunt.getCategory() == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to select CATEGORY.")).build();

		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be ADMIN.")).build();

		String path = ctx.getRealPath("/");

		if (restarauntServices.create(newRestaraunt, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Restaraunt already exists with that name or on that address.")).build();
		} else {
			ctx.setAttribute("restaraunts", restarauntServices.getRestaraunts());

			return Response.ok(ctx.getAttribute("restaraunts")).build();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Restaraunt update) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be ADMIN.")).build();

		if (update == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Fill the field first.")).build();

		String path = ctx.getRealPath("/");

		if (restarauntServices.update(update, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Restaraunt already exists with that name or on that address.")).build();
		} else {
			ctx.setAttribute("restaraunts", restarauntServices.getRestaraunts());

			return Response.ok(ctx.getAttribute("restaraunts")).build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be ADMIN.")).build();
		
		String path = ctx.getRealPath("/");

		if ( restarauntServices.remove(id, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Restaraunt with id " + id + " doesnt exists.")).build();
		} else {
			return Response.ok(restarauntServices.getRestaraunts()).build();
		}
	}

	@POST
	@Path("/{id}/articles")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addArticle(@PathParam("id") String id, Article article) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be ADMIN.")).build();

		if (id.equals("undefined"))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Select an restaraunt first!")).build();


		String path = ctx.getRealPath("/");

		Restaraunt restaraunt = restarauntServices.addArticle(id, article, path);

		if (restaraunt == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("This article is already on your list!")).build();
		} else {
			return Response.ok(new Messages("Successfully added to menu in restaraunt " + restaraunt.getName())).build();
		}
	}

}
