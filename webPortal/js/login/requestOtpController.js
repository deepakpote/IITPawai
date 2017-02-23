/**
 * Created by amoghpalnitkar on 2/14/17.
 */

angular.module("mitraPortal").controller("requestOtpController", RequestOtpController);

RequestOtpController.$inject = ['$modalInstance', 'HttpUtils','loginService', '$state','appUtils','commonService'];

function RequestOtpController($modalInstance, HttpUtils, loginService, $state, appUtils, commonService){

    console.log("request otp controller called..");
    var vm = this;
    vm.errorMessage = "";
    vm.isOtpSent = false;
    vm.hasError = false;
    vm.getOtp = getOtp;
    vm.closeModal = closeModal;
    vm.verifyOtp = verifyOtp;

    function getOtp() {
        var phoneNumber = vm.phoneno;
        loginService.requestOtp(phoneNumber)
            .then(
                function onSuccess(response){
                    console.log(response);
                    if(HttpUtils.isSuccessful(response.data)){
                        vm.isOtpSent = true;
                    }else{
                        //TODO ask pradnya regarding $rootscope.
                        //TODO throw error here decide what is to be done
                        vm.hasError = true;
                        console.log(response.data.response_message);
                        vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
                    }
                },
                function onError(response) {
                    //TODO throw error here decide what is to be done
                    vm.hasError = true;
                    vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
                }
            );
    }

    function closeModal() {
        $modalInstance.close();
    }

    function verifyOtp() {
        var phoneNumber = vm.phoneno;
        var otp = vm.otp;
        loginService.verifyOtp(phoneNumber,otp)
            .then(
                function onSuccess(response){
                    console.log(response);
                    if(HttpUtils.isSuccessful(response.data)){
                        var data = response.data.data[0];
                        appUtils.saveToLocalStorage("token",data.token);
                        $state.go('main.index.home.setpassword');
                    }else{
                        vm.hasError = true;
                        vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
                    }
                },
                function onError(response) {
                    //TODO throw error here decide what is to be done
                    // $rootScope.globals.currentUser = undefined;
                    // alert(response.data.response_message);
                    vm.hasError = true;
                    vm.errorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn
                }
            );
    }

}
