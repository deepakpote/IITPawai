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
        this.fetchMore = fetchMore;
        this.getAuthorList = getAuthorList;

        ////////////////

        function fetch(fileTypeCodeId,statusCodeId,dataFilters,uploadedBy,onSuccess, onFailure) {
            var gradeIds = "" + dataFilters.gradeCodeIDs;
            var subjectIds = "" + dataFilters.subjectCodeIDs;
            var postData = {"fileTypeCodeID":fileTypeCodeId,
                            "statusCodeID":statusCodeId,
                            "gradeCodeIDs" : gradeIds,
                            "subjectCodeIDs" : subjectIds,
                            "uploadedBy" : uploadedBy,
                            "pageNumber" : 0};
            var authToken = appUtils.getFromCookies("token","");
            options.data = postData;
            options.url = 'content/searchTeachingAid/';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }

        function fetchMore(fileTypeCodeId,statusCodeId,dataFilters,uploadedBy,onSuccess, onFailure) {
            var gradeIds = "" + dataFilters.gradeCodeIDs;
            var subjectIds = "" + dataFilters.subjectCodeIDs;
            var postData = {"fileTypeCodeID":fileTypeCodeId,
                            "statusCodeID":statusCodeId,
                            "gradeCodeIDs" : gradeIds,
                            "subjectCodeIDs" : subjectIds,
                            "uploadedBy" : uploadedBy,
                            "pageNumber" : 1};
            var authToken = appUtils.getFromCookies("token","");
            options.data = postData;
            options.url = 'content/searchTeachingAid/';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }

        function getAuthorList(onSuccess,onFailure) {
            var authToken = appUtils.getFromCookies("token","");

            options.url = 'content/getContentUploadedByList/';
            options.headers = {"authToken":authToken};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }


    }

})();


