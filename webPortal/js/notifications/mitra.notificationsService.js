angular.module("mitraPortal").service("NotificationsService", function($http, $window) {
	this.sendDataNotifications = function(dataMessage, utoken) {
		   var postData = {"title": "Mitra","body": dataMessage, "utoken": utoken};
		   var serverName = "http://localhost:8000";
		   serverName = $window.location.origin;
		   return $http({method:'POST', url: serverName + '/user/sendDataNotificationsToAll/', data: postData});
	   };
});
