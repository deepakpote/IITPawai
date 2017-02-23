angular.module("mitraPortal").controller("dashboardController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'contentService', 'commonService',
  function($scope, $location, $log, appUtils, appConstants, contentService, commonService) {
  	
	$scope.goTo = function ( path ) {
		$location.path( path );
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