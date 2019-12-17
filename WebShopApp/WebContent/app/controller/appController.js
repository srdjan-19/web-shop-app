app.controller("appCtrl", function ($scope, $rootScope, userFactory) {
	userFactory.init()
		.then(function (response) {
			$scope.users = response.data;
		});

	userFactory.getSignedUser()
		.then(function (response) {
			var role = response.data.role;

			if (role === 'ADMIN') {
				$rootScope.userSignedIn = true;
				$rootScope.adminOnly = true;
				$rootScope.buyerOnly = false;
				$rootScope.delivererOnly = false;
			} else if (role === 'BUYER') {
				$rootScope.userSignedIn = true;
				$rootScope.buyerOnly = true;
				$rootScope.adminOnly = false;
				$rootScope.delivererOnly = false;
			} else if (role == 'DELIVERER') {
				$rootScope.userSignedIn = true;
				$rootScope.delivererOnly = true;
				$rootScope.buyerOnly = false;
				$rootScope.adminOnly = false;
			}
		})
});