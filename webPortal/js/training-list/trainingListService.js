/**
 * Created by Dipak Pote.
 * Date: 20-July-2017
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .service('trainingListService', trainingListService);

    trainingListService.$inject = ['$http','appUtils'];

    /* @ngInject */
    function trainingListService($http,appUtils) {
        this.fetch = fetch;
        this.fetchUsersNews = fetchUsersNews;


        function fetch(newsCategoryCodeID,departmentCodeID,publishFromDate,publishToDate,onSuccess, onFailure) {

            var postData = {"newsCategoryCodeID":newsCategoryCodeID,
                            "departmentCodeID":departmentCodeID,
                            "publishFromDate" : publishFromDate,
                            "publishToDate" : publishToDate};
            var authToken = appUtils.getFromCookies("token","");
            options.data = postData;
            options.url = 'news/newsList/';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }
        
        function fetchUsersNews(newsCategoryCodeID,departmentCodeID,publishFromDate,publishToDate,onSuccess, onFailure) {

            var postData = {"newsCategoryCodeID":newsCategoryCodeID,
                            "departmentCodeID":departmentCodeID,
                            "publishFromDate" : publishFromDate,
                            "publishToDate" : publishToDate};
            var authToken = appUtils.getFromCookies("token","");
            options.data = postData;
            options.url = 'news/userNewsList/';
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
            options.method = 'POST';
            appUtils.ajax(options,onSuccess,onFailure);
        }

    }

})();


