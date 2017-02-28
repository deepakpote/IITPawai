/**
 * Created by amoghpalnitkar on 2/21/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .service('TeachingAidsService', TeachingAidsService);

    TeachingAidsService.$inject = ['$http','appUtils'];

    /* @ngInject */
    function TeachingAidsService($http,appUtils) {
        this.fetch = fetch;

        ////////////////

        function fetch(fileTypeCodeId,statusCodeId,dataFilters,onSuccess, onFailure) {
            var gradeIds = "" + dataFilters.gradeCodeIDs;
            var subjectIds = "" + dataFilters.subjectCodeIDs;
            var postData = {"fileTypeCodeID":fileTypeCodeId,
                            "statusCodeID":statusCodeId,
                            "gradeCodeIDs" : gradeIds,
                            "subjectCodeIDs" : subjectIds};
            var authToken = appUtils.getFromLocalStorage("token","");
            options.data = postData;
            options.url = 'content/searchTeachingAid/';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }
    }

})();


