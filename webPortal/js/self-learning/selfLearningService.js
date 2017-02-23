/**
 * Created by amoghpalnitkar on 2/22/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .service('SelfLearningService', SelfLearningService);

    SelfLearningService.$inject = ['$http'];

    /* @ngInject */
    function SelfLearningService($http) {
        this.fetch = fetch;

        ////////////////

        function fetch() {
            var postData = {"languageCodeIDs":"","pageNumber":0,"topicCodeIDs":""};
            var header = {"authToken":"OF3eOof1qa5bDkHQjwPjlT24sRWb42J1",
                "appLanguageCodeID":"113101"};
            return $http({method:'POST', url: 'http://54.152.74.194:8000' + '/content/searchSelfLearning/',
                data: postData,
                headers : header});
        }
    }

})();

