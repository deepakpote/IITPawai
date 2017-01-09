webPortal.service("NotificationsService", function($http, $window) {
	this.sendDataNotifications = function(dataMessage) {
		   alert("In notification service:" + dataMessage);
		   //The ajax call for sending the call to FCM will come here
	   };
});