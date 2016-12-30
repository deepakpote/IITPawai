webPortal.controller("loginController", function($scope, LoginService){
	$scope.validate = function(){
		phoneNumber = $scope.phoneno;
		passkey = $scope.passKey;
		LoginService.validate(phoneNumber, passkey)
				.then(function(response){alert("success");}, function(response){alert("error");});
	};
});