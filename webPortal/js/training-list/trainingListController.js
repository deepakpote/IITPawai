angular.module("mitraPortal").controller("trainingListController", trainingListController);

trainingListController.$inject = ['trainingListService','trainingService','commonService','$uibModal','$scope','$stateParams','$http','appConstants','$filter', '$state', 'appUtils' , '$log'];

function trainingListController(trainingListService,trainingService,commonService,$uibModal,$scope,$stateParams,$http,appConstants,$filter,$state,appUtils,$log) {

    var vm = this;
    
    vm.getTrainerList = getTrainerList;
    vm.selectedTrainerCodeID = null;
    vm.selectedStateCodeID = null;
    vm.selectedDistrictCodeID = null;
    vm.selectedBlockCodeID = null;
    vm.getState = getState;
    vm.setSelectedDistrictDetails = setSelectedDistrictDetails;
    vm.setSelectedBlockDetails = setSelectedBlockDetails;
    vm.filterDate = null;
    vm.calendarDate = null;
    vm.setFilterDate = setFilterDate;
    vm.getAlternateTrainingList = getAlternateTrainingList;
    vm.trainer = null;
    vm.setSelectedTrainerDetails = setSelectedTrainerDetails;
    vm.setTrainingListForMonth = setTrainingListForMonth;
    vm.resetTrainingFilter = resetTrainingFilter;
    
    vm.calledFromAdmin = $stateParams.calledFromAdmin;
    vm.setCalendarDetails = setCalendarDetails;
    $scope.currentDate = new Date();
    
    $scope.currentMonthName = String(($scope.currentDate)).split(" ");
    
    vm.showCalendarDetails = showCalendarDetails;
    
    $scope.IsEventSuccessfullyAttended = false;
    $scope.listOfCurrentMonthDates = [];
    
    
    vm.oneAtATime = true;
    
    vm.status = {
    	    isCustomHeaderOpen: false,
    	    isFirstOpen: true,
    	    isFirstDisabled: false
    	  };


    vm.status = 114102;
    vm.fileType = 108100;
    vm.data = {};
    vm.goToTrainingPreview = goToTrainingPreview;
    vm.selectedOption = "";
    vm.setSelectedOption = setSelectedOption;
    
    $scope.training = {};
    $scope.training.divider = "";
    
    $scope.location = "STATE"; // can be "STATE" or "DISTRICT" or "BLOCK"
    $scope.selectedOption = "";
    $scope.item = {};
    $scope.isOpen = false;
    $scope.oneAtATime = true;
    
    $scope.updateOpenStatus = function(){
        $scope.isOpen = $scope.groups.some(function(item){
          return item.isOpen;
        });
      }
    
    populateDropDowns();

    vm.orderByKey = '';
    vm.isAdmin = appUtils.isAdmin();
    vm.isfetchSavedNews = false;
    if(!vm.isAdmin) {
        vm.status = 114100;
    }
 
    //calendar code start here....********************************
    $scope.open = function (eventDetailID,eventName,eventID) {
    	attendEvent(eventDetailID,eventName);
    	//getAlternateTrainingList(eventID);
    }
    
    
    // Set training details.
    $scope.setTrainingDetails = function(objTrainingDetail)
    {
        $scope.training.divider = "|";
    	$scope.training.nameOfTraining = objTrainingDetail.eventTitle;
    	$scope.training.trainingDate = objTrainingDetail.newdate;
    	$scope.training.trainingPlace = commonService.getValueByCode(objTrainingDetail.blockCodeID)[0].codeNameEn;
    	$scope.training.trainingTrainer = objTrainingDetail.engTrainer;
    	$scope.training.trainingDescription = objTrainingDetail.eventDescription;
    	
    	$scope.training.myEventDetailID = objTrainingDetail.eventDetailID;
    	$scope.training.myEventName = objTrainingDetail.eventTitle;
    }
    
    $scope.resetTrainingDetails = function()
    {
    	
        $scope.training.divider = "";
        
    	$scope.training.nameOfTraining = "";
    	$scope.training.trainingDate = "";
    	$scope.training.trainingPlace = "";
    	$scope.training.trainingTrainer = "";
    	$scope.training.trainingDescription = "";
    }
    
    $scope.close = function () {
    	$uibModalInstance.dismiss('cancel');
    	};
    
    
    $scope.today = function() {
        $scope.dt = new Date();
      };
      $scope.today();

      $scope.clear = function () {
        $scope.dt = null;
      };

      // Disable weekend selection

      $scope.today = function() {
    	    $scope.dt = new Date();
    	  };
    	  $scope.today();

    	  $scope.clear = function() {
    	    $scope.dt = null;
    	  };

    	  $scope.options = {
    		showWeeks: false,
    	    customClass: getDayClass,
    	    minDate: new Date(),
    	    initDate: new Date()
    	    
    	  };
    	  
    	  
    	  // Disable weekend selection
    	  function disabled(data) {
    	    var date = data.date,
    	      mode = data.mode;
    	    return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
    	  }

    	  $scope.toggleMin = function() {
    	    $scope.options.minDate = $scope.options.minDate ? null : new Date();
    	  };

    	  $scope.toggleMin();

    	  $scope.setDate = function(year, month, day) {
    	    $scope.dt = new Date(year, month, day);
    	  };

    	  var tomorrow = new Date();
    	  tomorrow.setDate(tomorrow.getDate() + 1);
    	  var afterTomorrow = new Date(tomorrow);
    	  afterTomorrow.setDate(tomorrow.getDate() + 2);
    	  $scope.events = [
    	    {
    	      date: tomorrow,
    	      status: 'full'
    	    },
    	    {
    	      date: afterTomorrow,
    	      status: 'partially'
    	    }
    	  ];
    	  

              
    	  function getDayClass(data) {
    	    var date = data.date,
    	      mode = data.mode;
    	    if (mode === 'day') {
    	      var dayToCheck = new Date(date).setHours(0,0,0,0);

    	      for (var i = 0; i < $scope.events.length; i++) {
    	        var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

    	        if (dayToCheck === currentDay) {
    	          return $scope.events[i].status;
    	        }
    	      }
    	    }

    	    return '';
    	  }
    
	  function pad(d) {
		    return (d < 10) ? '0' + d.toString() : d.toString();
	  }
    	  
    // get training list for month
    	  
    	    var getTrainingListForMonth = function()
    	    {
    	    
    	      $scope.listOfCurrentMonthDates = [];

    	      var fd = new FormData();
    	      
    	      //trainingListForMonth
              if(!isUndefinedOrNull($scope.dt))
          	   {fd.append('trainingListForMonth', $scope.dt);}

    	        var headers = { "authToken": appUtils.getFromCookies("token",""),
    	        'Content-Type': undefined};
    	        
    	        $http.post(appConstants.endpoint.baseUrl + "events/trainingList/", fd, {
    	          transformRequest: angular.identity,
    	          headers: headers
    	        })
    	        .then (function success(response){
    	          console.log("Get event list successfully....IN getTrainingListForMonth");
    	          console.log(response.data);
    	          
    	          var objTrainings = response.data;
    	          $scope.objTrainingListForMonth = (response.data).data;
    	          $scope.training.countOfTraining = $scope.objTrainingListForMonth.length;


    	            for(var i = 0 ; i < $scope.objTrainingListForMonth.length ; i ++) {
    	            	var formatedDate = "";
    	            	var newDate = ";"
    	            	var singleImageURL = "";
    	            	var objDate = "";
    	            	
    	            	//2017-06-23T17:00:00
    	            	
    	                objDate = ($scope.objTrainingListForMonth[i].date).split("-");
    	                var MonthName = getMonthName(objDate[1]);

    	                $scope.objTrainingListForMonth[i].newdate = objDate[2].substring(0, 2) + '-' + MonthName + '-' + objDate[0]; 

    	                //Set district & block name..
    	                $scope.objTrainingListForMonth[i].place = commonService.getValueByCode($scope.objTrainingListForMonth[i].blockCodeID)[0].codeNameEn;
    	                
    	                $scope.listOfCurrentMonthDates.push(objDate[2].substring(0, 2));
    	            }   
    	         
    	        },
    	        function error(response){
    	          console.log("IN ERRORR RESPONSE");
    	          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
    	          $scope.result ="Failed to add training.";
    	        });
    	      }
    	    
    	    
    	    
    	    // get my training list
      	  
    	    var getMyTrainingList = function()
    	    {
    	    
    	      $scope.myTrainingList = [];

    	      var fd = new FormData();

    	        var headers = { "authToken": appUtils.getFromCookies("token",""),
    	        'Content-Type': undefined};
    	        
    	        $http.post(appConstants.endpoint.baseUrl + "events/myTrainingList/", fd, {
    	          transformRequest: angular.identity,
    	          headers: headers
    	        })
    	        .then (function success(response){
    	          console.log("Get event list successfully....IN getMyTrainingList");
    	          console.log(response.data);
    	          
    	          var objTrainings = response.data;
    	          $scope.objMyTrainingList = (response.data).data;

    	            for(var i = 0 ; i < $scope.objMyTrainingList.length ; i ++) 
    	            {
    	                $scope.myTrainingList.push($scope.objMyTrainingList[i].eventDetail);
    	            }   
    	            
    	        },
    	        function error(response){
    	          console.log("IN ERRORR RESPONSE");
    	          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
    	          $scope.result ="Failed to add training.";
    	        });
    	      }
    	    
    	    //Check string is empty or null
    	    var isUndefinedOrNull = function(val) 
    	    {
    	        return angular.isUndefined(val) || val === null 
    	    }
    	    
    	    // get all training
      	  
    	    var getAllTrainingList = function()
    	    {

    	      var fd = new FormData();
    	      
    	      //vm.filterDate
              if(!isUndefinedOrNull(vm.filterDate))
          	   {fd.append('date', vm.filterDate);}
              
    	      // Filter on district districtCodeID
              if(!isUndefinedOrNull(vm.selectedDistrictCodeID))
          	   {fd.append('districtCodeID', vm.selectedDistrictCodeID);}
              
    	      // Filter on blockCodeID
              if(!isUndefinedOrNull(vm.selectedBlockCodeID))
          	   {fd.append('blockCodeID', vm.selectedBlockCodeID);}
              
              //trainer
              if(!isUndefinedOrNull(vm.trainer))
         	   {fd.append('trainer', vm.trainer);}
              

    	        var headers = { "authToken": appUtils.getFromCookies("token",""),
    	        'Content-Type': undefined};
    	        
    	        $http.post(appConstants.endpoint.baseUrl + "events/trainingList/", fd, {
    	          transformRequest: angular.identity,
    	          headers: headers
    	        })
    	        .then (function success(response){
    	          console.log("Get event list successfully....");
    	          console.log(response.data);
    	          
    	          var objTrainings = response.data;
    	          $scope.objAllTrainingList = (response.data).data; 

    	            for(var i = 0 ; i < $scope.objAllTrainingList.length ; i ++) {
    	            	var formatedDate = "";
    	            	var newDate = ";"
    	            	var singleImageURL = "";
    	            	var objDate = "";
    	            	
    	            	//2017-06-23T17:00:00
    	            	
    	                objDate = ($scope.objAllTrainingList[i].date).split("-");
    	                var MonthName = getMonthName(objDate[1]);

    	                $scope.objAllTrainingList[i].newdate = objDate[2].substring(0, 2) + '-' + MonthName + '-' + objDate[0]; 

    	                //Set district & block name..
    	                $scope.objAllTrainingList[i].place = commonService.getValueByCode($scope.objAllTrainingList[i].blockCodeID)[0].codeNameEn;
    	            }   
    	         
    	        },
    	        function error(response){
    	          console.log("IN ERRORR RESPONSE");
    	          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
    	          $scope.result ="Failed to add training.";
    	        });
    	      }
    	  
    	    
    	    // get alternate training list..
      	  
    	    function getAlternateTrainingList(eventID)
    	    {
    	    	
    	        if($stateParams.calledFromAdmin == true)
            	{
            		$state.go('main.loggedIn.previewTrainingDetails',
                	      {'eventID' : eventID});
            	}	
    	        else
    	        	{
    	    	
		    	      var fd = new FormData();
		    	      
		    	      fd.append("eventID", eventID);
		
		    	        var headers = { "authToken": appUtils.getFromCookies("token",""),
		    	        'Content-Type': undefined};
		    	        
		    	        $http.post(appConstants.endpoint.baseUrl + "events/alternateTrainingList/", fd, {
		    	          transformRequest: angular.identity,
		    	          headers: headers
		    	        })
		    	        .then (function success(response){
		    	          console.log("Get alternate event list successfully....");
		    	          console.log(response.data);
		    	          
		    	          var objTrainings = response.data;
		    	          $scope.objAlternateTrainingList = (response.data).data;
		    	          console.log("!st event:" + $scope.objAlternateTrainingList[0]);  
		
		    	            for(var i = 0 ; i < $scope.objAlternateTrainingList.length ; i ++) {
		    	            	var formatedDate = "";
		    	            	var newDate = ";"
		    	            	var singleImageURL = "";
		    	            	var objDate = "";
		    	            	
		    	            	//2017-06-23T17:00:00
		    	            	
		    	                objDate = ($scope.objAlternateTrainingList[i].date).split("-");
		    	                var MonthName = getMonthName(objDate[1]);
		
		    	                $scope.objAlternateTrainingList[i].newdate = objDate[2].substring(0, 2) + '-' + MonthName + '-' + objDate[0]; 
		
		    	                //Set district & block name..
		    	                $scope.objAlternateTrainingList[i].place = commonService.getValueByCode($scope.objAlternateTrainingList[i].blockCodeID)[0].codeNameEn;
		    	            }   
		    	         
		    	        },
		    	        function error(response){
		    	          console.log("IN ERRORR RESPONSE");
		    	          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
		    	          $scope.result ="Failed to add training.";
		    	        });
		    	        
    	        	}
    	        
    	        
    	      }
    	  
    	    var attendEvent = function(eventDetailID,eventName)
    	    {

    	      var fd = new FormData();

    	        fd.append("eventDetailID", eventDetailID);
    	        
    	        var headers = { "authToken": appUtils.getFromCookies("token",""),
    	        'Content-Type': undefined};
    	        

    	        $http.post(appConstants.endpoint.baseUrl + "events/attendEvent/", fd, {
    	          transformRequest: angular.identity,
    	          headers: headers
    	        })
    	        .then (function success(response){
    	          console.log("Event attended successfully....");
    	          $scope.IsEventSuccessfullyAttended = true;
    	          console.log(response.data);
    	          
    	          $scope.success = {};
    	          $scope.success.eventName = "Training:" + eventName;
    	          
    	          if(response.data.response_message == 100100)
	        	  {
        	          $scope.success.eventName = "Training:" + eventName;
        	          $scope.success.secondaryText = "Above training added to My Training List"
	        	  }
    	          else if(response.data.response_message == 100147)
	        	  {
        	          $scope.success.eventName = "Training:" + eventName;
        	          $scope.success.secondaryText = "Above training is already added to My Training List"
	        	  }
    	          
    		        $scope.result= "uploaded successfully";

    		       // $scope.success = {};
    		        $scope.success.title = eventName;
    		        
    		        $scope.success.uploaderName = "self";

    		        $scope.success.message = $scope.success.secondaryText; // "Training details saved successfully";
    		        
    		        
    		        var modalInstance = $uibModal.open({
    		          url: 'result',
    		          scope: $scope,
    		          templateUrl : appConstants.siteName.mitraSiteName + '/js/training/submittedSuccessView.html',
    		        })
		        	
    	        },
    	        function error(response){
    	        	$scope.IsEventSuccessfullyAttended = false;
    	          console.log("IN ERRORR RESPONSE attendEvent");
    	          console.log(response.data);
    	          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
    	          $scope.result ="Failed to add training.";
    	        });
    	      }
    	  
    	  
    //calendar code ends here...*********************
    	    
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
    	    
    	    
	      // Get state for training
	      function getState() {
	        vm.stateList = commonService.getCodeListPerCodeGroup(
	          appConstants.codeGroup.state
	          );
	      };
	      
	      // Get district for training
	      function getDistrict() {
	        vm.districtList = commonService.getCodeListPerCodeGroup(
	          appConstants.codeGroup.district
	          );
	      };
	      
	      // Get display block for training
	      function getDisplayBlock() {
	        vm.displayBlockList = commonService.getCodeListPerCodeGroup(
	          appConstants.codeGroup.block
	          );
	      };
	      
	      // Get block for training
	      function getBlock() {
	        vm.blockList = commonService.getCodeListPerCodeGroup(
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
	          vm.displayBlockList = response.data;
	      }
	      
	      // Failed blockList
	      function failureInFetchingBlockList(response) {
	          console.log(response);
	      }
	      
	      // Get trainer list
	      function getTrainerList() 
	      {  console.log("IN GET TRAINING LIST");
	    	  trainingService.getTrainerList(fetchedTrainerList, failureInFetchingTrainerList);
	      }

	      // Success trainerList
	      function fetchedTrainerList(response) {
	          console.log(response);
	          vm.trainerList = response.data;
	          console.log("vm.trainerList");
	          console.log(vm.trainerList);
	      }
	      
	      // Failed trainerList
	      function failureInFetchingTrainerList(response) {
	          console.log(response);
	      }
	      
	      // Get blocks accordning to the district
	      function setSelectedBlockDetails(selectedBlockID) {
	    	  console.log("IN setSelectedBlockDetails");
	    	  vm.selectedBlockCodeID = selectedBlockID;
	          getAllTrainingList();
	      }
	      
	      // Get blocks accordning to the district
	      function setSelectedDistrictDetails(selectedDistrictID) {
	    	  console.log("IN setSelectedDistrictDetails");
	    	  vm.selectedDistrictCodeID = selectedDistrictID;
	    	  vm.selectedBlockCodeID = null;
	          getBlockList(selectedDistrictID);
	          getAllTrainingList();
	      }

	getTrainerList();
	getState();
    userSeenNewsList = "";
    getTrainingListForMonth();
    getMyTrainingList();
    getAllTrainingList();
    setCalendarDetails($scope.currentDate);


    function goToTrainingPreview(eventID)
    {


        console.log("IN goToTrainingPreview");  
        
        console.log("vm.calledFromAdmin:" + vm.calledFromAdmin);
        console.log("$stateParams.calledFromAdmin:" + $stateParams.calledFromAdmin); 
        
        if($stateParams.calledFromAdmin == true)
        	{
        		$state.go('main.loggedIn.previewTrainingDetails',
            	      {'eventID' : eventID});
        	}
    }
    
    function getMonthName (monthNumber) { 
        var monthNames = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ];
        return monthNames[monthNumber - 1];
    }

    function populateDropDowns() {
    	//getTrainingTypes(); 
    	getDistrict();
    	getState();
    	getBlock();
    	getDisplayBlock();
    	getBlockList();
    	getTrainerList();
      };

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
    
    //setPublishFromDate
    function setFilterDate(filterDate) {
    	vm.filterDate = filterDate;
    	getAllTrainingList();
    }


    function setSelectedOption(option) {
        vm.selectedOption = option;
    }
    
    // set trainer setSelectedTrainer
    function setSelectedTrainerDetails(selectedTrainer) 
    {
        vm.trainer = selectedTrainer; 
        getAllTrainingList();

     }
    
    function setTrainingListForMonth(selectedDate)
    {
    	getTrainingListForMonth();
    	$scope.resetTrainingDetails();
    }
    
    function resetTrainingFilter()
    {
    	vm.filterDate = null;
        vm.selectedDistrictCodeID = null;
        vm.selectedBlockCodeID = null;
        vm.selectedTrainerCodeID = null;
        $scope.training.filterDate = null;
        getAllTrainingList();
    }
    

    var startCount = 5;
    
    //Month is 1 based
    function daysInMonth(month,year) {
        return new Date(year, month, 0).getDate();
    }
    
    $scope.months = [
        {month : "Jan", monthNumber : "01"},
        {month : "Feb", monthNumber : "02"},
        {month : "Mar", monthNumber : "03"},
        {month : "Apr", monthNumber : "04"},
        {month : "May", monthNumber : "05"},
        {month : "Jun", monthNumber : "06"},
        {month : "Jul", monthNumber : "07"},
        {month : "Aug", monthNumber : "08"},
        {month : "Sep", monthNumber : "09"},
        {month : "Oct", monthNumber : "10"},
        {month : "Nov", monthNumber : "11"},
        {month : "Dec", monthNumber : "12"}
    ];
    
    //$scope.selectedMonth = $scope.months[0];
    $scope.MonthName
    $scope.selectedMonth = $scope.months.filter(function(x) {
        return x.month == $scope.currentMonthName[1];
    });
    
    $scope.selectedMonth = $scope.selectedMonth[0];
    
    $scope.years = [
        {year : "2015"},
        {year : "2016"},
        {year : "2017"},
        {year : "2018"},
        {year : "2019"},
        {year : "2020"},
        {year : "2021"},
        {year : "2022"},
        {year : "2023"},
        {year : "2024"},
        {year : "2025"},
        {year : "2026"},
    ];
    $scope.selectedYear2 = $scope.years[0];
    
    $scope.selectedYear = $scope.years.filter(function(x) {
        return x.year == "2017";
    });
    
    $scope.selectedYear= $scope.selectedYear[0];
    
    $scope.setSelectedMonth = function()
    {
    	//set date in this for mat & make call to setCalendarDetails function 
    	//Thu Aug 03 2017 14:13:30 GMT+0530 (India Standard Time)

    	if(!isUndefinedOrNull($scope.selectedYear.year))
    	{
    	 var formatDate = "Thu " + $scope.selectedMonth.month + " 01 " + $scope.selectedYear.year + " 14:13:30 GMT+0530 (India Standard Time)"
    	 console.log("formatDate" + formatDate);
    	 setCalendarDetails(formatDate);
    	 
    	 $scope.resetTrainingDetails();
    	}
    	
    }
    
    //Check string is empty or null
    var isUndefinedOrNull = function(val) 
    {
        return angular.isUndefined(val) || val === null 
    }
    
    $scope.setSelectedYear = function()
    {
    	
    	if(!isUndefinedOrNull($scope.selectedMonth.month))
    	{
    	 var formatDate = "Thu " + $scope.selectedMonth.month + " 01 " + $scope.selectedYear.year + " 14:13:30 GMT+0530 (India Standard Time)"
    	 setCalendarDetails(formatDate);
    	 
    	 $scope.resetTrainingDetails();
    	}
    	
    }
    
    function setCalendarDetails(idate)
    {
    	
    	var formattedDate = new Date(idate);
    	$scope.dt = formattedDate;
    	
    	
    	getTrainingListForMonth();
    	showCalendarDetails(formattedDate);
    	
    	
    }
    
    //$scope.previousMonthCount = 0;
    //$scope.nextMonthCount = 0;
    
    function showCalendarDetails(showDate)
    {
    	console.log("IN showCalendarDetails");
    	console.log(showDate);
    	var totalNoOfDay = daysInMonth((showDate.getMonth() + 1),showDate.getFullYear())
    	
    	var todaydate = new Date();
    	var previousMonthDays = daysInMonth((showDate.getMonth()),showDate.getFullYear())

    	
    	console.log("todaydate.getMonth()" + showDate.getMonth());
    	console.log("previousMonthDays:" + previousMonthDays);
    	console.log(previousMonthDays);
    	
        $scope.monthDays = [01, 02, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31];
        
        $scope.monthDays = $scope.monthDays.filter(function(x) {
            return x <= totalNoOfDay;
        });
        
    	var diffCount = getDiffCountFromDate(showDate)
    	$scope.previousMonthCount = diffCount;
    	//alert($scope.previousMonthCount);
    	
    	for(var v=0; v< diffCount; v++)
    		{
    		 $scope.monthDays.unshift(previousMonthDays);
    		 previousMonthDays = previousMonthDays -1;
    		}
    	
        console.log("$scope.monthDays.length:" + $scope.monthDays.length%7);
        var modValue = $scope.monthDays.length % 7;
        $scope.nextMonthCount = $scope.monthDays.length;
        //alert( $scope.monthDays.length);
        if(modValue != 0)
    	{
        	var startDay = 1;
        	for(var i = modValue; i < 7; i++)
        		{
        		$scope.monthDays.push(startDay);
        		startDay = startDay +1;
        		}
    	}

        var dates = [];

        for (var i = 0; i < $scope.monthDays.length; i++ )
        {
            if (i % 7 == 0)
            {
            dates.push([]);
            } 
            dates[dates.length-1].push($scope.monthDays[i]);
        }
        

               
        $scope.dates = dates;
    }
    
    function getDiffCountFromDate(getDate)
    {
      	$scope.weekDays = 
        {
        'Sun' : "0",
        'Mon' : "1",
        'Tue' : "2",
        'Wed' : "3",
        'Thu' : "4",
        'Fri' : "5",
        'Sat' : "6",
        'Sun' : "7"
        };
    	
      	var convertedDate = new Date(getDate);

        var firstDay = new Date(convertedDate.getFullYear(), convertedDate.getMonth(), 1);
        var objDay = (firstDay.toString()).split(" ");
        var diffCount = $scope.weekDays[objDay[0]];
        // alert($scope.weekDays[oobj[0]]);
        return diffCount;
    }
    
    $scope.showEventOnDay = function(day)
    {
    	if($scope.listOfCurrentMonthDates.indexOf(pad(day)) != -1)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}

    }
    
    $scope.checkValidDay = function(day,parentIndex,index)
    {
    	console.log("day :::"+ day);
    	console.log("parentIndex :::"+ parentIndex);
    	console.log("index :::"+ index);
    	var tt = (parentIndex * 7) + index + 1;
    	if(tt <= parseInt($scope.previousMonthCount))
    	{
    		return true;
    	}
    	else if(tt > parseInt($scope.nextMonthCount))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}

    }
    
    $scope.showCurrentDay = function(dayIndex)
    {
        var d = new Date();
        var CurrentDayIndex = d.getDay()
    	
    	if( CurrentDayIndex == dayIndex)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}

    }
    
    //isEventAttended
    $scope.isEventAttended = function(eventDetailID)
    {
    	if($scope.myTrainingList.indexOf(eventDetailID) != -1)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}

    }

}


