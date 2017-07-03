angular.module("mitraPortal").controller("mitra.notificationsController", function($scope,$rootScope, NotificationsService) {
	$scope.sendDataNotifications = function(){
		dataMessage = $scope.notificationDataMessage;
		alert("Token: " + $rootScope.globals.currentUser.token);
		NotificationsService.sendDataNotifications(dataMessage, $rootScope.globals.currentUser.token)
				.then(
					function(response){
						alert(response.data.response_message);
					}, 
					function(response){
						alert("error");
					}
				);
		
	};
});
