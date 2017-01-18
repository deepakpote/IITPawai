webPortal.controller("CommonController", function($scope, $location,$rootScope,$cookies){
	$scope.errormessage = ""; 
	$scope.logout = function(){
		$rootScope.globals.currentUser = {};
		$cookies.putObject("globals",{});
		$location.path("/login");
	};
});
