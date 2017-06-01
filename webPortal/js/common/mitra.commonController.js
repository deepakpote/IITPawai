angular.module("mitraPortal").controller("CommonController", function($scope, $location,$rootScope,$cookies){
	$scope.errormessage = ""; 
	$scope.logout = function(){
		$rootScope.globals.currentUser = {};
		delete $rootScope.appGlobals
		$cookies.putObject("globals",{});
		$location.path("/login");
	};
});
