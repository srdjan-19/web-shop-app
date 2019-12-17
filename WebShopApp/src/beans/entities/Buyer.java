package beans.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonBackReference;

import beans.data.Data;
import beans.enums.Roles;

public class Buyer extends User implements Serializable {

	private static final long serialVersionUID = 2577281366398966658L;

	private ArrayList<Restaraunt> favRestaraunts;

	@JsonBackReference(value = "orders")
	private ArrayList<Order> orders;

	public Buyer() {
		this.role = Roles.BUYER;
		this.registered = formatDate(new Date());
		this.orders = new ArrayList<Order>();
		this.favRestaraunts = new ArrayList<Restaraunt>();
	}

	public Buyer(String un, String pw, String fn, String ln, String p, String e, String rd, String orders,
			String restorani) {
		this.username = un;
		this.password = pw;
		this.firstName = fn;
		this.lastName = ln;
		this.phone = p;
		this.email = e;
		this.role = Roles.BUYER;
		this.registered = rd;
		this.orders = findMyOrders(orders);
		this.favRestaraunts = findMyRestaraunts(restorani);
	}

	private ArrayList<Restaraunt> findMyRestaraunts(String restorani) {
		String[] names = restorani.split(":");

		this.favRestaraunts = new ArrayList<Restaraunt>();

		ArrayList<Restaraunt> ret = new ArrayList<Restaraunt>();

		for (String rest_name : names) {
			for (Restaraunt rest : Data.getInstance().getRestaraunts()) {
				if (rest.getName().equals(rest_name)) {
					ret.add(rest);
				}
			}
		}

		return ret;

	}

	private ArrayList<Order> findMyOrders(String orders) {
		String[] dates = orders.split(",");

		this.orders = new ArrayList<Order>();
		ArrayList<Order> ret = new ArrayList<Order>();

		for (String date : dates) {
			for (Order order : Data.getInstance().getOrders()) {
				if (order.getDateOfOrder().equals(date)) {
					ret.add(order);
				}
			}
		}

		return ret;

	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	public ArrayList<Restaraunt> getFavRestaraunts() {
		return favRestaraunts;
	}

	public void setFavRestaraunts(ArrayList<Restaraunt> favRestaraunts) {
		this.favRestaraunts = favRestaraunts;
	}

}
