/**
 * Created by amoghpalnitkar on 2/15/17.
 */

angular.module("mitraPortal").controller("setPasswordController", SetPasswordController);

SetPasswordController.$inject = ['$modalInstance','HttpUtils','loginService','$state','commonService'];

function SetPasswordController($modalInstance,HttpUtils, loginService, $state, commonService){

    console.log("set password controller called..");
    var vm = this;
    vm.errormessage = "";
    vm.setPassword = setPassword;
    vm.closeModal = closeModal;

    function setPassword() {
        console.log("set password");
        //TODO write server call to set password
        var password = vm.password;
        loginService.setPassword(password).then(
            function onSuccess(response) {
                if(HttpUtils.isSuccessful(response.data)) {
                    //TODO Access-Controler-Allow-Headers should be handled by the server
                    $state.go('main.index.home');
                } else {
                    //TODO decide what to do in error state
                    vm.hasError = true;
                    vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
                }
            },
            function onFailure(response) {
                //TODO decide what to do in error state
                vm.hasError = true;
                vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
            }
        );
    }

    function closeModal () {
        $modalInstance.close();
    }
}