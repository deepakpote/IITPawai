angular.module("mitraPortal").controller("loginController", LoginController);

LoginController.$inject = ['$location', '$modalInstance', '$rootScope' ,'$cookies', 'loginService','commonService',
                                'appUtils','HttpUtils','$state'];

function LoginController($location, $modalInstance, $rootScope,$cookies, loginService,
                         commonService, appUtils,HttpUtils,$state) {

    console.log("login controller called..");
    var vm = this;
    vm.errormessage = "";
    vm.validate = validate;
    vm.closeModal = closeModal;
    vm.requestOtp = requestOtp;

    function validate(){
	    console.log("validate called..");
		var phoneNumber = "+91" + vm.phoneno;
		var passkey = vm.passkey;

		loginService.validate(phoneNumber, passkey,onSuccess, onFailure);

        function onSuccess(response){
            console.log(response);
            if(HttpUtils.isSuccessful(response)){
                var data = response.data[0];
                appUtils.saveToCookies("token",data.token);
                $state.go('main.loggedIn.home');
            }else{
                vm.hasError = true;
                vm.errorMessage = commonService.getValueByCode(response.response_message)[0].codeNameEn
            }
        }

        function onFailure(response) {
            vm.hasError = true;
            vm.errorMessage = commonService.getValueByCode(response.response_message)[0].codeNameEn
        }

	}

	function requestOtp(){
        commonService.setPhoneNumber(vm.phoneno);
        $state.go("main.notLoggedIn.home.requestotp");
    }

	function closeModal () {
	    $modalInstance.close();
    }

}
