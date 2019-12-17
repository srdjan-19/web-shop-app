package beans.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import beans.data.Data;
import beans.enums.Category;

public class Restaraunt implements Serializable {

	private static final long serialVersionUID = -956866296304892047L;
	
	private UUID id;
	private String name;
	private String address;
	private Category category;
	private ArrayList<Article> dishes;
	private ArrayList<Article> drinks;

	public Restaraunt() {
		this.setId(UUID.randomUUID());
		this.dishes = new ArrayList<Article>();
		this.drinks = new ArrayList<Article>();
	}

	public Restaraunt(String id, String n, String a, String c, String di, String dr) {
		this.id = UUID.fromString(id);
		this.name = n;
		this.address = a;
		this.category = findMyCategory(c);
		this.dishes = findMyDishes(di);
		this.drinks = findMyDrinks(dr);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	private ArrayList<Article> findMyDrinks(String dr) {
		String[] names = dr.split(",");
		this.drinks = new ArrayList<Article>();

		ArrayList<Article> ret = new ArrayList<Article>();

		for (String drink : names) {
			for (Article article : Data.getInstance().getArticles()) {
				if (article.getName().equals(drink)) {
					ret.add(article);
				}
			}
		}
		return ret;
	}

	private ArrayList<Article> findMyDishes(String di) {
		String[] names = di.split("-");
		this.dishes = new ArrayList<Article>();

		ArrayList<Article> ret = new ArrayList<Article>();

		for (String dish : names) {
			for (Article article : Data.getInstance().getArticles()) {
				if (article.getName().equals(dish)) {
					ret.add(article);
				}
			}
		}

		return ret;
	}

	private Category findMyCategory(String c) {
		if (c.equals("DOMESTIC"))
			return Category.DOMESTIC;
		else if (c.equals("GRILL"))
			return Category.GRILL;
		else if (c.equals("CHINESE"))
			return Category.CHINESE;
		else if (c.equals("INDIAN"))
			return Category.INDIAN;
		else if (c.equals("CANDY_SHOP"))
			return Category.CANDY_SHOP;
		else
			return Category.PIZZERIA;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public ArrayList<Article> getDishes() {
		return dishes;
	}

	public void setDish(ArrayList<Article> dish) {
		this.dishes = dish;
	}

	public ArrayList<Article> getDrinks() {
		return drinks;
	}

	public void setDrinks(ArrayList<Article> drinks) {
		this.drinks = drinks;
	}

	public void addDish(Article article) {
		this.dishes.add(article);
	}

	public void addDrink(Article article) {
		this.drinks.add(article);
	}

	public boolean containsDrink(String articleName) {
		try {
			for (Article drink : this.drinks) {
				if (drink.getName().equals(articleName))
					return true;
			}
		} catch (NullPointerException e) {

		}

		return false;
	}

	public boolean containsDish(String articleName) {
		try {
			for (Article dish : this.dishes) {
				if (dish.getName().equals(articleName))
					return true;
			}
		} catch (NullPointerException e) {

		}

		return false;

	}

}
