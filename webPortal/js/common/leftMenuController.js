/**
 * Created by amoghpalnitkar on 3/2/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('leftMenuController', leftMenuController);

    leftMenuController.$inject = ['$scope'];

    /* @ngInject */
    function leftMenuController($scope) {
        $scope.selectedItem = 'home';
        $scope.setSelected = function(item) {
            $scope.selectedItem = item;
        }
    }

})();


