/** Copied from https://www.npmjs.com/package/angular-localization, and made modifications to include com_codeList */
angular.module('i18n', []);
angular.module('i18n').provider('$i18n', i18n);

function i18n() {
  
	/** Set the default settings for language object, used for translations */
  this.language = {
    name: '',
    code: '',
    translations: {},
    settings: {
      path: '',
      prefix: '',
      type: ''
    }
  };
  
  /** Configuration for localization */
  this.config = function(settings) {
    this.language.code = settings.language || '';
    this.language.settings.path = settings.path || '';
    this.language.settings.prefix = settings.prefix || '';
    this.language.settings.type = settings.type || '';
  };
  
  
  this.$get = ['$http', '$rootScope', '$log', 'appConstants', 'appUtils', 'commonService',
  	function($http, $rootScope, $log, appConstants, appUtils, commonService) {
      var provider = this;
      
      /** Fetch the code list */
      // MOVE THE CODE FOR FETCHING CODE LIST FROM "mainController" to this file i.e lacalization.js
      var getCodeListSuccessCB = function (response) {
    		appUtils.saveToLocalStorage(appConstants.localStorage.codeListVersionKey, response.data[0].version);
    		appUtils.saveToLocalStorage(appConstants.localStorage.codeListKey, response.data[0].codeList);
    		buildCodeLocalization();
    	};
    	
    	var getCodeListErrorCB = function (response) {
    		$log.debug('localization:getCodeListErrorCB::in error cb of get code list');
    		$log.debug(response);
    	};
    	
    	var getCodeList = function() {
    		commonService.getCodeList(getCodeListSuccessCB, getCodeListErrorCB);
    	};
    	
      
      /**  
       *  Build the translation json for codes in com_code
       *  */
    	var buildCodeLocalization = function() {
      	
      	/* Get the code list stored in local storage */
      	var codeList = appUtils.getFromLocalStorage(appConstants.localStorage.codeListKey);
      	
      	/* Based on the translation is required for English / Marathi, select
      	 * the values from appropriate key in the code list json */
      	if (provider.language.code === "mr") {
      		pluckKey = "codeNameMr";
      	} else {
      		pluckKey = "codeNameEn";
      	}
      	
      	/* Pluck only the "codeID" and "codeNameEn / codeNameMr", values from codeList
      	 * and build a json object of following format -
      	 * {110100: "Message1", 110101: "Message2"} */
      	codeTranslation = _.object(_.pluck(codeList, "codeID"), _.pluck(codeList, pluckKey));
      	
      	/* Append the object built above, to the translations provided in the json files */
      	_.extend(provider.language.translations, codeTranslation);
        $rootScope.$broadcast('i18n.language:change');
      };
      
      /** Function that can be explicitly called through controllers for translations
       *  However, this function must be called in the listener for 
       *  i18n.language:change event */
      var i18n = function(translation) {
        var translatedText = provider.language.translations[translation] || '';
        return translatedText;
      };
      
      var getResourceMessages = function (path, languageCode) {
      	var successCB = function(response) {
          provider.language.name = response.data.name;
          provider.language.code = languageCode;
          provider.language.translations = response.data.translations;
          getCodeList();
        };
        
      	var errorCB = function(response) {
      		$log.debug('localization:getResourceMessages::in error cb, response is -');
      		$log.debug(response);
        };
      	$http.get(path, { cache: true }).then(successCB, errorCB);
      }
      
      i18n.language = provider.language;
      
      i18n.set = function(languageCode) {
        var path = '';
        path += provider.language.settings.path ? provider.language.settings.path + '/' : '';
        path += provider.language.settings.prefix ? provider.language.settings.prefix + '.' : '';
        path += languageCode;
        path += provider.language.settings.type ? '.' + provider.language.settings.type : '';
        getResourceMessages(path, languageCode);
      };
      
      if (provider.language.code) {
        i18n.set(provider.language.code);
      }
      
      return i18n;
    }];
};

angular
  .module('i18n')
  .directive('i18n', ['$rootScope', '$i18n', function($rootScope, $i18n) {
    return function(scope, element, attrs) {
      
      function applyLocal() {
        var translation = $i18n(attrs.i18n);
        element.text(translation);
      }
      
      applyLocal();
      
      $rootScope.$on('i18n.language:change', function() {
        applyLocal();
      });
      
    };
  }]);
