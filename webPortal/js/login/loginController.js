angular.module("mitraPortal").controller("loginController", LoginController);

LoginController.$inject = ['$location', '$uibModalInstance', '$rootScope' ,'$cookies', 'loginService','commonService',
                                'appUtils','HttpUtils','$state'];

function LoginController($location, $uibModalInstance, $rootScope,$cookies, loginService,
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
                loginService.getUserRoleList( 
                    //on success of getUserRoleList
                    function (response){
                        var data = response.data;
                        var roleIDs = [];
                        for (i=0;i<data.length;i++){
                            roleIDs.push(data[i].roleID);
                        }
                        appUtils.saveToCookies("roleIDs",roleIDs.join(','));
                        console.log(roleIDs);
                        $state.go('main.loggedIn.home');
                    },
                    //on failure of getUserRoleList
                    function (response){
                       vm.hasError = true;
                        vm.errorMessage = commonService.getValueByCode(response.response_message)[0].codeNameEn
                    }
                )

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
	    $uibModalInstance.close();
    }

}
