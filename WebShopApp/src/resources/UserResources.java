package resources;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.data.Data;
import beans.entities.Administrator;
import beans.entities.Article;
import beans.entities.Buyer;
import beans.entities.Deliverer;
import beans.entities.Log;
import beans.entities.Messages;
import beans.entities.Restaraunt;
import beans.entities.User;
import beans.enums.Roles;
import services.UserService;

@Path("/users")
public class UserResources {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	private UserService userService = new UserService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		ctx.setAttribute("users", userService.getUsers());

		return Response.ok(ctx.getAttribute("users")).build();
	}

	@GET
	@Path("/buyers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findBuyers() {
		ctx.setAttribute("buyers", userService.getBuyers());

		return Response.ok(ctx.getAttribute("buyers")).build();
	}

	@GET
	@Path("/deliverers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findDeliverers() {
		ctx.setAttribute("deliverers", userService.getDeliverers());

		return Response.ok(ctx.getAttribute("deliverers")).build();
	}

	@GET
	@Path("/articles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCurrentNumberOfArticles() {
		User user = (User) request.getSession().getAttribute("user");

		if (user != null) {
			if (user.getRole().equals(Roles.DELIVERER))
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(new Messages("You need to be a ADMIN or BUYER.")).build();

			ctx.setAttribute("currentNumberOfArticles", userService.getCurrentNumberOfArticles());

			return Response.ok(ctx.getAttribute("currentNumberOfArticles")).build();
		}
		return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to sign in as ADMIN or BUYER.")).build();
	}

	@GET
	@Path("/signed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSigned() {
		User u = (User) request.getSession().getAttribute("user");

		if (u == null)
			return Response.ok(new Messages("No one is signed in.")).build();

		if (u.getRole().equals(Roles.ADMIN)) {
			Administrator admin = (Administrator) u;
			return Response.ok(admin).build();
		} else if (u.getRole().equals(Roles.BUYER)) {
			Buyer buyer = (Buyer) u;
			return Response.ok(buyer).build();
		} else if (u.getRole().equals(Roles.DELIVERER)) {
			Deliverer deliverer = (Deliverer) u;
			return Response.ok(deliverer).build();
		} else
			return Response.ok(new Messages("No one is signed in.")).build();
	}

	@GET
	@Path("/restaraunts/favourites")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFavourites() {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.DELIVERER) || user.getRole().equals(Roles.ADMIN))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be BUYER.")).build();

		Buyer buyer = (Buyer) user;

		ctx.setAttribute("favouriteRestaraunts", buyer.getFavRestaraunts());

		return Response.ok(ctx.getAttribute("favouriteRestaraunts")).build();
	}

	@POST
	@Path("/restaraunts/favourites")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFavouriteRestaraunt(Restaraunt restaraunt) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER)) {

			Buyer buyer = (Buyer) user;
			String path = ctx.getRealPath("/");

			if (userService.addFavouriteRestaraunt(buyer, restaraunt, path) == true) {

				ctx.setAttribute("favouriteRestaraunts", buyer.getFavRestaraunts());

				return Response.status(Response.Status.OK).entity(new Messages("Successfully added restaraunt in your favourites!")).build();
			} else {
				return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Restaraunt is already in your favourites")).build();
			}
		} else {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You must be BUYER!")).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Buyer newBuyer) {
		String path = ctx.getRealPath("/");

		if (userService.create(newBuyer, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(new Messages("Username or email are already taken.")).build();
		} else {
			ctx.setAttribute("buyers", userService.getBuyers());

			return Response.ok(ctx.getAttribute("buyers")).build();
		}
	}

	@POST
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signin(Log signInRequest) throws IOException {
		if (signInRequest == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Please enter your username and password.")).build();

		if (signInRequest.getUsername().equals("") && signInRequest.getPassword().equals(""))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Please enter your username and password.")).build();

		if (signInRequest.getUsername() == null || signInRequest.getUsername().equals(""))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Please enter your username.")).build();

		if (signInRequest.getPassword() == null || signInRequest.getPassword().equals(""))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Please enter your password.")).build();

		User user = userService.signin(signInRequest.getUsername(), signInRequest.getPassword());

		if (user == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Wrong username or password.")).build();
		} else {
			request.getSession().setAttribute("user", user);
			
			return Response.ok(user).build();
		}
	}

	@POST
	@Path("/signout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response signout() throws IOException {
		User user = (User) request.getSession().getAttribute("user");

		if (user == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Already signed out")).build();
		} else {
			request.getSession().setAttribute("user", null);
			Data.getInstance().setPrepareOrder(new ArrayList<Article>());
			
			return Response.ok(user).build();
		}
	}

	@POST
	@Path("/promote/{promoteRole}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response promote(@PathParam("promoteRole") String role, String username) throws IOException {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be an ADMIN.")).build();

		if (role.equals("undefined"))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Please select ROLE first.")).build();

		String promotion = role.trim();

		for (User current : Data.getInstance().getUsers()) {
			if (current.getUsername().equals(username)) {
				if (current.getRole().toString().equals(promotion))
					return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("User is already " + promotion)).build();

				if (current.getUsername().equals(username) && current.getRole().equals(Roles.ADMIN))
					return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You can't run from administrator obligations!")).build();

				if (current.getRole().equals(Roles.BUYER)) 
					userService.removeBuyer(current.getUsername());
				else if (current.getRole().equals(Roles.DELIVERER))
					userService.removeDeliverer(current.getUsername());
				else
					userService.removeAdmin(current.getUsername());

				if (promotion.equals("ADMIN")) {
					Administrator newAdmin = new Administrator(current.getUsername(), current.getPassword(),
							current.getFirstName(), current.getLastName(), current.getPhone(), current.getEmail(), current.getRegistered());
					Data.getInstance().getAdmins().add(newAdmin);
					current.setRole(Roles.ADMIN);
					
				} else if (promotion.equals("DELIVERER")) {
					Deliverer newDeliverer = new Deliverer(current.getUsername(), current.getPassword(),
							current.getFirstName(), current.getLastName(), current.getPhone(), current.getEmail(),
							current.getRegistered(), " ");

					Data.getInstance().getDeliverers().add(newDeliverer);
					current.setRole(Roles.DELIVERER);

				} else {
					Buyer newBuyer = new Buyer(current.getUsername(), current.getPassword(), current.getFirstName(),
							current.getLastName(), current.getPhone(), current.getEmail(), current.getRegistered(), "", "");

					Data.getInstance().getBuyers().add(newBuyer);
					current.setRole(Roles.BUYER);

				}
			}
		}

		String path = ctx.getRealPath("/");

		Data.getInstance().writeAdmins(path);
		Data.getInstance().writeBuyers(path);
		Data.getInstance().writeDeliverer(path);

		ctx.setAttribute("users", Data.getInstance().getUsers());
		ctx.setAttribute("admins", Data.getInstance().getAdmins());
		ctx.setAttribute("buyers", Data.getInstance().getBuyers());
		ctx.setAttribute("deliverers", Data.getInstance().getDeliverers());

		return Response.ok(ctx.getAttribute("users")).build();

	}

	@POST
	@Path("/remove/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("username") String username) {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be an ADMIN.")).build();

		userService.removeBuyer(username);
		return Response.ok(userService.getBuyers()).build();
	}
}
