webPortal.service('loginService', function($http, $window){
    
   this.validate = function(phoneno, passkey) {
	   var postData = {"phoneno": phoneno,"passkey": passkey};
	   var serverName = "http://localhost:8000";
	   serverName = $window.location.origin;
	   return $http({method:'POST', url: serverName + '/user/validateWebAdmin/', data: postData});
   };
   	  
});
