angular.module("mitraPortal").controller("aboutUsController",
  ['$scope', '$log', 'appUtils', 'appConstants', 'commonService', '$i18n',
  function($scope, $log, appUtils, appConstants, commonService, $i18n) {  	
  	//$i18n.set('en');
  	
  	/* Example of how to set translations through controller 
  	 * Listen to the event 'i18n.language:change'. 
  	 * This listener required, for page refresh and 
  	 * language change events. */
  	$scope.$on('i18n.language:change', function(event, data) {
  		setTranslatedText();
  	});
  	
  	var setTranslatedText = function () {
  		$scope.textFromController = $i18n('107100');
    	//console.log('trnaslated text is [%s]', $scope.textFromController);
  	};
  	
  	/* For normal navigation using on screen, links - when neither 
  	 * page refresh event nor language change event triggers, a simple
  	 * call as below would work. But we'll need to put both of them in 
  	 * the controller. 
  	 * Note - translations through controller are required only 
  	 * for situation, where the translation cannot be done using
  	 * the i18n directive. */
  	setTranslatedText();
  }
]);