/**
 * Created by amoghpalnitkar on 2/22/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .service('SelfLearningService', SelfLearningService);

    SelfLearningService.$inject = ['$http','appUtils'];

    /* @ngInject */
    function SelfLearningService($http,appUtils) {
        this.fetch = fetch;

        ////////////////

        function fetch(filter,onSuccess,onFailure) {
            var postData = {"languageCodeIDs":"","pageNumber":0,"topicCodeIDs":""};
            var authToken = appUtils.getFromLocalStorage("token","");
            options.data = postData;
            options.url = 'content/searchSelfLearning/';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }
    }

})();

