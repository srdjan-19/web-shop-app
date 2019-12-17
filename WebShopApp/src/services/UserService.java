package services;

import java.util.ArrayList;

import beans.data.Data;
import beans.entities.Administrator;
import beans.entities.Buyer;
import beans.entities.Deliverer;
import beans.entities.Restaraunt;
import beans.entities.User;

public class UserService {

	public ArrayList<User> getUsers() {
		return Data.getInstance().getUsers();
	}

	public ArrayList<Buyer> getBuyers() {
		return Data.getInstance().getBuyers();
	}

	public ArrayList<Administrator> getAdmins() {
		return Data.getInstance().getAdmins();
	}

	public ArrayList<Deliverer> getDeliverers() {
		return Data.getInstance().getDeliverers();
	}

	public int getCurrentNumberOfArticles() {
		return Data.getInstance().getPrepareOrder().size();
	}

	public boolean create(Buyer newBuyer, String path) {
		for (Buyer current : Data.getInstance().getBuyers()) {
			if (current.getUsername().equals(newBuyer.getUsername()) || current.getEmail().equals(newBuyer.getEmail()))
				return false;
		}

		for (Deliverer current : Data.getInstance().getDeliverers()) {
			if (current.getUsername().equals(newBuyer.getUsername()) || current.getEmail().equals(newBuyer.getEmail()))
				return false;
		}

		for (Administrator current : Data.getInstance().getAdmins()) {
			if (current.getUsername().equals(newBuyer.getUsername()) || current.getEmail().equals(newBuyer.getEmail()))
				return false;
		}

		Data.getInstance().getBuyers().add(newBuyer);
		Data.getInstance().getUsers().add(newBuyer);
		Data.getInstance().writeBuyers(path);

		return true;
	}

	public void removeBuyer(String buyer) {
		for (int i = 0; i < Data.getInstance().getBuyers().size(); i++) {
			if (Data.getInstance().getBuyers().get(i).getUsername().equals(buyer)) {
				Data.getInstance().getBuyers().remove(i);
			}
		}
	}

	public void removeDeliverer(String del) {
		for (int i = 0; i < Data.getInstance().getDeliverers().size(); i++) {
			if (Data.getInstance().getDeliverers().get(i).getUsername().equals(del)) {
				Data.getInstance().getDeliverers().remove(i);
			}
		}
	}

	public void removeAdmin(String admin) {
		for (int i = 0; i < Data.getInstance().getAdmins().size(); i++) {
			if (Data.getInstance().getAdmins().get(i).getUsername().equals(admin)) {
				Data.getInstance().getAdmins().remove(i);
			}
		}
	}

	public User signin(String un, String pw) {
		String username = un.trim();
		String password = pw.trim();

		for (int i = 0; i < Data.getInstance().getBuyers().size(); i++) {
			if (Data.getInstance().getBuyers().get(i).getUsername().equals(username)
					&& Data.getInstance().getBuyers().get(i).getPassword().equals(password)) {
				return Data.getInstance().getBuyers().get(i); // kupac je
			}
		}

		for (int i = 0; i < Data.getInstance().getDeliverers().size(); i++) {
			if (Data.getInstance().getDeliverers().get(i).getUsername().equals(username)
					&& Data.getInstance().getDeliverers().get(i).getPassword().equals(password)) {
				return Data.getInstance().getDeliverers().get(i); // dostavljac je
			}
		}

		for (int i = 0; i < Data.getInstance().getAdmins().size(); i++) {
			if (Data.getInstance().getAdmins().get(i).getUsername().equals(username)
					&& Data.getInstance().getAdmins().get(i).getPassword().equals(password)) {
				return Data.getInstance().getAdmins().get(i); // admin je
			}
		}

		return null;

	}

	public boolean addFavouriteRestaraunt(Buyer buyer, Restaraunt restaraunt, String path) {
		for (Restaraunt current : buyer.getFavRestaraunts()) {
			if (current.getName().equals(restaraunt.getName()))
				return false;
		}
		buyer.getFavRestaraunts().add(restaraunt);

		Data.getInstance().writeBuyers(path);

		return true;
	}
}
