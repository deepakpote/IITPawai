angular.module("mitraPortal").service('newsService', ['appUtils', 'appConstants',
	function(appUtils, appConstants) {
    
		var service = {};
	
		service.addNews = function (news, successCB, errorCB) {
  		options = {};
  		options.url = appConstants.endpoint.news.add;
  		
  		options.data = news;  		
  		appUtils.ajax(options, successCB, errorCB);
		};

		service.getNews = function(success,error) {
		    options = {};
            options.url = 'news/newsList';
            var authToken = appUtils.getFromCookies("token","");
            options.method = 'POST';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            appUtils.ajax(options,success,error);
        };
		return service;
	}
]);