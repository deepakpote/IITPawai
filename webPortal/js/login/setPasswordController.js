/**
 * Created by amoghpalnitkar on 2/15/17.
 */

angular.module("mitraPortal").controller("setPasswordController", SetPasswordController);

SetPasswordController.$inject = ['$uibModalInstance','HttpUtils','loginService','$state','commonService','appUtils'];

function SetPasswordController($uibModalInstance,HttpUtils, loginService, $state, commonService, appUtils){

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
        $uibModalInstance.close();
    }
}