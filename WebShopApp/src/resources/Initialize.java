package resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.data.Data;

@Path("/init")
public class Initialize {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response init() {
		String path = ctx.getRealPath("/");

		if (ctx.getAttribute("articles") == null) {
			Data.getInstance().readArticles(path);
			ctx.setAttribute("articles", Data.getInstance().getArticles());
		}

		if (ctx.getAttribute("restaraunts") == null) {
			Data.getInstance().readRestaraunts(path);
			ctx.setAttribute("restaraunts", Data.getInstance().getRestaraunts());
		}

		if (ctx.getAttribute("admins") == null) {
			Data.getInstance().readAdmins(path);
			ctx.setAttribute("admins", Data.getInstance().getAdmins());
		}

		if (ctx.getAttribute("buyers") == null) {
			Data.getInstance().readBuyers(path);
			ctx.setAttribute("buyers", Data.getInstance().getBuyers());
		}

		if (ctx.getAttribute("vehicles") == null) {
			Data.getInstance().readVehicles(path);
			ctx.setAttribute("vehicles", Data.getInstance().getVehicles());
		}

		if (ctx.getAttribute("deliverers") == null) {
			Data.getInstance().readDeliverers(path);
			ctx.setAttribute("deliverers", Data.getInstance().getDeliverers());
		}

		if (ctx.getAttribute("orders") == null) {
			Data.getInstance().readOrders(path);
			ctx.setAttribute("orders", Data.getInstance().getOrders());
		}

		if (ctx.getAttribute("users") == null) {
			ctx.setAttribute("users", Data.getInstance().getUsers());
		}

		return Response.status(Response.Status.OK).entity(ctx.getAttribute("users")).build();
	}

}
