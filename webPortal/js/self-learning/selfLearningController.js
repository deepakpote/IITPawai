/**
 * Created by amoghpalnitkar on 2/22/17.
 */

(function () {
    'use strict';

    angular
        .module('mitraPortal')
        .controller('selfLearningController', SelfLearningController);

    SelfLearningController.$inject = ['SelfLearningService','$scope','commonService','appConstants', '$state'];

    /* @ngInject */
    function SelfLearningController(SelfLearningService,$scope,commonService,appConstants, $state) {
        var vm = this;
        vm.title = 'SelfLearningController';
        vm.fetch = fetchSelfLearning;
        vm.dataFilter = {
            "languageCodeIDs" : "",
            "topicCodeIDs" : ""
        };
        vm.setSelectedOption = setSelectedOption;
        vm.setStatus = setStatus;
        vm.status = 114101;
        vm.batchLength = 9;
        vm.loadMore = loadMore;
        vm.hasMoreData = true;
        vm.goToReview =goToReview;

        activate();

        /**
         * all functions below
         */

        function activate() {
            if (commonService.isCodeListEmpty()) {
                $scope.$on('codesAvailable', function(event,data){
                    getTopics();
                    getLanguage();
                    setTopicAndLanguageWatchers();
                    fetchSelfLearning();
                });
            } else {
                getTopics();
                getLanguage();
                setTopicAndLanguageWatchers();
                fetchSelfLearning();
            }
        }

        function setStatus(status) {
            vm.status = status;
            fetchSelfLearning();
        }

        function fetchSelfLearning() {
            //TODO set appropriate filters here
            SelfLearningService.fetch(vm.dataFilter,vm.status,onSuccess, onFailure);

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

        function loadMore() {
            SelfLearningService.fetchMore(vm.dataFilter,vm.status,onSuccess, onFailure);

            function onSuccess(response){
                var contents = response.data;
                console.log(contents);
                for(var i =0 ; i < contents.length ; i ++) {
                    var content = contents[i];
                    content.topicName = commonService.getValueByCode(content.topic)[0].codeNameEn;
                    content.language = commonService.getValueByCode(content.language)[0].codeNameEn;
                }
                for(i =0 ; i < contents.length ; i ++) {
                    content = contents[i];
                    vm.data.push(content);
                }
                vm.hasMoreData = false;
            }
            function onFailure(response) {

            }
        }

        function getTopics() {
            $scope.topicList = commonService.getCodeListPerCodeGroup(
                appConstants.codeGroup.topic
            );
        }

        function getLanguage() {
            $scope.contentLanguageList = commonService.getCodeListPerCodeGroup(
                appConstants.codeGroup.contentLanguage
            );
        }

        function setSelectedOption(option) {
            if(option === vm.selectedOption) {
                vm.selectedOption = "";
            } else {
                vm.selectedOption = option;
            }
        }

        function showDataFilters() {
            console.log(vm.dataFilter);
        }
        function goToReview (selfLearning){
            $state.go('main.loggedIn.reviewContent',
                {'contentID' : selfLearning.contentID});
        }

        function setTopicAndLanguageWatchers() {
            /**
             * watch topic list to make the server call on change
             * */
            $scope.$watch('topicList', function (topicList){
                if(topicList != undefined) {
                    var checkedTopics = topicList.filter(function(topic){ return (topic.checked == true)});
                    var topicString = "";
                    if (checkedTopics.length > 0){
                        topicString = checkedTopics[0].codeID;
                    }
                    for (var i = 1;i < checkedTopics.length; i++){
                        topicString += ',' + checkedTopics[i].codeID;
                    }
                    vm.dataFilter.topicCodeIDs = topicString;
                    fetchSelfLearning();
                }
            }, true);

            /**
             * watch language list to make the server call on change
             * */
            $scope.$watch('languageList', function (languageList){
                if(languageList != undefined) {
                    var checkedLanguages = languageList.filter(function(language){ return (language.checked == true)});
                    var languageString = "";
                    if (checkedLanguages.length > 0){
                        languageString = checkedLanguages[0].codeID;
                    }
                    for (var i = 1;i < checkedSubjects.length; i++){
                        languageString += ',' + checkedLanguages[i].codeID;
                    }
                    vm.dataFilter.languageCodeIDs = languageString;
                    fetchSelfLearning();
                }
            }, true);
        }
    }

})();


