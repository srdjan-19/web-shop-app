package services;

import java.util.ArrayList;
import java.util.UUID;

import beans.data.Data;
import beans.entities.Article;

public class ArticleServices {
	
	public ArrayList<Article> getArticles() {
		return Data.getInstance().getArticles();
	}

	public int getCurrentNumberOfArticles() {
		return Data.getInstance().getPrepareOrder().size();
	}

	public boolean create(Article article, String path) {
		for (Article current : Data.getInstance().getArticles()) {
			if (current.getName().equals(article.getName()))
				return false;
		}
		
		article.setId(UUID.randomUUID());
		article.setPopularity(0);
		Data.getInstance().getArticles().add(article);
		Data.getInstance().writeArticles(path);

		return true;
	}

	public boolean update(Article update, String path) {
		for (int i = 0; i < Data.getInstance().getArticles().size(); i++) {
			if (Data.getInstance().getArticles().get(i).getName().equals(update.getName()) &&
					!Data.getInstance().getArticles().get(i).getId().toString().equals(update.getId().toString())) {
					
					return false;
				}
			
			if (Data.getInstance().getArticles().get(i).getId().toString().equals(update.getId().toString())) {

				if (!(update.getName() == null || update.getName().equals("")))
					Data.getInstance().getArticles().get(i).setName(update.getName());

				if (!(update.getPrice() == 0))
					Data.getInstance().getArticles().get(i).setPrice(update.getPrice());

				if (!(update.getDescription() == null || update.getDescription().equals("")))
					Data.getInstance().getArticles().get(i).setDescription(update.getDescription());

				Data.getInstance().writeArticles(path);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean remove(String id, String path) {
		for (int i = 0; i < Data.getInstance().getArticles().size(); i++) {
			if (Data.getInstance().getArticles().get(i).getId().toString().equals(id)) {
				Data.getInstance().getArticles().remove(i);

				for (int j = 0; j < Data.getInstance().getRestaraunts().size(); j++) {
					for (int k = 0; k < Data.getInstance().getRestaraunts().get(j).getDishes().size(); k++) {
						if (Data.getInstance().getRestaraunts().get(j).getDishes().get(k).getId().toString().equals(id)) {
							Data.getInstance().getRestaraunts().get(j).getDishes().remove(k);
						}
					}

					for (int l = 0; l < Data.getInstance().getRestaraunts().get(j).getDrinks().size(); l++) {
						if (Data.getInstance().getRestaraunts().get(j).getDrinks().get(l).getId().toString().equals(id)) {
							Data.getInstance().getRestaraunts().get(j).getDrinks().remove(l);
						}
					}
				}
				
				Data.getInstance().writeArticles(path);
				
				return true;
			}
		}
		
		return false;
	}

	public ArrayList<Article> getMostPopularDishes() {
		ArrayList<Article> dishes = new ArrayList<Article>();

		for (int i = 0; i < Data.getInstance().getArticles().size(); i++) {
			if (Data.getInstance().getArticles().get(i).getUnit().equals("g")) {
				dishes.add(Data.getInstance().getArticles().get(i));
			}
		}

		dishes.sort((dish1, dish2) -> dish2.getPopularity().compareTo(dish1.getPopularity()));

		if (dishes.size() > 10) {
			ArrayList<Article> top10 = new ArrayList<Article>(dishes.subList(0, 9));
			return top10;
		}
		
		return dishes;
	}

	public ArrayList<Article> getMostPopularDrinks() {
		ArrayList<Article> drinks = new ArrayList<Article>();

		for (int i = 0; i < Data.getInstance().getArticles().size(); i++) {
			if (Data.getInstance().getArticles().get(i).getUnit().equals("ml")) {
				drinks.add(Data.getInstance().getArticles().get(i));
			}
		}

		drinks.sort((drink1, drink2) -> drink2.getPopularity().compareTo(drink1.getPopularity()));

		if (drinks.size() > 10) {
			ArrayList<Article> top10 = new ArrayList<Article>(drinks.subList(0, 9));
			return top10;
		}
		
		return drinks;
	}
}
