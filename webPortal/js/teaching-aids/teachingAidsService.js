/**
 * Created by amoghpalnitkar on 2/21/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .service('TeachingAidsService', TeachingAidsService);

    TeachingAidsService.$inject = ['$http'];

    /* @ngInject */
    function TeachingAidsService($http) {
        this.fetch = fetch;

        ////////////////

        function fetch(fileTypeCodeId) {

            var postData = {"fileTypeCodeID":fileTypeCodeId,"languageCodeID":101100};
            var header = {"authToken":"OF3eOof1qa5bDkHQjwPjlT24sRWb42J1",
                            "appLanguageCodeID":"113100"};
            return $http({method:'POST', url: 'http://54.152.74.194:8000' + '/content/searchTeachingAid/',
                data: postData,
                headers : header});
        }
    }

})();


