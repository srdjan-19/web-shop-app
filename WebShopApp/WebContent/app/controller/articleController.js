articleModule.controller("articleCtrl", function ($scope, $rootScope, $location, articleFactory) {

	articleFactory.findAll()
		.then(function (response) {
			$scope.articles = response.data;
		});

	articleFactory.getCurrentNumberOfMyArticles()
		.then(function (response) {
			$rootScope.currentNumberOfArticles = response.data;
		});

	$scope.createArticle = function (article) {
		articleFactory.create(article)
			.then(function (response) {
				$scope.articles.push(article);
				$('#popCreate').modal('hide');
			}, function (response) {
				data = response.data;
				$scope.errorMessageArticle = data.message;
				$('#errorArticle').modal('show');
			});
	};

	var outOfDate;
	$scope.prepareArticle = function (article) {
		outOfDate = article;
		$scope.aupdate = article;
	};

	$scope.updateArticle = function (update) {
		articleFactory.update(update)
			.then(function (response) {
				$scope.articles = response.data;
				$scope.update.name = "";
				$scope.update.price = "";
				$scope.update.description = "";
				$('#popUpdate').modal('hide');
			}, function (response) {
				$scope.aupdate = outOfDate;
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	}

	$scope.deleteArticle = function (id) {
		articleFactory.remove(id)
			.then(function (response) {
				$scope.articles = response.data;
				$location.path("/articles");
			});
	};

	$scope.orderArticle = function (article, quantity) {
		articleFactory.orderArticle(article, quantity)
			.then(function (response) {
				articleFactory.getCurrentNumberOfMyArticles()
					.then(function (response) {
						$rootScope.currentNumberOfArticles = response.data;
					});

				data = response.data;
				$scope.successMessage = data.message;
				$('#successMessage').modal('show');

			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	};

	$scope.addToMenu = function (article, restarauntName) {
		articleFactory.addToMenu(article, restarauntName)
			.then(function (response) {
				data = response.data;
				$scope.successMessage = data.message;
				$('#successMessage').modal('show');
			}, function (response) {
				data = response.data;
				$scope.errorMessage = data.message;
				$('#errorMessage').modal('show');
			});
	};

	articleFactory.findTopDrinks()
		.then(function (response) {
			$scope.topDrinks = response.data;
		});

	articleFactory.findTopDishes()
		.then(function (response) {
			$scope.topDishes = response.data;
		});
});