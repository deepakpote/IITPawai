/**
 * Created by amoghpalnitkar on 4/5/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('newsListController', NewsListController);

    NewsListController.$inject = ['newsService','HttpUtils','$scope','appUtils','$state','$uibModal'];

    /* @ngInject */
    function NewsListController(newsService, httpUtils,$scope,appUtils,$state,$uibModal) {
        var vm = this;
        vm.title = 'NewsListController';
        $scope.isAdmin = appUtils.isAdmin();
        $scope.goToView = goToView;
        $scope.showDeleteConfirmation = showDeleteConfirmation;
        var modalInstance;
        activate();

        ////////////////

        function activate() {
            newsService.getNews(function onSuccess(response) {
                if(httpUtils.isSuccessful(response)) {
                    console.log(response);
                    var list = response.data;
                    for ( var i = 0; i < list.length ;i++) {
                        var momentDate = moment(list[i].publishDate);
                        list[i].publishDate = momentDate.format("D MMM YYYY");
                        console.log(list[i].publishDate);
                    }
                    $scope.newsList = list;
                }

            }, function onFailure(response) {
                console.log(response);
            })
        }

        function goToView (newsItem){
            $state.go('main.loggedIn.viewNews',
                {'newsID' : newsItem.news});
        }

        function showDeleteConfirmation(newsItem) {
            setConfirmationScopes();
            modalInstance = $uibModal.open({
                url: 'confirm',
                scope: $scope,
                templateUrl : '/js/news-circulars/list/deleteConfirmationView.html'
            });
            modalInstance.result.finally(function(){
                    $window.scrollTo(0, 0);
                    $state.go('main.loggedIn.newsList');
            });
        }

        function setConfirmationScopes() {
            $scope.deleteNews = function () {
                //TODO call server to delete news
            };

            $scope.dismissModal = function() {
                console.log("clicked..");
                modalInstance.dismiss();
            };

        }
    }

})();


