webPortal.service('LoginService', function($http, $window){
    
   this.validate = function(phoneno, passkey) {
	   var data = {"phonno": phoneno,"passkey": passkey};
	   var config = {};
	   var serverName = "http://localhost:8000";
	   //serverName = $window.location.origin;
	   $http.post(serverName + '/user/validateWebAdmin/', data, config);
   };
});