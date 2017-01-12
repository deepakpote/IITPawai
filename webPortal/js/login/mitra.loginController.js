webPortal.controller("LoginController", function($scope, $location,$rootScope,$cookies, LoginService){
	$scope.errormessage = ""; 
	$scope.validate = function(){
		var phoneNumber = $scope.phoneno;
		var passkey = $scope.passkey;

		LoginService.validate(phoneNumber, passkey)
				.then(
						function(response){
							
							if(response.data.data.error == "false"){
								var tokenObject = {token:response.data.data.usertoken};
								var cookieObject = {currentUser:tokenObject};
								$rootScope.globals.currentUser = tokenObject;
								$cookies.putObject("globals",cookieObject);
								$location.path("/sendDataNotifications");
							}else{
								$rootScope.globals.currentUser = undefined;
								alert(response.data.response_message);
								//$location.path("/login");
							}
						}, 
						function(response){
							$location.path("/login");
						}
					);
	};
});
