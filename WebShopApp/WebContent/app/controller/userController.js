userModule.controller("userCtrl", function ($scope, $rootScope, $location, $window, userFactory) {

	$scope.findAllUsers = function () {
		userFactory.findAll()
			.then(function (response) {
				$scope.users = response.data;
			})
	}

	$scope.signup = function (user) {
		userFactory.signUp(user)
			.then(function (response) {
				$location.path("/signin");
			}, function (response) {
				$scope.errorMessage = response.data.message;
				$('#errorMessage').modal('show');
			});
	};

	$scope.signin = function (login) {
		userFactory.signIn(login)
			.then(function (response) {
				var role = response.data.role;

				$rootScope.userSignedIn = true;

				if (role === 'ADMIN') {
					$rootScope.adminOnly = true;

					userFactory.getCurrentNumberOfMyArticles()
						.then(function (response) {
							$scope.currentNumberOfArticles = response.data;
						});
				} else if (role === 'BUYER') {
					$rootScope.buyerOnly = true;

					userFactory.getCurrentNumberOfMyArticles()
						.then(function (response) {
							$rootScope.currentNumberOfArticles = response.data;
						});
				} else if (role == 'DELIVERER')
					$rootScope.delivererOnly = true;

				$location.path("/");

			}, function (response) {
				$rootScope.errorMessage = response.data.message;
				$('#errorMessage').modal('show');
			});
	};

	$scope.signout = function () {
		userFactory.signOut().then(function (response) {
			$rootScope.userSignedIn = false;
			$rootScope.delivererOnly = false;
			$rootScope.buyerOnly = false;
			$rootScope.adminOnly = false;

			$location.path("/");
		});
	};

	$scope.promote = function (user, promoteRole) {
		userFactory.promote(user, promoteRole)
			.then(function (response) {
				$scope.users = response.data;
			}, function (response) {
				$scope.errorMessage = response.data.message;
				$('#errorMessage').modal('show');
			});
	};

	if ($rootScope.buyerOnly === true) {
		userFactory.getMyOrders()
			.then(function (response) {
				$scope.myOrders = response.data;
			});

		userFactory.getMyFavourites()
			.then(function (response) {
				$scope.myFavourites = response.data;
			});
	}
});