userModule.factory("userFactory", function ($http) {

	var factory = {};

	factory.init = function () {
		return $http.get("/WebShopApp/api/init")
	}

	factory.findAll = function () {
		return $http.get("/WebShopApp/api/users")
	};

	factory.findBuyers = function () {
		return $http.get("/WebShopApp/api/users/buyers")
	}

	factory.findDeliverers = function () {
		return $http.get("/WebShopApp/api/users/deliverers")
	}

	factory.signUp = function (user) {
		return $http.post("/WebShopApp/api/users", user)
	}

	factory.signIn = function (log) {
		return $http.post("/WebShopApp/api/users/signin", log)
	}

	factory.getSignedUser = function () {
		return $http.get("/WebShopApp/api/users/signed")
	}

	factory.signOut = function () {
		return $http.post("/WebShopApp/api/users/signout")
	}

	factory.getCurrentNumberOfMyArticles = function () {
		return $http.get("/WebShopApp/api/users/articles/")
	}

	factory.promote = function (user, promoteRole) {
		return $http.post("/WebShopApp/api/users/promote/" + promoteRole, user.username);
	}

	factory.getMyOrders = function () {
		return $http.get("/WebShopApp/api/orders/user");
	}

	factory.getMyFavourites = function () {
		return $http.get("/WebShopApp/api/users/restaraunts/favourites");
	}

	return factory;

});