package resources;

import java.util.ArrayList;

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
import beans.entities.Administrator;
import beans.entities.Article;
import beans.entities.Buyer;
import beans.entities.Deliverer;
import beans.entities.Messages;
import beans.entities.Order;
import beans.entities.User;
import beans.enums.Roles;
import beans.enums.State;
import services.OrderServices;

@Path("/orders")
public class OrderResources {

	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;

	private OrderServices orderServices = new OrderServices();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		if (orderServices.getOrders().size() == 0)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("No orders.")).build();
		else {
			if (ctx.getAttribute("orders") == null)
				ctx.setAttribute("orders", orderServices.getOrders());

			return Response.ok(ctx.getAttribute("orders")).build();
		}
	}

	@GET
	@Path("/undelivered")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findUndelivered() {
		if (orderServices.getUndeliveredOrders().size() == 0)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("No new orders.")).build();
		else {
			if (ctx.getAttribute("undeliveredOrders") == null)
				ctx.setAttribute("undeliveredOrders", orderServices.getUndeliveredOrders());

			return Response.ok(ctx.getAttribute("undeliveredOrders")).build();
		}
	}

	@GET
	@Path("/articles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findOrderedArticles() {
		if (Data.getInstance().getPrepareOrder() == null || Data.getInstance().getPrepareOrder().size() == 0) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You didnt add any article in your order.")).build();
		} else {
			if (ctx.getAttribute("prepareOrder") == null)
				ctx.setAttribute("prepareOrder", Data.getInstance().getPrepareOrder());

			return Response.ok(ctx.getAttribute("prepareOrder")).build();
		}
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findMyOrders() {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.ADMIN) || user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a BUYER.")).build();

		Buyer buyer = (Buyer) user;

		if (orderServices.getMyOrders(buyer).size() == 0)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("No orders.")).build();
		else {
			ctx.setAttribute("myOrders", orderServices.getMyOrders(buyer));

			return Response.ok(ctx.getAttribute("myOrders")).build();
		}
	}

	@POST
	@Path("/take")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response takeOrder(Order order) {
		if (order == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Select an order first!")).build();

		if (order.getState().equals(State.IN_PROGRESS)) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Order is already taken!")).build();
		}

		if (order.getState().equals(State.CANCELED)) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Order is canceled!")).build();
		}

		if (order.getState().equals(State.DELIVERIED)) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Order is already delivered!")).build();
		}

		String path = ctx.getRealPath("/");
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.ADMIN) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a deliverer.")).build();

		Deliverer deliverer = (Deliverer) user;

		Order myDelivery = orderServices.getMyDelivery(deliverer);

		if (myDelivery != null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Delivery your order first!")).build();

		ArrayList<Order> undelivered = orderServices.takeOrder(order, deliverer, path);

		ctx.setAttribute("undeliveredOrders", undelivered);

		return Response.ok(ctx.getAttribute("undeliveredOrders")).build();
	}

	@POST
	@Path("/articles/{quantity}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addArticle(@PathParam("quantity") String quantity, Article orderedArticle) {
		String path = ctx.getRealPath("/");

		if (quantity.equals("undefined"))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Set quantity first.")).build();

		User user = (User) request.getSession().getAttribute("user");

		if (user == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to sign in as BUYER or ADMIN")).build();

		if (user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a ADMIN or BUYER.")).build();

		boolean odziv = orderServices.addArticleToOrder(orderedArticle, quantity, path);

		if (odziv == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Something went wrong.")).build();
		} else {
			ctx.setAttribute("prepareOrder", Data.getInstance().getPrepareOrder());

			return Response.ok(new Messages("You have been successfully add an article!")).build();
		}
	}

	@POST
	@Path("/complete/{buyerUN}/{delivererUN}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response completeOrder(@PathParam("buyerUN") String newBuyerUN, @PathParam("delivererUN") String newDelivererUN, Order order) {
		if (Data.getInstance().getPrepareOrder().size() == 0)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to add articles first.")).build();

		String buyerUsername = newBuyerUN.trim();
		String delivererUsername = newDelivererUN.trim();

		String path = ctx.getRealPath("/");

		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.DELIVERER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a ADMIN or BUYER.")).build();

		Order odziv;

		if (user.getRole().equals(Roles.BUYER)) {

			Buyer buyer = (Buyer) user;

			odziv = orderServices.completeOrderAsBuyer(buyer, order, path);
		} else {

			if (newBuyerUN.equals("undefined"))
				return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to select BUYER."))
						.build();

			Administrator admin = (Administrator) user;

			odziv = orderServices.completeOrderAsAdmin(admin, buyerUsername, delivererUsername, order, path);
		}

		if (odziv != null) {
			ctx.setAttribute("orders", Data.getInstance().getOrders());
			ctx.setAttribute("prepareOrder", new ArrayList<Article>());
			Data.getInstance().setPrepareOrder(new ArrayList<Article>());
			return Response.ok(odziv).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Something went wrong!")).build();
		}
	}

	@GET
	@Path("/delivery")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMyDelivery() {
		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.ADMIN) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a DELIVERER.")).build();

		Deliverer deliverer = (Deliverer) user;

		try {
			Order ret = orderServices.getMyDelivery(deliverer);

			if (ctx.getAttribute("myDelivery") == null)
				ctx.setAttribute("myDelivery", ret);

			return Response.ok((Order) ctx.getAttribute("myDelivery")).build();

		} catch (Exception e) {
			return Response.ok(new Messages("You dont have any delivery.")).build();
		}

	}

	@POST
	@Path("/done")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deliveryDone() {
		String path = ctx.getRealPath("/");

		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.BUYER) || user.getRole().equals(Roles.ADMIN))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to be a DELIVERER.")).build();

		Deliverer deliverer = (Deliverer) user;

		if (deliverer.getVehicle().getRegistrationNumber() == null)
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You need to have an vehicle!")).build();

		if (orderServices.deliveryDone(deliverer, path) == true) {

			ctx.setAttribute("orders", orderServices.getOrders());
			ctx.setAttribute("myDelivery", null);

			return Response.ok(ctx.getAttribute("orders")).build();
		} else {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("You havent been assigend to delivery any order!")).build();
		}
	}

	@DELETE
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeOrder(@PathParam("id") String id) {
		String path = ctx.getRealPath("/");

		User user = (User) request.getSession().getAttribute("user");

		if (user.getRole().equals(Roles.DELIVERER) || user.getRole().equals(Roles.BUYER))
			return Response.status(Response.Status.FORBIDDEN).entity(new Messages("You are not ADMIN!")).build();

		return Response.ok(orderServices.remove(id, path)).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(Order update) {
		if (update.getBuyer().getUsername().equals("undefined"))
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Select buyer.")).build();

		String path = ctx.getRealPath("/");


		if (orderServices.update(update, path) == false) {
			return Response.status(Response.Status.BAD_REQUEST).entity(new Messages("Order with that date already exists.")).build();
		} else {
			ctx.setAttribute("orders", orderServices.getOrders());

			return Response.ok(ctx.getAttribute("orders")).build();
		}
	}

}
