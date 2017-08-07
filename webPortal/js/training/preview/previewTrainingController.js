angular.module("mitraPortal").controller("previewTrainingController",
    ['$scope', '$stateParams', '$state', '$window', '$log', '$http','$uibModal', 'appUtils', 'appConstants', 'commonService',
        'trainingService','$animate',
        function ($scope, $stateParams, $state, $window, $log, $http, $uibModal, appUtils, appConstants, commonService,trainingService,$animate) {

      $scope.objNews = [];
  	  $scope.myInterval = 0;
	  $animate.enabled(true);  
	  $scope.myInterval = 3000;
	  $scope.myDate;
	  $scope.newFile = [];
	  
	  //$stateParams.contentType;
	  $scope.calledFromAdmin = $stateParams.calledFromAdmin;
	 
    // Get training type/category.
    var getTrainingTypes = function () {
    	console.log("IN getTrainingTypes");
      $scope.trainingTypeList = commonService.getCodeListPerCodeGroup(
        appConstants.codeGroup.trainingCategory
        );
      var icons = ["LOGO HERE"];
      for (var i=0; i<$scope.trainingTypeList.length;i++){
        $scope.trainingTypeList[i].icon = icons[i];
      }
    };
    
    // set trainer setSelectedTrainer
    $scope.setSelectedTrainerDetails = function (selectedTrainerID,engTrainer,marTrainer,index) 
    {
  	  
        $scope.selectedTrainerCodeID = selectedTrainerID; 
        $scope.items[index].engTrainer = engTrainer;
        $scope.items[index].marTrainer = marTrainer;
        $scope.items[index].selectedTrainerCodeID = selectedTrainerID;

     }
    
    //set Selected State
    $scope.setSelectedStateDetails = function (selectedStateCodeID,index) {
        $scope.items[index].selectedStateCodeID = selectedStateCodeID;
      }
    
    // Set training category 
    $scope.setSelectedDistrictDetails = function (selectedDistrictID,index) {
  	    $scope.items[index].selectedBlockCodeID = selectedDistrictID;
        getBlockList(selectedDistrictID);
    }
    
    // Set training block 
    $scope.setSelectedBlockDetails = function (selectedBlockID,index) {
  	 // $scope.items[index].selectedBlockCodeID = selectedBlockID;
    }
	
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
    
    // Get display block for training
    var getDisplayBlock = function () {
      $scope.displayBlockList = commonService.getCodeListPerCodeGroup(
        appConstants.codeGroup.block
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
        $scope.displayBlockList = response.data;
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
        $scope.trainerList = response.data;
    }
    
    // Failed trainerList
    function failureInFetchingTrainerList(response) {
        console.log(response);
    }
	
    // Create array and push the alternative events into the array...and then call to the API.
    $scope.items = [];
    
    $scope.add = function (eventID,eventDetailID,selectedTrainerCodeID,engTrainer,marTrainer,publishDate,selectedStateCodeID,selectedDistrictCodeID,selectedBlockCodeID,engLocation,marLocation,isEditable) {
  	  
        $scope.items.push({ 
          eventID : eventID,
      	  eventDetailID : eventDetailID,
          selectedTrainerCodeID: selectedTrainerCodeID,
      	  engTrainer: engTrainer,
      	  marTrainer: marTrainer,
      	  publishDate: publishDate,
      	  selectedStateCodeID:selectedStateCodeID,
      	  selectedDistrictCodeID:selectedDistrictCodeID,
      	  selectedBlockCodeID:selectedBlockCodeID,
      	  engLocation: engLocation,
      	  marLocation: marLocation,
      	  isEditable: isEditable
        });
        
      }
	
	// delete alternative events
    $scope.deleteAlternativeEvent = function (index,eventDetailsID)
    {
    	// get count for number of user attending the event.
    	getUserCountForEvent(eventDetailsID);
        if (confirm($scope.EventAttendingUserCount + " User's are attending this event. Do you want to delete this?")) {
            alert("Event successfully deleted");
            //delete from the array...
            $scope.items.splice(index, 1); 
            
            //delete from DB
            deleteAlternativeEvent(eventDetailsID); 
        }
    	
     }
    
    //update the training details i.e Allow to edit only training description and add new alternative events
    $scope.updateTrainingDeails = function()
    {
    	submit();
    }
	
    // add new events or update the event info.
    var submit = function()
    {
      var fd = new FormData();
      
        fd.append("eventID", $stateParams.eventID);
        fd.append("categoryCodeID", $scope.training.categoryCodeID);
        fd.append("marEventTitle", $scope.training.marEventTitle);  
        fd.append("engEventTitle", $scope.training.engEventTitle);
        
        fd.append("marEventDescription", $scope.training.marEventDescription);  
        fd.append("engEventDescription", $scope.training.engEventDescription);

        var headers = { "authToken": appUtils.getFromCookies("token",""),
        'Content-Type': undefined};
        
        $http.post(appConstants.endpoint.baseUrl + "events/addEvent/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $scope.result= "Event added successfully";
          console.log(response.data);

          if (((response.data.data)[0].eventID) > 0){
        	 saveAlternateEvents((response.data.data)[0].eventID);
        }
          
	        $scope.result= "uploaded successfully";
	        setSuccessDetails();
	        var modalInstance = $uibModal.open({
	          url: 'result',
	          scope: $scope,
	          templateUrl : appConstants.siteName.mitraSiteName + '/js/training/submittedSuccessView.html',
	        })
	        modalInstance.result.finally(function(){ 
	            $window.scrollTo(0, 0);
	            $state.go('main.loggedIn.trainingList');
	        });
         
          
        },
        function error(response){
          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
          $scope.result ="Failed to add training.";

          $scope.uploadErrorMessage = "Failed to add training.";
          $scope.result ="Failed to add training.";
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl : appConstants.endpoint.baseUrl + appConstants.siteName.mitraSiteName +  '/js/training/submittedErrorView.html',
          });
          
          
        });
      }
    
    var setSuccessDetails = function() 
    {
        $scope.success = {};
        $scope.success.title = $scope.training.engEventTitle;
        $scope.success.uploaderName = "self";
        $scope.success.message = "Training details saved successfully";
     }
	
	// Save alternative events...
    var saveAlternateEvents = function(eventID)
    {
    	for (var i=0; i<$scope.items.length;i++)
    	{
    		// Add new alternative event.
    		if($scope.items[i].eventDetailID == '0')
    		{
	    	    var fd = new FormData();
	
	            fd.append("eventID", eventID);
	            
	            if(!isUndefinedOrNull($scope.items[i].publishDate))
	            	{fd.append('date', $scope.items[i].publishDate);}
	            
	            if(!isUndefinedOrNull($scope.items[i].selectedStateCodeID))
	        		{fd.append('stateCodeID', $scope.items[i].selectedStateCodeID);}   
	            
	            if(!isUndefinedOrNull($scope.items[i].selectedDistrictCodeID))
	        		{fd.append('districtCodeID', $scope.items[i].selectedDistrictCodeID);}
	            
	            if(!isUndefinedOrNull($scope.items[i].selectedBlockCodeID))
	        		{fd.append('blockCodeID', $scope.items[i].selectedBlockCodeID);}
	            
	            if(!isUndefinedOrNull($scope.items[i].engLocation))
	        		{fd.append('engLocation', $scope.items[i].engLocation);} 
	            
	            if(!isUndefinedOrNull($scope.items[i].marLocation))
	    		{fd.append('marLocation', $scope.items[i].marLocation);}  
	            
	            if(!isUndefinedOrNull($scope.items[i].marTrainer))
	    		{fd.append('marTrainer', $scope.items[i].marTrainer);}  
	            
	            if(!isUndefinedOrNull($scope.items[i].engTrainer))
	    		{fd.append('engTrainer', $scope.items[i].engTrainer);}  
	            
	    		if(!isUndefinedOrNull(appConstants.code.contentOrNewsOrTrainingStatus_Published))
	    			{fd.append('statusCodeID', appConstants.code.contentOrNewsOrTrainingStatus_Published);}
	    		
	            var headers = { "authToken": appUtils.getFromCookies("token",""),
	            'Content-Type': undefined};
	            
	            $http.post(appConstants.endpoint.baseUrl + "events/addAlternateEvent/", fd, {
	              transformRequest: angular.identity,
	              headers: headers
	            })
	            .then (function success(response){
	            	$scope.trainingSaveedSuccessfully = true;
	            	console.log("Success........");
	            },
	            function error(response){
	              $scope.trainingSaveedSuccessfully = false;
	              $scope.uploadErrorMessage = "Failed to add training.";
	              $scope.result ="Failed to add training.";
	              var modalInstance = $uibModal.open({
	                url: 'result',
	                scope: $scope,
	                templateUrl : appConstants.endpoint.baseUrl + appConstants.siteName.mitraSiteName +  '/js/training/submittedErrorView.html',
	              });
	            });
          
    		}
            //end for loop
    	   }
    }
    
    // Get user count for the specific event.
    var getUserCountForEvent = function(eventDetailID)
    {

      var fd = new FormData();
      
        fd.append("eventDetailID", eventDetailID);
       
        var headers = { "authToken": appUtils.getFromCookies("token",""),
        'Content-Type': undefined};
        
        $http.post(appConstants.endpoint.baseUrl + "events/getUserCountForEvent/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $scope.result= "Event added successfully";
          console.log(response.data);
          $scope.EventAttendingUserCount = (response.data).data.userCount; 
        },
        function error(response){
          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
          $scope.result ="Failed to add training.";
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl : appConstants.endpoint.baseUrl + 'mitra/js/news-circulars/submittedErrorView.html',
          });
        });
      }
	
    //delete single event...
    var deleteAlternativeEvent = function(eventDetailID)
    {

      var fd = new FormData();
      
        fd.append("eventDetailID", eventDetailID);
       
        var headers = { "authToken": appUtils.getFromCookies("token",""),
        'Content-Type': undefined};
        
        $http.post(appConstants.endpoint.baseUrl + "events/deleteAlternateEvent/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $scope.result= "Event deleted successfully successfully";
          console.log(response.data);
        },
        function error(response){
          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
          $log.debug($scope.uploadErrorMessage);
          $scope.result ="Failed to add training.";
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl : appConstants.endpoint.baseUrl + 'mitra/js/news-circulars/submittedErrorView.html',
          });
        });
      }
    	
    $scope.inputs = {};
    $scope.isAdmin = appUtils.isAdmin();
    $scope.isTeacher = appUtils.isTeacher();

    $log.debug($scope.isAdmin, $scope.isTeacher, appUtils.isAdmin(), appUtils.isTeacher());

    $scope.mode = "PREVIEW"; // can be "EDIT" or "PPREVIEW" or "GIVE FEEDBACK"
    $scope.news = {};
    $scope.newsEditable = false;

            
        $scope.setDirty = function (form) {
            angular.forEach(form.$error.required, function (field) {
                field.$dirty = true;
            });
        };


        $scope.setSelectedOption = function (selectedOption,index) {
            if ($scope.mode == 'EDIT') {
                if ($scope.selectedOption == selectedOption) {
                    $scope.selectedOption = null;
                }
                else {
                    $scope.selectedOption = selectedOption + index;
                }
            }
        };
        

        $scope.setMode = function (mode) {
            if ($scope.mode == 'EDIT' && mode == 'PREVIEW' && $scope.originalContent) 
            {
                $scope.content = JSON.parse(JSON.stringify($scope.originalContent)); //deepcopy
            }
            $window.scrollTo(0, 0);
            $scope.mode = mode;
        };

        //publish the news
        $scope.publish = function () {
            var nextState = null;
            nextState = 'main.loggedIn.newsList';
            $state.go(nextState);
        };
        
        //Check string is empty or null
        var isUndefinedOrNull = function(val) 
        {
            return angular.isUndefined(val) || val === null 
        }
        
        $scope.selectedImageNumber = "";
        
            // Fetch training details in both languages
            var fetchTrainingDetails = function () 
            {
                var options = {};
                var data = {"eventID": $stateParams.eventID};
                options.data = data;
                options.url = 'events/alternateTrainingDetailList/';
                options.headers = {"authToken": appUtils.getFromCookies("token", "")};

                appUtils.ajax(options,
                    function (responseBody) {
                        //get news set
                        $scope.trainings = responseBody.data;
                        $scope.objNews = $scope.news;
                        
                        for(var i = 0 ; i < $scope.trainings.length; i++) 
                        {
                        	console.log("fetchTrainingDetailsfetchTrainingDetails");
                        	console.log("$scope.$scope.trainings.date" + $scope.trainings[i].date);
                        	$scope.trainings[i].publishDate = moment($scope.trainings[i].date).toDate();
                        	console.log("$scope.trainings[i].publishDate:" + $scope.trainings[i].publishDate);
                        	console.log($scope.trainings[i].publishDate);
                        	$scope.add($scope.trainings[i].eventID,$scope.trainings[i].eventDetailID,$scope.trainings[i].eventDetailID,$scope.trainings[i].engTrainer,$scope.trainings[i].marTrainer,$scope.trainings[i].publishDate,$scope.trainings[i].stateCodeID,$scope.trainings[i].districtCodeID,$scope.trainings[i].blockCodeID,$scope.trainings[i].engLocation,$scope.trainings[i].marLocation,false)
                        
                        	$scope.training.engEventTitle = $scope.trainings[i].engEventTitle;
                        	$scope.training.marEventTitle = $scope.trainings[i].marEventTitle;
                        	
                        	$scope.training.engEventDescription = $scope.trainings[i].engEventDescription;
                        	$scope.training.marEventDescription = $scope.trainings[i].marEventDescription;
                        	
                        	$scope.training.categoryCodeID = $scope.trainings[i].categoryCodeID;

                        }
                        
                    },
                    function (responseBody) {
                        $log.debug(responseBody);
                    }
                );
            };

            
            // fetch the code list and populate the dropdowns
            var populateDropDowns = function () {
                $scope.NewsCategoryList = commonService.getCodeListPerCodeGroup(
                    appConstants.codeGroup.NewsCategory
                );
                $scope.departmentList = commonService.getCodeListPerCodeGroup(
                    appConstants.codeGroup.department
                );
                $scope.importanceList = commonService.getCodeListPerCodeGroup(
                    appConstants.codeGroup.importance
                );
                
            	getDistrict();
            	getState();
            	getBlock();
            //	getDisplayBlock();
            //	getBlockList();
            	getTrainerList();
            };

            $scope.$on('codesAvailable', function (event, data) {
                populateDropDowns();
                fetchTrainingDetails();
            });
            
            var init = function () {
            	
            	$scope.EventAttendingUserCount = 0;
            	$scope.stateparamEventID = $stateParams.eventID;
                $scope.submitted = false;
                $scope.news = {"newsID": 0, "newsCategoryCodeID": 0};
                $scope.training = {};
                $scope.trainingCategoryState = appConstants.code.trainingCategory_State;
                $scope.errorMessage = "";
                fetchTrainingDetails();
                populateDropDowns();
                getTrainingTypes();
            	  
            };

            // Call to the init function.
            init();
            getTrainingTypes();
            //$scope.add();
        }
    ]);
