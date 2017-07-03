angular.module("mitraPortal").service("NotificationsService", ['appConstants',
		function($http, $window, appConstants) 
		{
			this.sendDataNotifications = function(dataMessage, utoken) {
		   var postData = {"title": "Mitra","body": dataMessage, "utoken": utoken};
		   var serverName = appConstants.endpoint.baseUrl;
		   serverName = $window.location.origin;
		   return $http({method:'POST', url: serverName + '/user/sendDataNotificationsToAll/', data: postData});
	   };
}]);
