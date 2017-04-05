/**
 * Created by amoghpalnitkar on 4/5/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('newsListController', NewsListController);

    NewsListController.$inject = ['newsService'];

    /* @ngInject */
    function NewsListController(newsService) {
        var vm = this;
        vm.title = 'NewsListController';

        activate();

        ////////////////

        function activate() {
            newsService.getNews(function onSuccess(response) {

                console.log(response);

            }, function onFailure(response) {
                console.log(response);
            })
        }
    }

})();


