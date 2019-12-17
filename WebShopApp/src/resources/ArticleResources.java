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
import beans.entities.User;
import beans.enums.Roles;
import services.ArticleServices;

@Path("/articles")
public class ArticleResources {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	private ArticleServices articleServices = new ArticleServices();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getArticles() {
		ctx.setAttribute("articles", articleServices.getArticles());

		return Response.ok(ctx.getAttribute("articles")).build();
	}

	@GET
	@Path("/popular/dishes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPopularDishes() {
		if (ctx.getAttribute("top10dishes") == null)
			ctx.setAttribute("top10dishes", articleServices.getMostPopularDishes());

		if (ctx.getAttribute("top10dishes") != null)
			return Response.ok(ctx.getAttribute("top10dishes")).build();
		else
			return Response.ok(articleServices.getMostPopularDishes()).build();
	}

	@GET
	@Path("/popular/drinks")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findPopularDrinks() {
		if (ctx.getAttribute("top10drinks") == null)
			ctx.setAttribute("top10drinks", articleServices.getMostPopularDrinks());

		if (ctx.getAttribute("top10drinks") != null)
			return Response.ok(ctx.getAttribute("top10drinks")).build();
		else
			return Response.ok(articleServices.getMostPopularDrinks()).build();
	}

	@GET
	@Path("/current-number")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentNumberOfArticles() {
		User user = (User) request.getSession().getAttribute("user");

		if (user != null) {
			if (user.getRole().equals(Roles.DELIVERER))
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new Messages("You need to be a ADMIN or BUYER.")).build();

			ctx.setAttribute("currentNumberOfArticles", articleServices.getCurrentNumberOfArticles());

			if (ctx.getAttribute("currentNumberOfArticles") != null)
				return Response.ok(ctx.getAttribute("currentNumberOfArticles")).build();
			else
				return Response.ok(articleServices.getCurrentNumberOfArticles()).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to sign in as ADMIN or BUYER.")).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Article newArticle) {
		String path = ctx.getRealPath("/");

		if (newArticle.getUnit() == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Please select unit of article.")).build();

		boolean odziv = articleServices.create(newArticle, path);

		if (odziv == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Article already exists.")).build();
		} else {
			ctx.setAttribute("articles", articleServices.getArticles());

			return Response.ok(ctx.getAttribute("articles")).build();
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Article update) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be ADMIN.")).build();

		if (update == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Fill the fields first.")).build();

		String path = ctx.getRealPath("/");

		if (articleServices.update(update, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Article with that name already exists.")).build();
		} else {
			ctx.setAttribute("articles", articleServices.getArticles());

			return Response.ok(ctx.getAttribute("articles")).build();
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

		if (articleServices.remove(id, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity("Article with id " + id + " doesnt exists.").build();
		} else {
			return Response.ok(articleServices.getArticles()).build();
		}
	}

}
