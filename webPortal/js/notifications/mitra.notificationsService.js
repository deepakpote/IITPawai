angular.module("mitraPortal").service('NotificationsService', ['$http','appUtils','appConstants',
		function($http, appUtils, appConstants) 
		{
	this.sendDataNotifications = function(dataMessage) {
			//var authToken = appUtils.getFromCookies("token","");
		   var postData = {"title": "Mitra","body": dataMessage};
		   //var serverName = "http://localhost:8000";
		   //serverName = $window.location.origin;
		   return $http({method:'POST', url: appConstants.endpoint.baseUrl + 'user/sendDataNotificationsToAll/', data: postData});
	   };
}]);
