angular.module("mitraPortal").service('newsService', ['appUtils', 'appConstants',
	function(appUtils, appConstants) {
    
		var service = {};
	
		service.addNews = function (news, successCB, errorCB) {
  		options = {};
  		options.url = appConstants.endpoint.news.add;
  		
  		options.data = news;  		
  		appUtils.ajax(options, successCB, errorCB);
		}
	
		return service;
	}
]);