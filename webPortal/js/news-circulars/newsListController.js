/**
 * Created by amoghpalnitkar on 4/5/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('newsListController', NewsListController);

    NewsListController.$inject = ['newsService','HttpUtils','$scope','appUtils'];

    /* @ngInject */
    function NewsListController(newsService, httpUtils,$scope,appUtils) {
        var vm = this;
        vm.title = 'NewsListController';
        $scope.isAdmin = appUtils.isAdmin();

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
    }

})();


