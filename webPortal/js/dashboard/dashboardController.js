angular.module("mitraPortal").controller("dashboardController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'contentService', 'commonService', '$state',
  function($scope, $location, $log, appUtils, appConstants, contentService, commonService, $state) {
  	
	$scope.goTo = function ( state ) {
		$state.go(state)
	};
	  
  	var init = function () {
//  		$scope.submitted = false;
//  		$scope.content = { "contentID": 0, "contentTypeCodeID": 0};
//  	  $scope.errorMessage = "";
  	  
//  	  populateDropDowns();
  	};
  	
  	init();
  }
]);