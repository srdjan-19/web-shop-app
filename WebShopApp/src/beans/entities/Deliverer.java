package beans.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import beans.data.Data;
import beans.enums.Roles;

public class Deliverer extends User implements Serializable {

	private static final long serialVersionUID = -8236324680671252827L;

	private Vehicle vehicle;
	private ArrayList<Order> toDelivery = new ArrayList<Order>();

	public Deliverer() {
		this.role = Roles.DELIVERER;
		this.registered = formatDate(new Date());
		this.toDelivery = new ArrayList<Order>();
	}

	public Deliverer(String un, String pw, String fn, String ln, String p, String e, String rd, String vRN) {
		this.username = un;
		this.password = pw;
		this.firstName = fn;
		this.lastName = ln;
		this.phone = p;
		this.email = e;
		this.role = Roles.DELIVERER;
		this.registered = rd;
		this.toDelivery = new ArrayList<Order>();
		this.vehicle = findMyVehicle(vRN);
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public ArrayList<Order> getToDelivery() {
		return toDelivery;
	}

	public void setToDelivery(ArrayList<Order> toDelivery) {
		this.toDelivery = toDelivery;
	}

	private Vehicle findMyVehicle(String vehicleRN) {
		for (Vehicle current : Data.getInstance().getVehicles()) {
			if (current.getRegistrationNumber().equals(vehicleRN)) {
				return current;
			}
		}

		return new Vehicle();
	}

}
