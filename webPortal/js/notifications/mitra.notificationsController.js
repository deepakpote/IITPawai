angular.module("mitraPortal").controller("mitra.notificationsController", function($scope,$rootScope, NotificationsService) {
	$scope.sendDataNotifications = function(){
		dataMessage = $scope.notificationDataMessage;
		//alert("Token: " + $rootScope.globals.currentUser.token);
		NotificationsService.sendDataNotifications(dataMessage)
				.then(
					function(response){
						alert("Notification sent successfully");
					}, 
					function(response){
						alert("There was an error sending notification.")
						console.log(response);
					}
				);
		
	};
});
