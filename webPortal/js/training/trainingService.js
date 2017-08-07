angular.module("mitraPortal").service('trainingService', ['appUtils', 'appConstants',
	function(appUtils, appConstants) {
    
		var service = {};
	
		service.addTraining = function (news, successCB, errorCB) {
  		options = {};
  		options.url = appConstants.endpoint.training.add;
  		
  		options.data = news;  		
  		appUtils.ajax(options, successCB, errorCB);
		}
		
		service.getBlockList = function(districtCodeID,success,failure) {
		    var options = {};
            var authToken = appUtils.getFromCookies("token","");
            options.headers = {"authToken" : authToken};
		    options.data = {"districtCodeID":districtCodeID};
		    options.url = "events/getBlockListFromDistrict/";
            options.method = "POST";
		    appUtils.ajax(options, success,failure);
        };
        
		service.getTrainerList = function(success,failure) {
		    var options = {};
            var authToken = appUtils.getFromCookies("token","");
		    options.data = {};
		    options.url = "events/getTrainerList/";
            options.method = "POST";
		    appUtils.ajax(options, success,failure);
        };
	
		return service;
	}
]);