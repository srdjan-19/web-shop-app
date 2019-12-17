articleModule.factory("articleFactory", function ($http) {

	var factory = {};

	factory.findAll = function () {
		return $http.get("/WebShopApp/api/articles")
	}

	factory.findTopDishes = function () {
		return $http.get("/WebShopApp/api/articles/popular/dishes")
	}

	factory.findTopDrinks = function () {
		return $http.get("/WebShopApp/api/articles/popular/drinks")
	}

	factory.create = function (article) {
		return $http.post("/WebShopApp/api/articles", article);
	}

	factory.update = function (update) {
		return $http.put("/WebShopApp/api/articles", update);
	}

	factory.remove = function (id) {
		return $http.delete("/WebShopApp/api/articles/" + id);
	}

	factory.orderArticle = function (article, quantity) {
		return $http.post("/WebShopApp/api/orders/articles/" + quantity, article);
	}

	factory.getCurrentNumberOfMyArticles = function () {
		return $http.get("/WebShopApp/api/articles/current-number")
	}

	factory.addToMenu = function (article, restarauntName) {
		return $http.post("/WebShopApp/api/restaraunts/" + restarauntName + "/articles", article);
	}

	return factory;

});