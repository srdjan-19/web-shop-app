vehicleModule.factory("vehicleFactory", function ($http) {

	var factory = {};

	factory.findAll = function () {
		return $http.get("/WebShopApp/api/vehicles")
	}

	factory.create = function (vehicle) {
		return $http.post("/WebShopApp/api/vehicles", vehicle);
	}

	factory.update = function (update) {
		return $http.put("/WebShopApp/api/vehicles", update);
	}

	factory.remove = function (id) {
		return $http.delete("/WebShopApp/api/vehicles/" + id);
	}

	factory.assign = function (delivererUsername, vehicle) {
		return $http.post("/WebShopApp/api/vehicles/assign/" + delivererUsername, vehicle);
	}

	factory.take = function (vehicle) {
		return $http.post("/WebShopApp/api/vehicles/take", vehicle);
	}

	return factory;

});