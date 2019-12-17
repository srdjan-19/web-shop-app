var userModule = angular.module("userMdl", []);
var restarauntModule = angular.module("restarauntMdl", []);
var vehicleModule = angular.module("vehicleMdl", []);
var articleModule = angular.module("articleMdl", []);
var orderModule = angular.module("orderMdl", []);

var app = angular.module("WebShopApp", ["ngRoute", "userMdl", "restarauntMdl", "vehicleMdl", "articleMdl", "orderMdl"]);

app.config(['$locationProvider', function ($locationProvider, $scope) {
	$locationProvider.hashPrefix('');
}]);

app.config(function ($routeProvider) {
	$routeProvider
		.when("/", {
			templateUrl: "pages/homepage.html"
		})
		.when("/signup", {
			templateUrl: "pages/signup.html"
		})
		.when("/signin", {
			templateUrl: "pages/signin.html"
		})
		.when("/restaraunts", {
			templateUrl: "pages/restaraunts.html"
		})
		.when("/vehicles", {
			templateUrl: "pages/vehicles.html"
		})
		.when("/users", {
			templateUrl: "pages/users.html"
		})
		.when("/articles", {
			templateUrl: "pages/articles.html"
		})
		.when("/orders", {
			templateUrl: "pages/orders.html"
		})
		.when("/profile", {
			templateUrl: "pages/account.html"
		})
		.otherwise({
			templateUrl: "pages/page-not-found.html"
		});
});