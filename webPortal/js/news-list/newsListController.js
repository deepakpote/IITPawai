angular.module("mitraPortal").controller("newsListController", newsListController);

newsListController.$inject = ['newsListService','commonService','$scope','appConstants','$filter', '$state', 'appUtils' , '$log'];

function newsListController(newsListService,commonService,$scope,appConstants,$filter,$state,appUtils,$log) {

    var vm = this;
    vm.newsCategoryCodeID = 115100;
    vm.departmentCodeID = setDepartment;
    vm.publishFromDate = setPublishFromDate;
    vm.publishToDate = setPublishToDate;
    vm.status = 114102;
    vm.fileType = 108100;
    vm.data = {};
    vm.goToPreview = goToPreview;
    vm.setDepartment = setDepartment;
    vm.setPublishFromDate = setPublishFromDate; //
    vm.setPublishToDate = setPublishToDate;
    vm.fetchSavedNews = fetchSavedNews;
    vm.selectedOption = "";
    vm.setSelectedOption = setSelectedOption;
    vm.dataFilter = {
        "subjectCodeIDs" : "",
        "gradeCodeIDs" : ""
    };

    vm.orderByKey = '';
    vm.isAdmin = appUtils.isAdmin();
    vm.isfetchSavedNews = false;
    if(!vm.isAdmin) {
        vm.status = 114100;
    }
 
       
    userSeenNewsList = "";
    fetchNewsList();

    
    //set news department
    function setDepartment(departmentCodeID) {
    	console.log(departmentCodeID);
        vm.departmentCodeID = departmentCodeID;
        fetchNewsList();
    }
    
 //setPublishFromDate
    function setPublishFromDate(PublishFromDate) {
    	console.log(PublishFromDate);
    	vm.publishFromDate = PublishFromDate;
        fetchNewsList();
    }
    
    //setPublishToDate
    function setPublishToDate(publishToDate) {	
        vm.publishToDate = publishToDate;
        fetchNewsList();
    }
    
    //Fetch saved news of the users
    function fetchSavedNews() {
        vm.isfetchSavedNews = true;
        $scope.isfetchSavedNews = true;
        //fetchNewsList();
        fetchUserNewsList();
        vm.isfetchSavedNews = false;
    }

    function goToPreview(newsList){
        $state.go('main.loggedIn.previewNews',
            {'newsID' : newsList.news});
        //Save newsID in userSeenNews
        var newSeenNewsID = appUtils.getFromCookies("userSeenNewsIDs","");
        appUtils.saveToCookies("userSeenNewsIDs", newSeenNewsID + "," + newsList.news );
        console.log(appUtils.getFromCookies("userSeenNewsIDs",""));
    }

    function fetchNewsList() {
        newsListService.fetch(vm.newsCategoryCodeID, vm.departmentCodeID, vm.publishFromDate,vm.publishToDate ,onSuccess, onFailure);
        function onSuccess(response) {
            var objnews = response.data;
            console.log("onSuccess Response : " + objnews);
            
            
            for(var i = 0 ; i < objnews.length ; i ++) {
            	var formatedDate = "";
            	var newDate = ";"
            	var singleImageURL = "";
            	var objDate = "";
                var news = objnews[i];
                objDate = news.publishDate.split(" ");
                var newFormatedDate = objDate[0].split("-");
                news.formatedDate = newFormatedDate[2]+ '-' + newFormatedDate[1]+ '-' + newFormatedDate[0]; 
                
//                $scope.newDate = moment(news.publishDate).toDate('dd-mm-yyyy');
//                console.log("newDatenewDate: " + $scope.newDate);
//                $scope.newDate= $filter('date')(news.publishDate, 'short');
//                console.log("newDatenewDate222: " + $scope.newDate);
                
                
                if(news.imageURL != "")
            	{
	            	var objURLs = "";
	            	objURLs = news.imageURL.split(",");
	            	news.singleImageURL = objURLs[0];
            	}

                var newsListIDs = appUtils.getFromCookies("userSeenNewsIDs","").split(",");

                
              for (var j = 0 ; j < newsListIDs.length ; j++) 
              {
              	if(news.news == newsListIDs[j])
              		{
              		news.seen = 1; 
              		console.log("Item Exists");
              		}
              }
              
              console.log("isfetchSavedNews:" + vm.isfetchSavedNews);  
            }
            
            getDepartments();
            vm.data = objnews;
        }
        function onFailure(response) {
        	console.log("onFailure Response : " + response);
        }
        
    }
    
    
    
    function fetchUserNewsList() {
        newsListService.fetchUsersNews(vm.newsCategoryCodeID, vm.departmentCodeID, vm.publishFromDate,vm.publishToDate ,onSuccess, onFailure);
        function onSuccess(response) {
            var objnews = response.data;
            console.log("onSuccess Response : " + objnews);
            
            
            for(var i = 0 ; i < objnews.length ; i ++) {
            	var formatedDate = "";
            	var singleImageURL = "";
            	var objDate = "";
                var news = objnews[i];
                objDate = news.publishDate.split(" ");
                news.formatedDate = objDate[0]; 	
                
                if(news.imageURL != "")
            	{
	            	var objURLs = "";
	            	objURLs = news.imageURL.split(",");
	            	news.singleImageURL = objURLs[0];
            	}

                var newsListIDs = appUtils.getFromCookies("userSeenNewsIDs","").split(",");

                
              for (var j = 0 ; j < newsListIDs.length ; j++) 
              {
              	if(news.news == newsListIDs[j])
              		{
              		news.seen = 1; 
              		console.log("Item Exists");
              		}
              }
              
              console.log("isfetchSavedNews:" + vm.isfetchSavedNews);
  
            }
            
            getDepartments();
            vm.data = objnews;
        }
        function onFailure(response) {
        	console.log("onFailure Response : " + response);
        }
        
    }

    // Get departments for news
    var getDepartments = function () {
      $scope.departmentList = commonService.getCodeListPerCodeGroup(
        appConstants.codeGroup.department
        );
    };
    
    // Get user seen newsID
    var getUserSeenNewsID = function () {
    	var userSeenNewsIDs = appUtils.getFromCookies("userSeenNewsIDs","");
        userSeenNewsList = userSeenNewsIDs;
    };


    function setSelectedOption(option) {
        console.log("selected option : " + option);
        console.log("current optoin : " + vm.selectedOption);
        vm.selectedOption = option;
    }

}


