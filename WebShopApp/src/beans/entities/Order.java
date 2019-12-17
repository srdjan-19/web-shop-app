package beans.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import beans.data.Data;
import beans.enums.State;

public class Order implements Serializable {

	private static final long serialVersionUID = -4886588362402545462L;
	
	private UUID id;
	private String dateOfOrder;
	private State state;
	private String note;
	private ArrayList<Article> articles;
	private Deliverer deliverer;
	private int bill;
	private Buyer buyer;

	public Order() {
		this.deliverer = new Deliverer();
		this.buyer = new Buyer();
		this.setId(UUID.randomUUID());
		this.dateOfOrder = getDate(new Date());
	}

	private String getDate(Date d) {
		String date = d.toString();
		return date;
	}

	public Order(String id, String a, String d, String b, String del, String s, String n) {
		this.id = UUID.fromString(id);
		this.articles = findMyArticles(a);
		this.dateOfOrder = d;
		this.buyer = findMyBuyer(b);
		this.deliverer = findMyDeliverer(del);
		this.state = findMyState(s);
		this.note = n;
		this.bill = countMyBill();

	}
	
	public Order(ArrayList<Article> articles, String date, String buyerUsername, Deliverer deliverer, State state, String note) {
		this.id = UUID.randomUUID();
		this.articles = articles;
		this.dateOfOrder = date;
		this.buyer = findMyBuyer(buyerUsername);
		this.deliverer = deliverer;
		this.state = state;
		this.note = note;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	private int countMyBill() {
		int racun = 0;

		for (Article article : this.articles) {
			racun += article.getPrice() * Integer.parseInt(article.getNop());
		}

		return racun;
	}

	public ArrayList<Article> getArticle() {
		return articles;
	}

	public void setArticle(ArrayList<Article> article) {
		this.articles = article;
	}

	public String getDateOfOrder() {
		return dateOfOrder;
	}

	public void setDateOfOrder(String dateOfOrder) {
		this.dateOfOrder = dateOfOrder;
	}

	public Buyer getBuyer() {
		return buyer;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	public Deliverer getDeliverer() {
		return deliverer;
	}

	public void setDeliverer(Deliverer deliverer) {
		this.deliverer = deliverer;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	private State findMyState(String s) {
		if (s.equals("ORDERED"))
			return State.ORDERED;
		else if (s.equals("IN_PROGRESS"))
			return State.IN_PROGRESS;
		else if (s.equals("CANCELED"))
			return State.CANCELED;
		else
			return State.DELIVERIED;
	}

	private Deliverer findMyDeliverer(String del) {
		if (del.equals("null"))
			return new Deliverer();

		for (Deliverer current : Data.getInstance().getDeliverers()) {
			if (current.getUsername().equals(del))
				return current;
		}

		return new Deliverer();
	}

	private Buyer findMyBuyer(String b) {
		for (Buyer current : Data.getInstance().getBuyers()) {
			if (current.getUsername().equals(b))
				return current;
		}

		return null;
	}

	private ArrayList<Article> findMyArticles(String articleNames) {
		String[] namesWithNop = articleNames.split(":");

		this.articles = new ArrayList<Article>();
		ArrayList<Article> articles = new ArrayList<Article>();

		for (String name : namesWithNop) {
			String[] nameAndNop;

			nameAndNop = name.split(",");

			for (Article article : Data.getInstance().getArticles()) {
				if (article.getName().equals(nameAndNop[0])) {
					article.setNop(nameAndNop[1]);
					articles.add(article);

				}
			}
		}

		return articles;
	}

	public ArrayList<Article> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}

	public int getBill() {
		return bill;
	}

	public void setBill(int bill) {
		this.bill = bill;
	}

	protected String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String format = formatter.format(date);
		return format;
	}
}
