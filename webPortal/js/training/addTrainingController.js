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
    $scope.trainingSaveedSuccessfully = false;
    
    
    
    $scope.setSelectedOption = function (selectedOption,index){
      if ($scope.selectedOption == selectedOption){
        $scope.selectedOption = null;
      }
      else{
        $scope.selectedOption = selectedOption + index;
      }
    }

    $scope.setDirty = function(form){
      angular.forEach(form.$error.required, function(field) {
        field.$dirty = true;
      });
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

        fd.append("categoryCodeID", $scope.training.trainingCategoryCodeID);
        
        fd.append("marEventTitle", $scope.training.marTrainingTitle);
        fd.append("engEventTitle", $scope.training.engTrainingTitle);
        
        fd.append("marEventDescription", $scope.training.marDescription);
        fd.append("engEventDescription", $scope.training.engDescription);

        var headers = { "authToken": appUtils.getFromCookies("token",""),
        'Content-Type': undefined};
        

        $http.post(appConstants.endpoint.baseUrl + "events/addEvent/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $scope.result= "Event added successfully";
          //setSuccessDetails()
          console.log(response.data);
          console.log("EventID:" + (response.data.data)[0].eventID);
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
          $scope.uploadErrorMessage = "Failed to add training.";
          $scope.result ="Failed to add training.";
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl : appConstants.endpoint.baseUrl + appConstants.siteName.mitraSiteName +  '/js/training/submittedErrorView.html',
          });
          
        });
      }
    
    var saveAlternateEvents = function(eventID)
    {
    	
    	for (var i=0; i<$scope.items.length;i++)
    	{
    		console.log("IN FOR LOOP");
    	    console.log( $scope.items[i].publishDate);
    	        
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
              //$scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
              $scope.uploadErrorMessage = "Failed to add training.";
              $scope.result ="Failed to add training.";
              var modalInstance = $uibModal.open({
                url: 'result',
                scope: $scope,
                templateUrl : appConstants.endpoint.baseUrl + appConstants.siteName.mitraSiteName +  '/js/training/submittedErrorView.html',
              });
            });
          
    	   }
    	
//    	if($scope.trainingSaveedSuccessfully == true)
//    		{
//		        $scope.result= "uploaded successfully";
//		        setSuccessDetails();
//		        var modalInstance = $uibModal.open({
//		          url: 'result',
//		          scope: $scope,
//		          templateUrl : appConstants.siteName.mitraSiteName + '/js/training/submittedSuccessView.html',
//		        })
//		        modalInstance.result.finally(function(){ 
//		            $window.scrollTo(0, 0);
//		            $state.go('main.loggedIn.home');
//		        });
//    		}
//    	else
//    		{
//	            $scope.uploadErrorMessage = "Failed to add training.";
//	            $scope.result ="Failed to add training.";
//	            var modalInstance = $uibModal.open({
//	              url: 'result',
//	              scope: $scope,
//	              templateUrl : appConstants.endpoint.baseUrl + appConstants.siteName.mitraSiteName +  '/js/training/submittedErrorView.html',
//	            });
//    		}
        
    
    }


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
    	  console.log("getBlockList response");
          console.log(response);
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
          console.log(response);
          $scope.trainerList = response.data;
          
          // Get unique trainer names
          $scope.trainerList = UniqueArraybyId($scope.trainerList ,"engTrainer");
          $scope.trainerList
      }
      
      // Get unique trainer names
      function UniqueArraybyId(collection, keyname) {
    	  console.log("IN UniqueArraybyId");
          var output = [], 
              keys = [];

          angular.forEach(collection, function(item) {
              var key = item[keyname];
              if(keys.indexOf(key) === -1) {
                  keys.push(key);
                  output.push(item);
              }
          });
          return output;
      };
      
      // Failed trainerList
      function failureInFetchingTrainerList(response) {
          console.log(response);
      }
      
 
      $scope.items = [];
      
      $scope.add = function () {
    	  
    	  console.log("IN ADD NEWWWWWWWW");
          $scope.items.push({ 
        	  selectedTrainerCodeID: null,
        	  engTrainer: "",
        	  marTrainer: "",
        	  publishDate: null,
        	  selectedStateCodeID:null,
        	  selectedDistrictCodeID:'',
        	  selectedBlockCodeID:0,
        	  engLocation: "",
        	  marLocation: ""
          });
          
        }
      
      $scope.deleteAlternativeEvent = function (index)
      {
    	  console.log("IN DELETE");
          $scope.items.splice(index, 1); 
       }
      
      // Publish news
      $scope.publish = function() 
      {

    	  console.log("$scope.items:");
    	  console.log($scope.items);
    	  submit();
      }
      
      // set trainer setSelectedTrainer
      $scope.setSelectedTrainerDetails = function (selectedTrainerID,engTrainer,marTrainer,index) 
      {
    	  
          $scope.selectedTrainerCodeID = selectedTrainerID; 
          $scope.items[index].engTrainer = engTrainer;
          $scope.items[index].marTrainer = marTrainer;
          $scope.items[index].selectedTrainerCodeID = selectedTrainerID;
          
          console.log("$scope.items[index].selectedTrainerCodeID:" + $scope.items[index].selectedTrainerCodeID );
          console.log($scope.items[index]);
       }
      
      //set Selected State
      $scope.setSelectedStateDetails = function (selectedStateCodeID,index) {
          $scope.items[index].selectedStateCodeID = selectedStateCodeID;
          getDistrict();
        }
      
      // Set training category 
      $scope.setSelectedDistrictDetails = function (selectedDistrictID,index) {
    	  $scope.items[index].selectedBlockCodeID = null;
          getBlockList(selectedDistrictID);
      }
      
      // Set training block 
      $scope.setSelectedBlockDetails = function (selectedBlockID,index) {
    	 // $scope.items[index].selectedBlockCodeID = selectedBlockID;
      }
        

      var populateDropDowns = function() {
    	getTrainingTypes();
    //	getDistrict();
    	getState();
    	getBlock();
   // 	getDisplayBlock();
   // 	getBlockList();
    	getTrainerList();
      };

      var setSuccessDetails = function() {
        $scope.success = {};
        $scope.success.title = $scope.training.marTrainingTitle;
        
        $scope.success.uploaderName = "self";

        $scope.success.message = "Training details saved successfully";
        
        }
        
      
      $scope.$on('codesAvailable', function(event,data){
        populateDropDowns();
      });
      
      function formatDate(date) {
    	  var monthNames = [
    	    "January", "February", "March",
    	    "April", "May", "June", "July",
    	    "Aug", "September", "October",
    	    "November", "December"
    	  ];

    	  var day = date.getDate();
    	  var monthIndex = date.getMonth();
    	  var year = date.getFullYear();

    	  return year + '-' + monthNames[monthIndex] + '-' + day;
    	}
      

      var init = function () {
        $scope.submitted = false;
        $scope.training = {};
        $scope.errorMessage = "";
        $log.debug("IN init");
        populateDropDowns();
        $scope.add();
        $scope.training.publishDate = new Date();
        $scope.minimumDate = String(formatDate(new Date()));
       
        $scope.min = new Date('2014-09-07');
        $scope.max = new Date('2016-09-07');
        
        $scope.training.trainingCategoryCodeID = appConstants.code.trainingCategory_State;
      };

      init();
      getTrainingTypes();
    }]);
;
