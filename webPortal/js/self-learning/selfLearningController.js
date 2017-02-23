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
            SelfLearningService.fetch().then(
                function onSuccess(response){
                    var contents = response.data.data;
                    vm.data = response.data.data;
                },
                function onFailure(response) {

                }
            );
        }
    }

})();


