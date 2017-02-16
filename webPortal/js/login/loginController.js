angular.module("mitraPortal").controller("loginController", LoginController);

LoginController.$inject = ['$location', '$modalInstance', '$rootScope' ,'$cookies', 'loginService'];

function LoginController($location, $modalInstance, $rootScope,$cookies, loginService) {

    console.log("login controller called..");
    var vm = this;
    vm.errormessage = "";
    vm.validate = validate;
    vm.closeModal = closeModal;

    function validate(){
	    console.log("validate called..");
		var phoneNumber = "+91" + vm.phoneno;
		var passkey = vm.passkey;

		loginService.validate(phoneNumber, passkey)
			.then(
					function(response){
                        console.log(response);
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
					}
				);
	}

	function closeModal () {
	    $modalInstance.close();
    }

}
