"use strict";

angular.module("mitraPortal").service('loginService', function($http,appUtils){

    var TYPE_SIGN_IN = 110101;
    //var TYPE_REGISTER = 110100;

   this.validate = function(phoneno, passkey) {
        var postData = {"phoneno": phoneno,"passkey": passkey};
        return $http({method:'POST', url: 'http://54.152.74.194:8000' + '/user/validateWebAdmin/', data: postData});
   };

    this.requestOtp = function(phoneno) {
        var postData = {"phoneNumber": "+91" + phoneno,"authenticationType":TYPE_SIGN_IN};
        return $http({method:'POST', url: 'http://54.152.74.194:8000' + '/user/requestOtp/', data: postData});
    };

    this.verifyOtp = function(phoneno,otp) {
        var postData = {"phoneNumber": "+91" + phoneno,"authenticationType":TYPE_SIGN_IN,"otp":otp,
            "fcmRegistrationRequired":"false"};
        return $http({method:'POST', url: 'http://54.152.74.194:8000' + '/user/verifyOtp/', data: postData});
    };

    this.setPassword = function(password) {
        var postData = {"password":password};
        var authToken = appUtils.getFromLocalStorage("token","");
        var header = {"authToken" : authToken};
        return $http({method:'POST', url: 'http://54.152.74.194:8000' + '/user/setPassword/', data: postData,
                        headers : header});
    };
});