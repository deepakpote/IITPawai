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

        function fetch(filter,statusCodeID,onSuccess,onFailure) {
            var languageCodeIDs = "" + filter.languageCodeIDs;
            var topicCodeIDs = "" + filter.topicCodeIDs;
            var postData = {"languageCodeIDs":languageCodeIDs,
                            "pageNumber":0,
                            "topicCodeIDs":topicCodeIDs,
                            "statusCodeID":statusCodeID};
            var authToken = appUtils.getFromCookies("token","");
            options.data = postData;
            options.url = 'content/searchSelfLearning/';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }
    }

})();

