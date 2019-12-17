package beans.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import beans.entities.Administrator;
import beans.entities.Article;
import beans.entities.Buyer;
import beans.entities.Deliverer;
import beans.entities.Order;
import beans.entities.Restaraunt;
import beans.entities.User;
import beans.entities.Vehicle;

public class Data {

	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Administrator> admins = new ArrayList<Administrator>();
	private ArrayList<Buyer> buyers = new ArrayList<Buyer>();
	private ArrayList<Deliverer> deliverers = new ArrayList<Deliverer>();

	private ArrayList<Restaraunt> restaraunts = new ArrayList<Restaraunt>();
	private ArrayList<Article> articles = new ArrayList<Article>();
	private ArrayList<Order> orders = new ArrayList<Order>();
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	private ArrayList<Article> prepareOrder = new ArrayList<Article>();

	private static Data instance;

	public Data() {

	}

	public static Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}

	public void readBuyers(String path) {
		BufferedReader in = null;

		this.getBuyers().clear();

		try {
			File file = new File(path + "Data/Buyers.txt");
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			readBuyer(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readBuyer(BufferedReader in) {
		String line, username = "", password = "", firstName = "", lastName = "", mobilePhone = "", email = "",
				registered = "", restorani = "", orders = "";
		StringTokenizer st;

		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					username = st.nextToken().trim();
					password = st.nextToken().trim();
					firstName = st.nextToken().trim();
					lastName = st.nextToken().trim();
					mobilePhone = st.nextToken().trim();
					email = st.nextToken().trim();
					registered = st.nextToken().trim();

					try {
						restorani = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						restorani = "";
					}
				}

				Buyer buyer = new Buyer(username, password, firstName, lastName, mobilePhone, email, registered, orders,
						restorani);
				this.getBuyers().add(buyer);
				this.getUsers().add(buyer);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeBuyers(String path) {
		BufferedWriter out = null;

		try {

			File file = new File(path + "Data/Buyers.txt");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			String upis = "";

			for (Buyer buyer : this.getBuyers()) {

				String favorites = "";

				for (Restaraunt favorite : buyer.getFavRestaraunts()) {
					favorites += favorite.getName() + ":";

				}

				// for (Order order : buyer.getOrders()) {
				// orders += order.getDateOfOrder() + ",";
				// }

				upis = buyer.getUsername() + ";" + buyer.getPassword() + ";" + buyer.getFirstName() + ";"
						+ buyer.getLastName() + ";" + buyer.getPhone() + ";" + buyer.getEmail() + ";"
						+ buyer.getRegistered() + ";" + /* orders + ";" + */ favorites + "\n";
				try {
					out.write(upis);
					out.flush(); // da mogu da nastavim sa upisom
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							// out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readAdmins(String path) {
		BufferedReader in = null;

		this.getAdmins().clear();
		this.getUsers().clear();

		try {
			File file = new File(path + "Data/Admins.txt");
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			readAdmin(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readAdmin(BufferedReader in) {
		String line, userName = "", password = "", firstName = "", lastName = "", mobilePhone = "", email = "",
				registered = "";
		StringTokenizer st;

		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					userName = st.nextToken().trim();
					password = st.nextToken().trim();
					firstName = st.nextToken().trim();
					lastName = st.nextToken().trim();
					mobilePhone = st.nextToken().trim();
					email = st.nextToken().trim();
					registered = st.nextToken().trim();
				}

				Administrator admin = new Administrator(userName, password, firstName, lastName, mobilePhone, email,
						registered);
				this.getAdmins().add(admin);
				this.getUsers().add(admin);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeAdmins(String path) {
		BufferedWriter out = null;

		try {

			File file = new File(path + "Data/Admins.txt");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			String upis = "";

			for (Administrator admin : this.getAdmins()) {
				upis = admin.getUsername() + ";" + admin.getPassword() + ";" + admin.getFirstName() + ";"
						+ admin.getLastName() + ";" + admin.getPhone() + ";" + admin.getEmail() + ";"
						+ admin.getRegistered() + ";" + "\n";
				try {
					out.write(upis);
					out.flush(); // da mogu da nastavim sa upisom
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							// out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readDeliverers(String path) {
		BufferedReader in = null;

		this.getDeliverers().clear();

		try {
			File file = new File(path + "Data/Deliverers.txt");
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			readDeliverer(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readDeliverer(BufferedReader in) {
		String line, userName = "", password = "", firstName = "", lastName = "", mobilePhone = "", email = "",
				registered = "", vehicleRN = "";
		StringTokenizer st;

		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					userName = st.nextToken().trim();
					password = st.nextToken().trim();
					firstName = st.nextToken().trim();
					lastName = st.nextToken().trim();
					mobilePhone = st.nextToken().trim();
					email = st.nextToken().trim();
					registered = st.nextToken().trim();

					try {
						vehicleRN = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						// TODO: handle exception
						vehicleRN = "";
					}

				}

				Deliverer deliverer = new Deliverer(userName, password, firstName, lastName, mobilePhone, email,
						registered, vehicleRN);

				this.getDeliverers().add(deliverer);
				this.getUsers().add(deliverer);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeDeliverer(String path) {
		BufferedWriter out = null;

		try {

			File file = new File(path + "Data/Deliverers.txt");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			String upis = "";

			for (Deliverer deliverer : this.getDeliverers()) {

				String reg = "";
				try {
					reg = deliverer.getVehicle().getRegistrationNumber();
				} catch (Exception e) {
					reg = "";
				}

				upis = deliverer.getUsername() + ";" + deliverer.getPassword() + ";" + deliverer.getFirstName() + ";"
						+ deliverer.getLastName() + ";" + deliverer.getPhone() + ";" + deliverer.getEmail() + ";"
						+ deliverer.getRegistered() + ";" + reg + ";" + "\n";
				try {
					out.write(upis);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							// out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readRestaraunts(String path) {
		BufferedReader in = null;

		this.getRestaraunts().clear();

		try {
			File file = new File(path + "Data/Restaraunts.txt");
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			readRestaraunt(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readRestaraunt(BufferedReader in) {
		String line, id = "", name = "", address = "", category = "", dishes = "", drinks = "";
		StringTokenizer st;

		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");

				while (st.hasMoreTokens()) {
					id = st.nextToken().trim().substring(1);
					name = st.nextToken().trim();
					address = st.nextToken().trim();
					category = st.nextToken().trim();

					try {
						dishes = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						dishes = "";
					}

					try {
						drinks = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						drinks = "";
					}

				}

				Restaraunt restaraunt = new Restaraunt(id, name, address, category, dishes, drinks);

				this.getRestaraunts().add(restaraunt);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeRestaraunts(String path) {
		BufferedWriter out = null;

		try {
			File file = new File(path + "Data/Restaraunts.txt");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			String upis = "";

			for (Restaraunt restaraunt : this.getRestaraunts()) {

				String dishes = "";
				String drinks = "";

				for (Article dish : restaraunt.getDishes()) {
					dishes += dish.getName() + "-";
				}

				for (Article drink : restaraunt.getDrinks()) {
					drinks += drink.getName() + ",";
				}

				upis = restaraunt.getId().toString() + ";" + restaraunt.getName() + ";" + restaraunt.getAddress() + ";"
						+ restaraunt.getCategory() + ";" + dishes + ";" + drinks + ";" + "\n";
				try {
					out.write(upis);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							// out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readArticles(String path) {
		BufferedReader in = null;

		this.getArticles().clear();

		try {
			File file = new File(path + "Data/Articles.txt");
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			readArticle(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readArticle(BufferedReader in) {
		String line, id = "", name = "", price = "", description = "", quantity = "", unit = "", popularity = "";
		StringTokenizer st;

		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim().substring(1);
					name = st.nextToken().trim();
					price = st.nextToken().trim();
					description = st.nextToken().trim();
					quantity = st.nextToken().trim();
					unit = st.nextToken().trim();
					try {
						popularity = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						popularity = "";
					}
				}

				Article article = new Article(id, name, Integer.parseInt(price), description,
						Integer.parseInt(quantity), unit, Integer.parseInt(popularity));
				this.getArticles().add(article);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeArticles(String path) {
		BufferedWriter out = null;

		try {
			File file = new File(path + "Data/Articles.txt");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			String upis = "";

			for (Article article : this.getArticles()) {
				upis = article.getId().toString() + ";" + article.getName() + ";" + article.getPrice() + ";"
						+ article.getDescription() + ";" + article.getQuantity() + ";" + article.getUnit() + ";"
						+ article.getPopularity() + ";" + "\n";
				try {
					out.write(upis);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							// out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readVehicles(String path) {
		BufferedReader in = null;

		this.getVehicles().clear();

		try {
			File file = new File(path + "Data/Vehicles.txt");
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			readVehicle(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readVehicle(BufferedReader in) {
		String line, id = "", brand = "", model = "", type = "", regN = "", manufactured = "", usage = "", note = "";
		StringTokenizer st;

		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim().substring(1);
					brand = st.nextToken().trim();
					model = st.nextToken().trim();
					type = st.nextToken().trim();
					regN = st.nextToken().trim();
					manufactured = st.nextToken().trim();
					usage = st.nextToken().trim();
					note = st.nextToken().trim();
				}

				Vehicle vehicle = new Vehicle(id, brand, model, type, regN, manufactured, usage, note);
				this.getVehicles().add(vehicle);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeVehicle(String path) {
		BufferedWriter out = null;

		try {

			File file = new File(path + "Data/Vehicles.txt");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			String upis = "";

			for (Vehicle vehicle : this.getVehicles()) {
				upis = vehicle.getId().toString() + ";" + vehicle.getBrand() + ";" + vehicle.getModel() + ";"
						+ vehicle.getType() + ";" + vehicle.getRegistrationNumber() + ";" + vehicle.getManufactured()
						+ ";" + vehicle.getUsage() + ";" + vehicle.getNote() + ";" + "\n";
				try {
					out.write(upis);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							// out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readOrders(String path) {
		BufferedReader in = null;

		this.getOrders().clear();

		try {
			File file = new File(path + "Data/Orders.txt");
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
			readOrder(in);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void readOrder(BufferedReader in) {
		String line, id = "", articlesNameWithNop = "", dateOfOrder = "", buyer = "", deliverer = "", state = "",
				note = "";
		StringTokenizer st;

		try {
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0)
					continue;
				st = new StringTokenizer(line, ";");
				while (st.hasMoreTokens()) {
					id = st.nextToken().trim().substring(1);
					articlesNameWithNop = st.nextToken().trim();
					dateOfOrder = st.nextToken().trim();
					buyer = st.nextToken().trim();

					try {
						deliverer = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						deliverer = "";
					}

					try {
						state = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						state = "";
					}

					try {
						note = st.nextToken().trim();
					} catch (NoSuchElementException e) {
						note = "";
					}

				}

				Order order = new Order(id, articlesNameWithNop, dateOfOrder, buyer, deliverer, state, note);

				this.getOrders().add(order);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void writeOrders(String path) {
		BufferedWriter out = null;

		try {

			File file = new File(path + "Data/Orders.txt");
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			String upis = "";

			for (Order order : this.getOrders()) {

				String articlesNameWithNop = "";
				String delivererName = "";
				String buyerName = "";

				for (Article article : order.getArticle()) {
					articlesNameWithNop += article.getName() + "," + article.getNop() + ":";
				}

				try {
					delivererName = order.getDeliverer().getUsername();
				} catch (Exception e) {
					delivererName = "";
				}

				try {
					buyerName = order.getBuyer().getUsername();
				} catch (Exception e) {
					buyerName = "";
				}

				upis = order.getId().toString() + ";" + articlesNameWithNop + ";" + order.getDateOfOrder() + ";"
						+ buyerName + ";" + delivererName + ";" + order.getState() + ";" + order.getNote() + ";" + "\n";
				try {
					out.write(upis);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							// out.close();
						} catch (Exception e) {
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public ArrayList<Administrator> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<Administrator> admins) {
		this.admins = admins;
	}

	public ArrayList<Buyer> getBuyers() {
		return buyers;
	}

	public void setBuyers(ArrayList<Buyer> buyers) {
		this.buyers = buyers;
	}

	public ArrayList<Deliverer> getDeliverers() {
		return deliverers;
	}

	public void setDeliverers(ArrayList<Deliverer> deliverers) {
		this.deliverers = deliverers;
	}

	public ArrayList<Article> getArticles() {
		return articles;
	}

	public void setArticles(ArrayList<Article> articles) {
		this.articles = articles;
	}

	public ArrayList<Article> getPrepareOrder() {
		return prepareOrder;
	}

	public void setPrepareOrder(ArrayList<Article> articles) {
		this.prepareOrder = articles;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<Order> orders) {
		this.orders = orders;
	}

	public ArrayList<Restaraunt> getRestaraunts() {
		return restaraunts;
	}

	public void setRestaraunts(ArrayList<Restaraunt> restaraunts) {
		this.restaraunts = restaraunts;
	}

	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public static void setInstance(Data instance) {
		Data.instance = instance;
	}

}
