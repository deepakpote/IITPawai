/**
 * Created by amoghpalnitkar on 3/2/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('leftMenuController', leftMenuController);

    leftMenuController.$inject = ['$scope','appUtils'];

    /* @ngInject */
    function leftMenuController($scope, appUtils) {
        $scope.selectedItem = 'home';
        $scope.isAdmin = appUtils.isAdmin();
        $scope.isTeacher = appUtils.isTeacher();

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
                    case "main.loggedIn.newsList":
                        $scope.selectedItem = 'news-list';
                        break;
                    case "main.loggedIn.previewNews":
                        $scope.selectedItem = 'preview-news';
                        break;
                    case "main.loggedIn.sendNotification":
                        $scope.selectedItem = 'send-notification';
                        break;
                }
            });
    }

})();


