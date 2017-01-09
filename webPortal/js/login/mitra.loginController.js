webPortal.controller("LoginController", function($scope, $location,$rootScope, LoginService){
	 
	$scope.validate = function(){
		var phoneNumber = $scope.phoneno;
		var passkey = $scope.passKey;

		LoginService.validate(phoneNumber, passkey)
				.then(
						function(response){
							if(!response.data.error){
								alert("Valid user");
								$rootScope.globals.currentUser = response.usertoken;
								$location.path("/sendDataNotifications");
							}else{
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