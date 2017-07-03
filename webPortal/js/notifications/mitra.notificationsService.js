angular.module("mitraPortal").service('NotificationsService', ['appConstants',
		function($http, $window, appConstants) 
		{
	this.sendDataNotifications = function(dataMessage, utoken) {
		   var postData = {"title": "Mitra","body": dataMessage, "utoken": utoken};
		   //var serverName = "http://localhost:8000";
		   //serverName = $window.location.origin;
		   return $http({method:'POST', url: appConstants.endpoint.baseUrl + '/user/sendDataNotificationsToAll/', data: postData});
	   };
}]);
