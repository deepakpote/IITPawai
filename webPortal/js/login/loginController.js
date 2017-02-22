angular.module("mitraPortal").controller("loginController", LoginController);

LoginController.$inject = ['$location', '$modalInstance', '$rootScope' ,'$cookies', 'loginService','commonService',
                                'appUtils'];

function LoginController($location, $modalInstance, $rootScope,$cookies, loginService, commonService, appUtils) {

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
                function onSuccess(response){
                    console.log(response);
                    if(response.data.data.error == "false"){
                        var data = response.data.data[0];
                        appUtils.saveToLocalStorage("token",data.token);

                        //TODO go to logged in state
                        $state.go('main.index.home');
                    }else{
                        vm.hasError = true;
                        vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
                    }
                },
                function onFailure(response) {
                    vm.hasError = true;
                    vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
                }
            );
	}

	function closeModal () {
	    $modalInstance.close();
    }

}
