vehicleModule.controller("vehicleCtrl", function ($scope, $location, vehicleFactory, userFactory) {

	userFactory.findBuyers()
		.then(function (response) {
			$scope.buyers = response.data;
		});

	userFactory.findDeliverers()
		.then(function (response) {
			$scope.deliverers = response.data;
		});

	vehicleFactory.findAll().then(function (response) {
		$scope.vehicles = response.data;
	});

	$scope.createVehicle = function (vehicle) {
		vehicleFactory.create(vehicle)
			.then(function (response) {
				$scope.vehicles.push(response.data);
				$('#popCreate').modal('hide');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	};

	var outoufDate;
	$scope.prepareVehicle = function (vehicle) {
		outoufDate = vehicle;
		$scope.vupdate = vehicle;
	};

	$scope.updateVehicle = function () {
		vehicleFactory.update($scope.vupdate)
			.then(function (response) {
				$scope.vehicles = response.data;
				$('#popUpdate').modal('hide');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	}

	$scope.deleteVehicle = function (id) {
		vehicleFactory.remove(id)
			.then(function (response) {
				$scope.vehicles = response.data;
				$location.path("/vehicles");
			});
	};

	$scope.assignVehicle = function (delivererUsername, vehicle) {
		vehicleFactory.assign(delivererUsername, vehicle)
			.then(function (response) {
				$scope.vehicles = response.data;
				$scope.successMessage = "Vehicle have been assigned successfully!";
				$('#successMessage').modal('show');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	}

	$scope.takeVehicle = function (vehicle) {
		vehicleFactory.take(vehicle)
			.then(function (response) {
				$scope.vehicles = response.data;
				$scope.successMessage = "You are ready to rock!";
				$('#successMessage').modal('show');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	}
});