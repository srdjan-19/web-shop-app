package services;

import java.util.ArrayList;
import java.util.Date;

import beans.data.Data;
import beans.entities.Administrator;
import beans.entities.Article;
import beans.entities.Buyer;
import beans.entities.Deliverer;
import beans.entities.Order;
import beans.enums.State;

public class OrderServices {

	public ArrayList<Order> getOrders() {
		return Data.getInstance().getOrders();
	}

	public ArrayList<Order> getMyOrders(Buyer buyer) {
		ArrayList<Order> mine = new ArrayList<Order>();

		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			String username = "";

			try {
				username = Data.getInstance().getOrders().get(i).getBuyer().getUsername();
			} catch (Exception e) {
				username = "";
			}

			if (!username.equals("")) {
				if (Data.getInstance().getOrders().get(i).getBuyer().getUsername().equals(username)
						&& Data.getInstance().getOrders().get(i).getState().equals(State.DELIVERIED)) {
					mine.add(Data.getInstance().getOrders().get(i));
				}
			}

		}
		return mine;
	}

	public ArrayList<Order> getUndeliveredOrders() {
		ArrayList<Order> undelivered = new ArrayList<Order>();

		for (Order order : Data.getInstance().getOrders()) {
			if (order.getState().equals(State.ORDERED)) {
				undelivered.add(order);
			}
		}

		return undelivered;
	}

	public Order getMyDelivery(Deliverer deliverer) {
		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			if (Data.getInstance().getOrders().get(i).getDeliverer().getUsername() != null)
				if (Data.getInstance().getOrders().get(i).getDeliverer().getUsername().equals(deliverer.getUsername())
						&& Data.getInstance().getOrders().get(i).getState().equals(State.IN_PROGRESS)) {

					return Data.getInstance().getOrders().get(i);
				}
		}
		return null;
	}

	public boolean addArticleToOrder(Article orderedArticle, String quantity, String path) {
		orderedArticle.setNop(quantity);
		orderedArticle.increasePopularity();

		for (int i = 0; i < Data.getInstance().getArticles().size(); i++) {
			if (Data.getInstance().getArticles().get(i).getName().equals(orderedArticle.getName())) {
				Data.getInstance().getArticles().get(i).increasePopularity();
			}
		}

		Data.getInstance().getPrepareOrder().add(orderedArticle);

		return true;
	}

	public Order completeOrderAsBuyer(Buyer buyer, Order order, String path) {
		Date date = new Date();

		String note = "";
		try {
			note = order.getNote();
		} catch (Exception e) {
			note = "";
		}

		Order newOrder = new Order(Data.getInstance().getPrepareOrder(), date.toString(), buyer.getUsername(),
				new Deliverer(), State.ORDERED, note);

		for (int i = 0; i < Data.getInstance().getBuyers().size(); i++) {
			if (Data.getInstance().getBuyers().get(i).getUsername().equals(buyer.getUsername())) {
				int racun = 0;

				for (Article article : Data.getInstance().getPrepareOrder()) {
					racun += article.getPrice() * Integer.parseInt(article.getNop());
					newOrder.setBill(racun);

				}

				Data.getInstance().getBuyers().get(i).getOrders().add(newOrder);
				Data.getInstance().getOrders().add(newOrder);

				Data.getInstance().writeArticles(path);
				Data.getInstance().writeOrders(path);

				Data.getInstance().setPrepareOrder(new ArrayList<Article>());

				return newOrder;
			}
		}
		return null;
	}

	public Order completeOrderAsAdmin(Administrator admin, String buyerUN, String delivererUN, Order order,
			String path) {
		Date date = new Date();
		Deliverer deliverer = new Deliverer();
		String note = "";
		State state = State.ORDERED;

		if (!delivererUN.equals("undefined"))
			for (int i = 0; i < Data.getInstance().getDeliverers().size(); i++) {
				if (Data.getInstance().getDeliverers().get(i).getUsername().equals(delivererUN)) {
					deliverer = Data.getInstance().getDeliverers().get(i);
					state = State.IN_PROGRESS;
				}
			}

		try {
			note = order.getNote();
		} catch (Exception e) {
			note = "";
		}

		Order newOrder = new Order(Data.getInstance().getPrepareOrder(), date.toString(), buyerUN, deliverer, state,
				note);

		for (int i = 0; i < Data.getInstance().getBuyers().size(); i++) {
			if (Data.getInstance().getBuyers().get(i).getUsername().equals(buyerUN)) {
				int racun = 0;

				for (Article article : Data.getInstance().getPrepareOrder()) {
					racun += article.getPrice() * Integer.parseInt(article.getNop());
					newOrder.setBill(racun);

				}

				Data.getInstance().getBuyers().get(i).getOrders().add(newOrder);
				Data.getInstance().getOrders().add(newOrder);

				Data.getInstance().writeArticles(path);
				Data.getInstance().writeOrders(path);

				Data.getInstance().setPrepareOrder(new ArrayList<Article>());

				return newOrder;
			}
		}

		return null;
	}

	public ArrayList<Order> takeOrder(Order order, Deliverer deliverer, String path) {
		ArrayList<Order> ret = new ArrayList<Order>();

		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			if (Data.getInstance().getOrders().get(i).getDeliverer().getUsername() != null)
				if (Data.getInstance().getOrders().get(i).getDeliverer().getUsername().equals(deliverer.getUsername())
						&& Data.getInstance().getOrders().get(i).getState().equals(State.IN_PROGRESS)) {
					return ret;
				}
		}

		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			if (Data.getInstance().getOrders().get(i).getDateOfOrder().equals(order.getDateOfOrder())
					&& Data.getInstance().getOrders().get(i).getState().equals(State.ORDERED)) {

				Data.getInstance().getOrders().get(i).setState(State.IN_PROGRESS);
				Data.getInstance().getOrders().get(i).setDeliverer(deliverer);

				deliverer.getToDelivery().add(order);
				
				Data.getInstance().writeOrders(path);

			}
		}

		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			if (Data.getInstance().getOrders().get(i).getState().equals(State.ORDERED)) {
				ret.add(Data.getInstance().getOrders().get(i));
			}
		}

		return ret;
	}

	public boolean deliveryDone(Deliverer deliverer, String path) {
		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			if (Data.getInstance().getOrders().get(i).getDeliverer().getUsername() != null)
				if (Data.getInstance().getOrders().get(i).getDeliverer().getUsername().equals(deliverer.getUsername())
						&& Data.getInstance().getOrders().get(i).getState().equals(State.IN_PROGRESS)) {
					Data.getInstance().getOrders().get(i).setState(State.DELIVERIED);

					Data.getInstance().writeOrders(path);

					return true;
				}
		}
		return false;
	}

	public ArrayList<Order> remove(String id, String path) {
		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			if (Data.getInstance().getOrders().get(i).getId().toString().equals(id)) {
				Data.getInstance().getOrders().remove(i);
				
				Data.getInstance().writeOrders(path);
			}
		}

		return Data.getInstance().getOrders();
	}

	public boolean update(Order update, String path) {
		for (int i = 0; i < Data.getInstance().getOrders().size(); i++) {
			if (Data.getInstance().getOrders().get(i).getId().toString().equals(update.getId().toString())) {

				if (!(update.getBuyer().getUsername().equals("undefined"))) {

					for (Buyer buyer : Data.getInstance().getBuyers()) {
						if (buyer.getUsername().equals(update.getBuyer().getUsername())) {
							Data.getInstance().getOrders().get(i).setBuyer(buyer);
						}
					}
				}

				if (!(update.getDeliverer().getUsername().equals("undefined"))) {
					for (Deliverer deliverer : Data.getInstance().getDeliverers()) {
						if (deliverer.getUsername().equals(update.getDeliverer().getUsername())) {
							Data.getInstance().getOrders().get(i).setDeliverer(deliverer);
						}
					}
				}

				if (update != null) {

					if (!(update.getNote() == null))
						Data.getInstance().getOrders().get(i).setNote(update.getNote());

					if (!(update.getState() == null))
						Data.getInstance().getOrders().get(i).setState(update.getState());

				}

				Data.getInstance().writeOrders(path);

				return true;
			}
		}
		return false;
	}
}
