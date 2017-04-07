/**
 * Created by amoghpalnitkar on 4/6/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('previewNewsController', reviewNewsController);

    reviewNewsController.$inject = ['appUtils','$scope','$stateParams'];

    /* @ngInject */
    function reviewNewsController(appUtils,$scope,$stateParams) {
        var vm = this;
        vm.title = 'reviewNewsController';
        $scope.isAdmin = appUtils.isAdmin();
        $scope.mode = "PREVIEW";

        activate();

        ////////////////

        function activate() {
            //TODO fetch news details from server
        }
    }

})();


