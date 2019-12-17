restarauntModule.factory("restarauntFactory", function ($http) {

	var factory = {};

	factory.findAll = function () {
		return $http.get("/WebShopApp/api/restaraunts")
	}

	factory.create = function (restaraunt) {
		return $http.post("/WebShopApp/api/restaraunts", restaraunt);
	}

	factory.update = function (update) {
		return $http.put("/WebShopApp/api/restaraunts", update);
	}

	factory.remove = function (id) {
		return $http.delete("/WebShopApp/api/restaraunts/" + id);
	}

	factory.addToFavourites = function (restaraunt) {
		return $http.post("/WebShopApp/api/users/restaraunts/favourites", restaraunt);
	}

	return factory;

});