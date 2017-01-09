webPortal.controller("NotificationsController", function($scope, NotificationsService) {
	$scope.sendDataNotifications = function(){
		dataMessage = $scope.notificationDataMessage;
		alert("Send following message:" + dataMessage);
		//NotificationsService.sendDataNotifications(dataMessage)
		//		.then(function(response){alert("success");}, function(response){alert("error");});
		
	};
});