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
        };
        $scope.$on('$stateChangeStart',
            function(event, toState, toParams, fromState, fromParams){
                console.log(toState);
                switch (toState.name) {
                    case "main.loggedIn.home":
                        $scope.selectedItem = 'home';
                        break;
                    case "main.loggedIn.contentUpload":
                        $scope.selectedItem = 'content-upload';
                        break;
                    case "main.loggedIn.selfLearning":
                        $scope.selectedItem = 'add-self-learning';
                        break;
                    case "main.loggedIn.teachingAids":
                        $scope.selectedItem = 'add-teaching-aids';
                        break;
                    case "main.loggedIn.addNews":
                        $scope.selectedItem = 'add-news';
                        break;
                }
            });
    }

})();


