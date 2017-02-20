angular.module("mitraPortal").controller("welcomeController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'commonService', '$i18n',
  function($scope, $location, $log, appUtils, appConstants, commonService, $i18n) {  	
  	//$i18n.set('en');
  	console.log('fetching translation for foo');
  	$scope.testResx = $i18n('foo');
  	console.log('trnaslated text is [%s]', $scope.testResx);
  	init();
  }
]);