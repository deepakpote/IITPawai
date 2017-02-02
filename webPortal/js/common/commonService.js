angular.module("mitraPortal").service('commonService', ['appUtils', 'appConstants', '_',
	function(appUtils, appConstants, _) {
    
		var service = {};
		
		service.getCodeList = function (successCB, errorCB) {
  		options = {};
  		options.url = appConstants.endpoint.code.list;
  		options.method = 'GET';
  		appUtils.ajax(options, successCB, errorCB);
		};
		
		service.getCodeListPerCodeGroup = function (codeGroupID) {
			storedCodeList = appUtils.getFromLocalStorage(appConstants.localStorage.codeListKey, []);
			
			return _.filter(storedCodeList, function(code){ 
				return code.codeGroup === codeGroupID
			});
		}
		
		return service;
	}
]);