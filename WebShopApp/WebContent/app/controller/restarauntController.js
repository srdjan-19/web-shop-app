restarauntModule.controller("restarauntCtrl", function ($scope, $rootScope, $location, restarauntFactory) {
	
	restarauntFactory.findAll()
		.then(function (response) {
			$scope.restaraunts = response.data;
		});

	$scope.categories = ["GRILL", "DOMESTIC", "CHINESE", "INDIAN", "CANDY_SHOP", "PIZZERIA"];

	$scope.createRestaraunt = function (restaraunt) {
		restarauntFactory.create(restaraunt)
			.then(function (response) {
				$scope.restaraunts.push(restaraunt);
				$('#popCreate').modal('hide');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	};

	var outoufDate;
	$scope.prepareRestaraunt = function (restaraunt) {
		outoufDate = restaraunt;
		$scope.rupdate = restaraunt;
	};

	$scope.updateRestaraunt = function (update) {
		restarauntFactory.update(update)
			.then(function (response) {
				$scope.restaraunts = response.data;
				$('#popUpdate').modal('hide');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	}

	$scope.deleteRestaraunt = function (restaraunt) {
		restarauntFactory.remove(restaraunt.id)
			.then(function (response) {
				$scope.restaraunts = response.data;
				$location.path("/restaraunts");
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	};

	$scope.addAsFavourite = function (restaraunt) {
		restarauntFactory.addToFavourites(restaraunt)
			.then(function (response) {
				$scope.successMessage = response.data.message;
				$('#successMessage').modal('show');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	};
});