/* Add trainings
 * Created by : Dipak Pote
 * Date : 27 June-2017
 * 
 * */
angular.module("mitraPortal").controller("addTrainingController",
  ['$scope', '$location', '$log', '$window', '$state', '$http', '$uibModal', 'appUtils', 'appConstants', 'trainingService', 'commonService', '$filter',
  function($scope, $location, $log, $window, $state, $http, $uibModal, appUtils, appConstants, trainingService, commonService, filter) {


    $scope.inputs= {}
    
    $scope.location = "STATE"; // can be "STATE" or "DISTRICT" or "BLOCK"

    $scope.selectedOption = "";
    
    $scope.setSelectedOption = function (selectedOption){
      if ($scope.selectedOption == selectedOption){
        $scope.selectedOption = null;
      }
      else{
        $scope.selectedOption = selectedOption;
      }
    }

    $scope.setDirty = function(form){
      angular.forEach(form.$error.required, function(field) {
        field.$dirty = true;
      });
    }

    // Save news in DRAFT.
    $scope.save = function() 
    {
      if(validateOptions())
      {
        $scope.statusCodeID = appConstants.code.contentOrNewsOrTrainingStatus_Created; 
        submit();
      }
    }

    // sendForReview news
    $scope.sendForReview = function() 
    {

      if(validateOptions())
      {
        $scope.statusCodeID = appConstants.code.contentOrNewsOrTrainingStatus_SentForReview;
        submit();
      }
    }
    
    // Publish news
    $scope.publish = function() 
    {

      if(validateOptions())
      {
        $scope.statusCodeID = appConstants.code.contentOrNewsOrTrainingStatus_Published;
        submit();
      }
    }
    


    // Validate details
    var validateOptions = function() 
    {
	      if ($scope.news.newsCategoryCodeID == appConstants.code.NewsCategory_MAA)
	      {           
	        return true;
	      }
      return true;
    }
    
    //Check string is empty or null
    var isUndefinedOrNull = function(val) 
    {
        return angular.isUndefined(val) || val === null 
    }
    

    var submit = function()
    {

      var fd = new FormData();
        for  (var key in $scope.news)
        {
          $log.debug(key);
          $log.debug($scope.news[key]);
          fd.append(key, $scope.news[key]);
        }
        
        fd.append("statusCodeID", $scope.statusCodeID);

        $log.debug($scope.newsImage1);
        $log.debug(!isUndefinedOrNull($scope.newsImage1));
        
        if(!isUndefinedOrNull($scope.newsImage1))
        	{fd.append('imageOne', $scope.newsImage1);}
        
        if(!isUndefinedOrNull($scope.newsImage2))
    		{fd.append('imageTwo', $scope.newsImage2);}
        
        if(!isUndefinedOrNull($scope.newsImage3))
    		{fd.append('imageThree', $scope.newsImage3);}
        
        if(!isUndefinedOrNull($scope.newsImage4))
    		{fd.append('imageFour', $scope.newsImage4);}
        
        if(!isUndefinedOrNull($scope.newsImage5))
    		{fd.append('imageFive', $scope.newsImage5);}        
        
		if(!isUndefinedOrNull($scope.myPDFFile))
			{fd.append('pdfFile', $scope.myPDFFile);}
		
		console.log("Actual date format............................");
		console.log($scope.news.publishDate);
		
		if(!isUndefinedOrNull($scope.news.publishDate))
		{fd.append('publishDate', $scope.news.publishDate);}
		
		
        var headers = { "authToken": appUtils.getFromCookies("token",""),
        'Content-Type': undefined};
        

        $http.post(appConstants.endpoint.baseUrl + "news/saveNews/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $scope.result= "uploaded successfully";
          setSuccessDetails();
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl :  'mitra/js/news-circulars/submittedSuccessView.html',
          })
          modalInstance.result.finally(function(){ 
            if ($scope.news.newsCategoryCodeID == appConstants.code.NewsCategory_MAA){
              $window.scrollTo(0, 0);
              $state.go('main.loggedIn.addNews');
            }

          });
          ;
        },
        function error(response){
          $log.debug("IN ERRORR RESPONSE");
          $log.debug(response);
          //$log.debug(response.data);
          //$log.debug(response.data.response_message);
          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
          $log.debug($scope.uploadErrorMessage);
          $scope.result ="Failed to add news.";
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl : appConstants.endpoint.baseUrl + 'mitra/js/news-circulars/submittedErrorView.html',
          });
        });
      }

      $scope.addNews = function () {
        newsService.addNews($scope.news, 
          addNewsSuccessCB,
          addNewsErrorCB);
        $scope.submitted = true;
      };

      $scope.getCodeFromCodeList = function(codeID){};

      // Check if the training category is State
      $scope.isTrainingTypeState = function (response) {
        return ($scope.training.trainingCategoryCodeID === appConstants.code.trainingCategory);
      }

      // Set training category 
      $scope.trainingTypeOnClick = function (selectedTrainingTypeCodeID) {
        $scope.training.trainingCategoryCodeID = selectedTrainingTypeCodeID;  
      }

      //set Selected State
      $scope.setSelectedState = function (selectedStateCodeID) {
          $scope.selectedStateCodeID = selectedStateCodeID;  
        }
      
      // Set training category 
      $scope.setSelectedDistrict = function (selectedDistrictID) {
        $scope.selectedDistrictCodeID = selectedDistrictID;  
        getBlockList(selectedDistrictID);
      }
      
      // Set training block 
      $scope.setSelectedBlock = function (selectedBlockID) {
        $scope.selectedBlockCodeID = selectedBlockID;  
      }
      
      // set trainer setSelectedTrainer
      $scope.setSelectedTrainer = function (selectedTrainerID,engTrainer,marTrainer) 
      {
    	  console.log("selectedTrainerID:" + selectedTrainerID);
          $scope.selectedTrainerCodeID = selectedTrainerID; 
          $scope.engTrainer = engTrainer;
          $scope.marTrainer = marTrainer;
        }
      
      $scope.isTrainingTypeSelected = function (trainingTypeCodeID) {
        return ($scope.training.trainingCategoryCodeID === trainingTypeCodeID);
      }

      // Get training type/category.
      var getTrainingTypes = function () {
        $scope.trainingTypeList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.trainingCategory
          );
        var icons = ["LOGO HERE"];
        for (var i=0; i<$scope.trainingTypeList.length;i++){
          $scope.trainingTypeList[i].icon = icons[i];
        }
      };


      // Get state for training
      var getState = function () {
        $scope.stateList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.state
          );
      };
      
      // Get district for training
      var getDistrict = function () {
        $scope.districtList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.district
          );
      };
      
      // Get block for training
      var getBlock = function () {
        $scope.blockList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.block
          );
      };

      // Get block list from district
      function getBlockList(districtCodeID) {
          if(districtCodeID) {
              trainingService.getBlockList(districtCodeID, fetchedBlockList, failureInFetchingBlockList);
          }
      }

      // Success blockList
      function fetchedBlockList(response) {
          console.log(response);
          $scope.blockList = response.data;
      }
      
      // Failed blockList
      function failureInFetchingBlockList(response) {
          console.log(response);
      }
      
      // Get trainer list
      function getTrainerList() 
      {
    	  trainingService.getTrainerList(fetchedTrainerList, failureInFetchingTrainerList);
      }

      // Success trainerList
      function fetchedTrainerList(response) {
          console.log(response);
          $scope.trainerList = response.data;
      }
      
      // Failed trainerList
      function failureInFetchingTrainerList(response) {
          console.log(response);
      }
      
      // Add alternative file
      $scope.addAlternateEvent = function () {
    	  
    	  var newEle = angular.element("<div class='gradient bottom-shadow' style='cursor: pointer;'>");
    	   // var newEle = angular.element("<div ng-include='webPortal/js/training/addAlternateTrainingView.html'></div>");
    	    var target = document.getElementById('alternateEventID');
    	    angular.element(target).append(newEle);
      }
      
     
      var populateDropDowns = function() {
    	getTrainingTypes();
    	getDistrict();
    	getState();
    	getBlock();
    	getBlockList();
    	getTrainerList();
      };

      var setSuccessDetails = function() {
        $scope.success = {};
        
        $scope.success.uploaderName = "self";

        if ($scope.statusCodeID == appConstants.code.contentOrNewsOrTrainingStatus_Created)
        {
          $scope.success.message = "Saved To Drafts";
        }
        else if ($scope.statusCodeID == appConstants.code.contentOrNewsOrTrainingStatus_SentForReview)
        {
            $scope.success.message = "Sent For Review";
        }
        else if ($scope.statusCodeID == appConstants.code.contentOrNewsOrTrainingStatus_Published)
        {
            $scope.success.message = "Published";
        }
        
      }


      $scope.$on('codesAvailable', function(event,data){
        populateDropDowns();
      });
      

      var init = function () {
        $scope.submitted = false;
        $scope.training = {};
        $scope.errorMessage = "";
        $log.debug("IN init");
        populateDropDowns();
        
        $scope.training.publishDate = new Date();
        console.log($scope.training.publishDate);
        
      };

      init();
      getTrainingTypes();
    }]);
;
