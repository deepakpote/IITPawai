/**
 * Created by amoghpalnitkar on 2/14/17.
 */

angular.module("mitraPortal").controller("requestOtpController", RequestOtpController);

RequestOtpController.$inject = ['$modalInstance', 'HttpUtils', '$rootScope' ,'loginService', '$state'];

function RequestOtpController($modalInstance, HttpUtils, $rootScope , loginService, $state){

    console.log("request otp controller called..");
    var vm = this;
    vm.errormessage = "";
    vm.isOtpSent = false;
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
                        $rootScope.globals.currentUser = undefined;
                        alert(response.data.response_message);
                    }
                },
                function onError(response) {
                    //TODO throw error here decide what is to be done
                    $rootScope.globals.currentUser = undefined;
                    alert(response.data.response_message);
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
                        $state.go('main.index.home.setpassword');
                    }else{
                        //TODO ask pradnya regarding $rootscope.
                        //TODO throw error here decide what is to be done
                        $rootScope.globals.currentUser = undefined;
                        alert(response.data.response_message);
                    }
                },
                function onError(response) {
                    //TODO throw error here decide what is to be done
                    $rootScope.globals.currentUser = undefined;
                    alert(response.data.response_message);
                }
            );
    }

}
