angular.module("mitraPortal").controller("newsListController", newsListController);


newsListController.$inject = ['newsListService','commonService','$scope','appConstants','$filter', '$state', 'appUtils' , '$log'];

function newsListController(newsListService,commonService,$scope,appConstants,filter,$state,appUtils,$log) {

    var vm = this;
    vm.newsCategoryCodeID = 115100;
    vm.departmentCodeID = setDepartment;
    vm.publishFromDate = setPublishFromDate;
    vm.publishToDate = setPublishToDate;
    
//    vm.setStatus = setStatus;
//    vm.setFileType = setFileType;
    vm.status = 114102;
    vm.fileType = 108100;
    vm.data = {};
    vm.goToPreview = goToPreview;

    vm.selectedOption = "";
    vm.setSelectedOption = setSelectedOption;
    vm.dataFilter = {
        "subjectCodeIDs" : "",
        "gradeCodeIDs" : ""
    };
    vm.showDataFilters = showDataFilters;
//    vm.loadMore = loadMore;
    vm.hasMoreData = true;
    vm.filterByUploader = filterByUploader;
    vm.setAscending = setAscending;
    vm.setDescending = setDescending;
    vm.orderByKey = '';
    vm.isAdmin = appUtils.isAdmin();
    if(!vm.isAdmin) {
        vm.status = 114100;
    }
 
    fetchNewsList();
    
    //activate();

    ////////////////

//    function activate() {
//    	
//        newsListService.getAuthorList(
//            function onSuccess(response) 
//            {
//            	
//                $scope.uploaderList = response.data;
//                console.log($scope.uploaderList);
//            },
//            function onFailure(response) {
//            							 }
//            );
//        
//        
//        if (commonService.isCodeListEmpty()) {
//            $scope.$on('codesAvailable', function(event,data){
//                getSubjects();
//                getGrades();
//                fetchNewsList();
//                setGradeAndSubjectWatchers();
//            });
//        } else {
//            getSubjects();
//            getGrades();
//            fetchNewsList();
//            setGradeAndSubjectWatchers();
//        }
//    }
    
    //set news department
    function setDepartment(departmentCodeID) {
        vm.newsCategoryCodeID = departmentCodeID;
        fetchNewsList();
    }


 //setPublishFromDate
    function setPublishFromDate(PublishFromDate) {
        vm.publishFromDate = PublishFromDate;
        fetchNewsList();
    }
    
    //setPublishToDate
    function setPublishToDate(publishToDate) {
        vm.publishToDate = publishToDate;
        fetchNewsList();
    }

    function goToPreview(newsList){
    	$log.debug('newsID' + newsList.news);
        $state.go('main.loggedIn.previewNews',
            {'newsID' : newsList.news});
    }

    function fetchNewsList() {
        newsListService.fetch(vm.newsCategoryCodeID, vm.departmentCodeID, vm.publishFromDate,vm.publishToDate ,onSuccess, onFailure);
        function onSuccess(response) {
            var objnews = response.data;
            console.log("onSuccess Response : " + objnews);
//            for(var i = 0 ; i < objnews.length ; i ++) {
//                var news = objnews[i];
//                news.subjectName = commonService.getValueByCode(content.subject)[0].codeNameEn;
//                var ids = content.gradeCodeIDs.split(",");
//                var grades = "";
//                for (var j = 0 ; j < ids.length ; j++) {
//                    grades = grades + " Grade " + commonService.getValueByCode(ids[j])[0].codeNameEn;
//                }
//                content.grades = grades;
//            }
//            for(i = 0 ; i < contents.length ; i ++) {
//                if(contents[i].fileType == 108100) {
//                    var videoId = parseYoutubeUrl(contents[i].fileName);
//                    contents[i].thumbnailUrl = "http://img.youtube.com/vi/" + videoId + "/0.jpg";
//                }
//            }
            vm.data = objnews;
        }
        function onFailure(response) {
        	console.log("onFailure Response : " + response);
        }
    }

    function parseYoutubeUrl(url) {
        var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
        var match = url.match(regExp);
        if (match && match[2].length == 11) {
            return match[2];
        } else {
            //error
        }
    }

    function getSubjects() {
        $scope.subjectList = commonService.getCodeListPerCodeGroup(
            appConstants.codeGroup.subject
        );
    }

    function setSelectedOption(option) {
        console.log("selected option : " + option);
        console.log("current optoin : " + vm.selectedOption);
        vm.selectedOption = option;
    }

    function showDataFilters() {
        console.log(vm.dataFilter);
    }

    function getGrades() {
        $scope.gradeList = commonService.getCodeListPerCodeGroup(
            appConstants.codeGroup.grade
        );
    }

    function filterByUploader(id) {
        vm.uploadedBy = id;
        fetchNewsList();
    }

    function setAscending() {
        vm.orderByKey = "createdOn";
    }

    function setDescending() {
        vm.orderByKey = "-createdOn";
    }
}
