webPortal.controller("LoginController", function($scope, $location,$rootScope,$cookies, LoginService){
	 
	$scope.validate = function(){
		var phoneNumber = $scope.phoneno;
		var passkey = $scope.passKey;
		LoginService.validate(phoneNumber, passkey)
				.then(
						function(response){
							if(!response.data.error){
								$rootScope.globals.currentUser = "mitra";
								$cookies.putObject("globals",{"currentUser":response.usertoken});
								$location.path("/sendDataNotifications");
							}else{
								$rootScope.globals.currentUser = undefined;
								$location.path("/login");
								$scope.message = response.response_message; 
							}
						}, 
						function(response){
							$location.path("/login");
						}
					);
		
	};
});