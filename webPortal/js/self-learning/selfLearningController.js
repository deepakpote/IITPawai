/**
 * Created by amoghpalnitkar on 2/22/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('selfLearningController', SelfLearningController);

    SelfLearningController.$inject = ['SelfLearningService','$scope','commonService'];

    /* @ngInject */
    function SelfLearningController(SelfLearningService,$scope,commonService) {
        var vm = this;
        vm.title = 'SelfLearningController';
        vm.fetch = fetchSelfLearning;

        activate();

        ////////////////

        function activate() {
            if (commonService.isCodeListEmpty()) {
                $scope.$on('codesAvailable', function(event,data){
                    fetchSelfLearning();
                });
            } else {
                fetchSelfLearning();
            }
        }

        function setStatus(status) {
            vm.status = status;
            fetchSelfLearning();
        }

        function setFileType(fileType) {
            vm.fileType = fileType;
            fetchTeachingAids();
        }

        function fetchSelfLearning() {
            //TODO set appropriate filters here
            var filter = {};
            SelfLearningService.fetch(filter, onSuccess, onFailure);

            function onSuccess(response){
                var contents = response.data;
                console.log(contents);
                for(var i =0 ; i < contents.length ; i ++) {
                    var content = contents[i];
                    content.topicName = commonService.getValueByCode(content.topic)[0].codeNameEn;
                    content.language = commonService.getValueByCode(content.language)[0].codeNameEn;
                }
                vm.data = contents;
            }
            function onFailure(response) {

            }

        }
    }

})();


