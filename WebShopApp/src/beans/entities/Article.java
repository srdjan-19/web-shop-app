package beans.entities;

import java.io.Serializable;
import java.util.UUID;

public class Article implements Serializable {

	private static final long serialVersionUID = -1381119065426439901L;

	private UUID id;
	private String name;
	private int price;
	private String description;
	private int quantity;
	private String unit;
	private String nop;
	private Integer popularity;

	public Article() {

	}

	public Article(String id, String n, int p, String d, int q, String u, int pop) {
		this.id = UUID.fromString(id);
		this.name = n;
		this.price = p;
		this.description = d;
		this.quantity = q;
		this.unit = u;
		this.nop = "0";
		this.popularity = pop;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getNop() {
		return nop;
	}

	public void setNop(String nop) {
		this.nop = nop;
	}

	public Integer getPopularity() {
		return popularity;
	}

	public void increasePopularity() {
		this.popularity++;
	}

	public void setPopularity(int pop) {
		this.popularity = pop;
	}

}
