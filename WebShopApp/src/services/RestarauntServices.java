package services;

import java.util.ArrayList;

import beans.data.Data;
import beans.entities.Article;
import beans.entities.Restaraunt;

public class RestarauntServices {

	public ArrayList<Restaraunt> getRestaraunts() {
		return Data.getInstance().getRestaraunts();
	}

	public boolean create(Restaraunt newRestaraunt, String path) {
		for (Restaraunt current : Data.getInstance().getRestaraunts()) {
			if (current.getName().equals(newRestaraunt.getName())
					|| current.getAddress().equals(newRestaraunt.getAddress()))
				return false;
		}

		Data.getInstance().getRestaraunts().add(newRestaraunt);
		Data.getInstance().writeRestaraunts(path);

		return true;
	}

	public boolean update(Restaraunt update, String path) {
		if (update == null) {
			return false;
		}

		for (int i = 0; i < Data.getInstance().getRestaraunts().size(); i++) {
			
		}

		for (int i = 0; i < Data.getInstance().getRestaraunts().size(); i++) {
			if ((Data.getInstance().getRestaraunts().get(i).getName().equals(update.getName()) || 
				 Data.getInstance().getRestaraunts().get(i).getAddress().equals(update.getAddress())) && 
				!Data.getInstance().getRestaraunts().get(i).getId().toString().equals(update.getId().toString())) {
					
					return false;
				}
			
			if (Data.getInstance().getRestaraunts().get(i).getId().toString().equals(update.getId().toString())) {

				if (!(update.getName() == null || update.getName().equals("")))
					Data.getInstance().getRestaraunts().get(i).setName(update.getName());

				if (!(update.getAddress() == null || update.getAddress().equals("")))
					Data.getInstance().getRestaraunts().get(i).setAddress(update.getAddress());

				if (!(update.getCategory() == null))
					Data.getInstance().getRestaraunts().get(i).setCategory(update.getCategory());
			}
		}

		Data.getInstance().writeRestaraunts(path);

		return true;
	}

	public boolean remove(String id, String path) {
		for (int i = 0; i < Data.getInstance().getRestaraunts().size(); i++) {
			if (Data.getInstance().getRestaraunts().get(i).getId().toString().equals(id)) {

				for (int j = 0; j < Data.getInstance().getBuyers().size(); j++) {
					for (int k = 0; k < Data.getInstance().getBuyers().get(j).getFavRestaraunts().size(); k++) {

						if (Data.getInstance().getBuyers().get(j).getFavRestaraunts().get(k).getName().equals(Data.getInstance().getRestaraunts().get(i).getName())) {
							Data.getInstance().getBuyers().get(j).getFavRestaraunts().remove(k);
						}
					}
				}
				
				Data.getInstance().getRestaraunts().remove(i);
				
				Data.getInstance().writeRestaraunts(path);
				Data.getInstance().writeBuyers(path);
				
				return true;
			}
		}
		return false;
	}

	public Restaraunt addArticle(String id, Article article, String path) {
		for (Restaraunt currentR : Data.getInstance().getRestaraunts()) {
			if (currentR.getId().toString().equals(id)) {
				if (!currentR.containsDish(article.getName()) && article.getUnit().equals("g")) {
					currentR.addDish(article);
					Data.getInstance().writeRestaraunts(path);
					return currentR;
				} else if (!currentR.containsDrink(article.getName()) && article.getUnit().equals("ml")) {
					currentR.addDrink(article);
					Data.getInstance().writeRestaraunts(path);
					return currentR;
				}
			}
		}
		return null;
	}
}
