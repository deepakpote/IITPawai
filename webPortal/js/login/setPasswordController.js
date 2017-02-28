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

        var password = vm.password;
        loginService.setPassword(password,onSuccess,onFailure);

        function onSuccess(response) {
            console.log("login response");
            if(HttpUtils.isSuccessful(response)) {
                $state.go('main.loggedIn.home');
            } else {
                vm.hasError = true;
                vm.errorMessage = commonService.getValueByCode(response.response_message)[0].codeNameEn
            }
        }

        function onFailure(response) {
            vm.hasError = true;
            vm.errorMessage = commonService.getValueByCode(response.response_message)[0].codeNameEn
        }

    }

    function closeModal () {
        $modalInstance.close();
    }
}