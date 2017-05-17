/**
 * Created by Dipak Pote.
 * Date: 15-May-2017
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .service('newsListService', newsListService);

    newsListService.$inject = ['$http','appUtils'];

    /* @ngInject */
    function newsListService($http,appUtils) {
        this.fetch = fetch;


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

    }

})();


