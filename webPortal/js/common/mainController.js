angular.module("mitraPortal").controller("mainController",
  ['$scope', '$location', '$log', '$route', 'appUtils', 'appConstants', 'commonService',
  function($scope, $location, $log, $route, appUtils, appConstants, commonService) {
  	
  	var getCodeListSuccessCB = function (response) {
  		$route.reload();
  		appUtils.saveToLocalStorage(appConstants.localStorage.codeListVersionKey, response.data[0].version);
  		appUtils.saveToLocalStorage(appConstants.localStorage.codeListKey, response.data[0].codeList);
  		$log.debug('in success cb of get code list');
  		$log.debug(response);
      $scope.$broadcast('codesAvailable',{});
      $log.debug('broadcast sent');
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

    $log.debug("init main controller");
  	init();

  }
]);