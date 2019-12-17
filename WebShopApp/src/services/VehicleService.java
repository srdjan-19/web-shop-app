package services;

import java.util.ArrayList;
import java.util.UUID;

import beans.data.Data;
import beans.entities.Deliverer;
import beans.entities.Vehicle;
import beans.enums.Usage;

public class VehicleService {

	public ArrayList<Vehicle> getVehicles() {
		return Data.getInstance().getVehicles();
	}

	public Vehicle create(Vehicle vehicle, String path) {
		for (Vehicle current : Data.getInstance().getVehicles()) {
			if (current.getRegistrationNumber().equals(vehicle.getRegistrationNumber()))
				return null;
		}

		vehicle.setId(UUID.randomUUID());
		vehicle.setUsage(Usage.AVAILABLE);
		
		Data.getInstance().getVehicles().add(vehicle);
		Data.getInstance().writeVehicle(path);

		return vehicle;
	}

	public boolean update(Vehicle update, String path) {
		if (update == null)
			return false;

		for (int i = 0; i < Data.getInstance().getVehicles().size(); i++) {
			if (Data.getInstance().getVehicles().get(i).getRegistrationNumber().equals(update.getRegistrationNumber()) &&
				!Data.getInstance().getVehicles().get(i).getId().toString().equals(update.getId().toString())) {
					
				return false;
			}

			if (Data.getInstance().getVehicles().get(i).getId().toString().equals(update.getId().toString())) {

				if (!(update.getBrand() == null || update.getBrand().equals("")))
					Data.getInstance().getVehicles().get(i).setBrand(update.getBrand());

				if (!(update.getModel() == null || update.getModel().equals("")))
					Data.getInstance().getVehicles().get(i).setModel(update.getModel());

				if (!(update.getManufactured() == null || update.getManufactured().equals("")))
					Data.getInstance().getVehicles().get(i).setManufactured(update.getManufactured());

				if (!(update.getType() == null))
					Data.getInstance().getVehicles().get(i).setType(update.getType());

				if (!(update.getRegistrationNumber() == null || update.getRegistrationNumber().equals("")))
					Data.getInstance().getVehicles().get(i).setRegistrationNumber(update.getRegistrationNumber());

				if (!(update.getNote() == null || update.getNote().equals("")))
					Data.getInstance().getVehicles().get(i).setNote(update.getNote());

				if (!(update.getUsage() == null || update.getUsage().equals(null))) {
					Data.getInstance().getVehicles().get(i).setUsage(update.getUsage());

					for (Deliverer d : Data.getInstance().getDeliverers()) {
						if (d.getVehicle().getRegistrationNumber() != null)
							if (d.getVehicle().getRegistrationNumber().equals(Data.getInstance().getVehicles().get(i).getRegistrationNumber())) {
								d.setVehicle(new Vehicle());
							}
					}
				}
			}
		}

		Data.getInstance().writeDeliverer(path);
		Data.getInstance().writeVehicle(path);

		return true;
	}

	public boolean remove(String id, String path) {
		for (int i = 0; i < Data.getInstance().getVehicles().size(); i++) {
			if (Data.getInstance().getVehicles().get(i).getId().toString().equals(id)) {

				for (Deliverer d : Data.getInstance().getDeliverers()) {
					if (d.getVehicle().getRegistrationNumber() != null)
						if (d.getVehicle().getRegistrationNumber().equals(Data.getInstance().getVehicles().get(i).getRegistrationNumber())) {
							d.setVehicle(new Vehicle());
						}
				}

				Data.getInstance().getVehicles().remove(i);

				Data.getInstance().writeDeliverer(path);
				Data.getInstance().writeVehicle(path);
				
				return true;
			}
		}
		return false;
	}

	public boolean assign(String delivererUsername, Vehicle vehicle, String path) {
		for (int i = 0; i < Data.getInstance().getDeliverers().size(); i++) {
			if (Data.getInstance().getDeliverers().get(i).getUsername().equals(delivererUsername)
					&& Data.getInstance().getDeliverers().get(i).getVehicle().getRegistrationNumber() == null) {

				for (int j = 0; j < Data.getInstance().getVehicles().size(); j++) {
					if (Data.getInstance().getVehicles().get(j).getRegistrationNumber().equals(vehicle.getRegistrationNumber())
							&& Data.getInstance().getVehicles().get(j).getUsage().equals(Usage.AVAILABLE)) {

						Data.getInstance().getDeliverers().get(i).setVehicle(vehicle);
						Data.getInstance().getVehicles().get(j).setUsage(Usage.NOT_AVAILABLE);

						Data.getInstance().writeDeliverer(path);
						Data.getInstance().writeVehicle(path);

						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean take(Vehicle vehicle, Deliverer deliverer, String path) {
		for (int i = 0; i < Data.getInstance().getDeliverers().size(); i++) {
			if (Data.getInstance().getDeliverers().get(i).getUsername().equals(deliverer.getUsername())) {
				for (int j = 0; j < Data.getInstance().getVehicles().size(); j++) {
					if (Data.getInstance().getVehicles().get(j).getRegistrationNumber().equals(vehicle.getRegistrationNumber())
							&& Data.getInstance().getVehicles().get(j).getUsage().equals(Usage.AVAILABLE)) {
						if (Data.getInstance().getDeliverers().get(i).getVehicle().getBrand() != null) {
							for (Vehicle v : Data.getInstance().getVehicles()) {
								if (Data.getInstance().getDeliverers().get(i).getVehicle().getRegistrationNumber().equals(v.getRegistrationNumber())) {
									v.setUsage(Usage.AVAILABLE);
								}
							}
						}

						Data.getInstance().getDeliverers().get(i).setVehicle(vehicle);
						Data.getInstance().getVehicles().get(j).setUsage(Usage.NOT_AVAILABLE);

						Data.getInstance().writeDeliverer(path);
						Data.getInstance().writeVehicle(path);

						return true;
					}
				}
			}
		}
		return false;
	}
}
