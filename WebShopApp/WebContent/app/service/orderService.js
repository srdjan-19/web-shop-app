orderModule.factory("orderFactory", function ($http) {

	var factory = {};

	factory.findAll = function () {
		return $http.get("/WebShopApp/api/orders")
	}

	factory.findOrderedArticles = function () {
		return $http.get("/WebShopApp/api/orders/articles")
	}

	factory.findUndelivered = function () {
		return $http.get("/WebShopApp/api/orders/undelivered")
	}

	factory.complete = function (order, buyerUsername, delivererUsername) {
		return $http.post("/WebShopApp/api/orders/complete/" + buyerUsername + "/" + delivererUsername, order)
	}

	factory.take = function (order) {
		return $http.post("/WebShopApp/api/orders/take", order)
	}

	factory.findMyDelivery = function () {
		return $http.get("/WebShopApp/api/orders/delivery")
	}

	factory.deliveryDone = function () {
		return $http.post("/WebShopApp/api/orders/done")
	}

	factory.remove = function (id) {
		return $http.delete("/WebShopApp/api/orders/" + id)
	}

	factory.update = function (update, buyerUsername, delivererUsername) {
		return $http.put("/WebShopApp/api/orders/" + buyerUsername + "/" + delivererUsername, update)
	}

	return factory;

});