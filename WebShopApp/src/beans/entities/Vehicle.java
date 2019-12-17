package beans.entities;

import java.io.Serializable;
import java.util.UUID;

import beans.enums.Type;
import beans.enums.Usage;

public class Vehicle implements Serializable {

	private static final long serialVersionUID = -203936658704671321L;

	private UUID id;
	private String brand;
	private String model;
	private Type type;
	private String registrationNumber;
	private String manufactured;
	private Usage usage;
	private String note;

	public Vehicle() {
		this.id = UUID.randomUUID();
		this.usage = Usage.AVAILABLE;
	}

	public Vehicle(String id, String b, String p, String t, String r, String m, String u, String n) {
		this.id = UUID.fromString(id);
		this.brand = b;
		this.model = p;
		this.type = findMyType(t);
		this.registrationNumber = r;
		this.manufactured = m;
		this.usage = findMyUsage(u);
		this.note = n;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	private Usage findMyUsage(String u) {
		if (u.equals("AVAILABLE"))
			return Usage.AVAILABLE;
		else
			return Usage.NOT_AVAILABLE;
	}

	private Type findMyType(String t) {
		if (t.equals("CAR"))
			return Type.CAR;
		else if (t.equals("SCOOTER"))
			return Type.SCOOTER;
		else
			return Type.BICYCLE;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String regNum) {
		this.registrationNumber = regNum;
	}

	public String getManufactured() {
		return manufactured;
	}

	public void setManufactured(String yearOfProd) {
		this.manufactured = yearOfProd;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
