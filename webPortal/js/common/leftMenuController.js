angular.module("mitraPortal").controller("mainController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'commonService', '$i18n',
  function($scope, $location, $log, appUtils, appConstants, commonService, $i18n) {
  	
  	/** On language change event */
  	$scope.languageChange = function () {
  		if(appUtils.getAppLanguage() === appConstants.appAvailableLanguages.english.codeID) {
  			appUtils.setAppLanguage(appConstants.appAvailableLanguages.marathi.codeID);
  			$i18n.set(appConstants.appAvailableLanguages.marathi.shortCode);
  		}
  		else {
  			appUtils.setAppLanguage(appConstants.appAvailableLanguages.english.codeID);
  			$i18n.set(appConstants.appAvailableLanguages.english.shortCode);
  		}
  	};
  	
  }
]);