"use strict";

angular.module("mitraPortal").service('loginService', function(appUtils){

    var TYPE_SIGN_IN = 110101;
    //var TYPE_REGISTER = 110100;

   this.validate = function(phoneno, passkey, onSuccess, onFailure) {
       var options = {};
       var postData = {"phoneNumber": phoneno,"password": passkey};
       options.data = postData;
       options.url = 'user/webSignIn/';
       options.method = 'post';
       appUtils.ajax(options,onSuccess,onFailure);
   };

    this.requestOtp = function(phoneno, onSuccess, onFailure) {
        var options = {};
        options.data = {"phoneNumber": "+91" + phoneno,"authenticationType":TYPE_SIGN_IN};
        options.url = 'user/requestOtp/';
        options.method = 'post';
        appUtils.ajax(options,onSuccess,onFailure);
    };

    this.verifyOtp = function(phoneno,otp, onSuccess, onFailure) {
        var options = {};
        options.data = {"phoneNumber": "+91" + phoneno,"authenticationType":TYPE_SIGN_IN,"otp":otp,
            "fcmRegistrationRequired":"false"};
        options.url = 'user/verifyOtp/';
        options.method = 'post';
        appUtils.ajax(options,onSuccess,onFailure);
    };

    this.setPassword = function(password, onSuccess, onFailure) {
        var options = {};
        options.data = {"password":password};
        var authToken = appUtils.getFromCookies("token","");
        console.log("auth token get " + authToken);
        options.headers = {"authToken" : authToken};
        options.url = 'user/setPassword/';
        appUtils.ajax(options,onSuccess,onFailure);
    };
});