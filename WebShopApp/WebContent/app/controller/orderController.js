orderModule.controller("orderCtrl", function ($scope, $rootScope, $location, orderFactory, userFactory, articleFactory) {

	$scope.toDelivery = null;

	if ($rootScope.adminOnly === true) {

		articleFactory.getCurrentNumberOfMyArticles()
			.then(function (response) {
				$rootScope.currentNumberOfArticles = response.data;
			});

		userFactory.findDeliverers()
			.then(function (response) {
				$scope.deliverers = response.data;
			});

		userFactory.findBuyers()
			.then(function (response) {
				$scope.buyers = response.data;
			});

		orderFactory.findOrderedArticles()
			.then(function (response) {
				$scope.articles = response.data;
			});

		orderFactory.findAll()
			.then(function (response) {
				$scope.orders = response.data;
			});

		$scope.completeOrder = function (order, buyerUsername, delivererUsername) {
			orderFactory.complete(order, buyerUsername, delivererUsername)
				.then(function (response) {
					$('#popCompleteOrder').modal('hide');
					$scope.orders.push(response.data);
					$scope.articles = null;
					$scope.note = "";
					$scope.successMessage = "You successfully made an order! Bill: " + response.data.bill;
					$('#successMessage').modal('show');
					$rootScope.currentNumberOfArticles = 0;
				}, function (response) {
					data = response.data;
					$scope.errorMessage = data.message;
					$('#errorMessage').modal('show');
				});
		}

		$scope.deleteOrder = function (order) {
			orderFactory.remove(order.id)
				.then(function (response) {
					$scope.orders = response.data;
					$scope.successMessageOrder = "You have been successfully remove an order!";
					$('#successMessage').modal('show');
				}, function (response) {
					data = response.data;
					$scope.errorMessage = data.message;
					$('#errorMessage').modal('show');
				});
		}

		$scope.prepareUpdate = function (order) {
			$scope.oupdate = order;
		};

		$scope.updateOrder = function (oupdate, buyer, deliverer) {
			orderFactory.update(oupdate, buyer.username, deliverer.username)
				.then(function (response) {
					$('#popUpdate').modal('hide');
				}, function (response) {
					data = response.data;
					$scope.orders = response.data;
					$scope.errorMessage = data.message;
					$('#errorMessage').modal('show');
				});
		}
	}

	if ($rootScope.buyerOnly === true) {

		$scope.findOrderedArticles = function () {
			orderFactory.findOrderedArticles()
				.then(function (response) {
					$scope.articles = response.data;
				}, function (response) {
					data = response.data;
					$scope.errorMessage = data.message;
					$('#errorMessage').modal('show');
				});
		}

		articleFactory.getCurrentNumberOfMyArticles()
			.then(function (response) {
				$rootScope.currentNumberOfArticles = response.data;
			}, function (response) {
				$location.url("/articles")
			});

		$scope.completeOrder = function (order, buyerUN, delivererUN) {
			orderFactory.complete(order, buyerUN, delivererUN).then(function (response) {
				$('#popCompleteOrder').modal('hide');
				$scope.articles = null;
				$scope.note = "";
				$scope.successMessage = "You successfully made an order! Bill: " + response.data.bill;
				$('#successMessage').modal('show');
				$rootScope.currentNumberOfArticles = 0;
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
		}
	}

	if ($rootScope.delivererOnly === true) {

		orderFactory.findMyDelivery()
			.then(function (response) {
				if (response.data === "")
					$scope.toDelivery = null;
				else
					$scope.toDelivery = response.data;
			}, function (response) {
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});

		orderFactory.findAll()
			.then(function (response) {
				$scope.orders = response.data;
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});


		orderFactory.findUndelivered()
			.then(function (response) {
				$scope.undelivered = response.data;
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});

		$scope.takeOrder = function (order) {
			orderFactory.take(order)
				.then(function (response) {
					$scope.orders = response.data;
					$scope.toDelivery = order;
					$scope.successMessage = "You have successfully took an order!";
					$('#successMessage').modal('show');
				}, function (response) {
					$scope.errorMessage = response.data.message;
					$('#errorMessage').modal('show');
				});
		}

		$scope.deliveryDone = function () {
			orderFactory.deliveryDone()
				.then(function (response) {
					$scope.toDelivery = null;
					$scope.orders = response.data;
					$scope.successMessage = "Delivery done!";
					$('#successMessage').modal('show');
				}, function (response) {
					data = response.data;
					$scope.errorMessage = data.message;
					$('#errorMessage').modal('show');
				});
		}
	}
});