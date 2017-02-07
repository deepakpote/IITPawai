angular.module("mitraPortal").controller("welcomeController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'commonService',
  function($scope, $location, $log, appUtils, appConstants, commonService) {
  	
  	var getCodeListSuccessCB = function (response) {
  		
  		appUtils.saveToLocalStorage(appConstants.localStorage.codeListVersionKey, response.data[0].version);
  		appUtils.saveToLocalStorage(appConstants.localStorage.codeListKey, response.data[0].codeList);
  		$log.debug('in success cb of get code list');
  		$log.debug(response);
  	};
  	
  	var getCodeListErrorCB = function (response) {
  		$log.debug('in error cb of get code list');
  		$log.debug(response);
  	};
  	
  	$scope.getCodeList = function() {
  		commonService.getCodeList(getCodeListSuccessCB, getCodeListErrorCB);
  	};
  	
  	var init = function () {
  		$scope.content = {};
  	  $scope.errorMessage = "";
  	  
  	  $scope.getCodeList();
  	};
  	
  	init();
  }
]);