/* Save news to the draft or publish news
 * Created by : Dipak Pote
 * Date : 7-July-2017
 * 
 * */
angular.module("mitraPortal").controller("sendNotificationController",
  ['$scope', '$stateParams','$location', '$log', '$window', '$state', '$http', '$uibModal', 'appUtils', 'appConstants', 'sendNotificationService', 'commonService', '$filter',
  function($scope, $stateParams, $location, $log, $window, $state, $http, $uibModal, appUtils, appConstants, newsService, commonService, filter) {


    $scope.inputs= {}

    $scope.selectedOption = "";
    
    // Get value from stateparams and set to scope variable.
    $scope.objectID = $stateParams.objectID;
    $scope.contentType = $stateParams.contentType;
    $scope.notificationTypeCodeID  = appConstants.code.notificationType_Other;
    
    // Set selected options
    $scope.setSelectedOption = function (selectedOption){
      if ($scope.selectedOption == selectedOption){
        $scope.selectedOption = null;
      }
      else{
        $scope.selectedOption = selectedOption;
      }
    }

    // set dirty value.
    $scope.setDirty = function(form){
      angular.forEach(form.$error.required, function(field) {
        field.$dirty = true;
      });
    }
    
    // Go to the object details.. 1] Content details or News details
    $scope.goToObjectDetails = function()
    {
		if ($scope.contentType == appConstants.code.contentCategory_TeachingAid || $scope.contentType == appConstants.code.contentCategory_Selflearning) 
		{
	        $state.go('main.loggedIn.reviewContent',
	                {'contentID' : $scope.objectID});
		}
	else if ($scope.contentType == appConstants.code.NewsCategory_MAA)
		{
	        $state.go('main.loggedIn.previewNews',
	                {'newsID' : $scope.objectID});
		}
	else
		{
	        $state.go('main.loggedIn.home');
		}
    }


    // Send data notification
    $scope.sendNotification = function() 
    {
      if(validateOptions())
      {
        submit();
      }
    }

    // Validate send notification details
    var validateOptions = function() 
    {
	      if ($scope.notificationTypeCodeID != 0)
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
    
    // Send notification
    var submit = function()
    {

      var fd = new FormData();

        fd.append("notificationTypeCodeID", $scope.notificationTypeCodeID);
        fd.append("objectID", $scope.objectID); 
        fd.append("marTitle", $scope.notification.marNotificationTitle);
        fd.append("marText",  $scope.notification.marText);
        fd.append("engTitle", $scope.notification.engNotificationTitle);
        fd.append("engText",  $scope.notification.engText);

		console.log($scope.notification);
		
        var headers = { "authToken": appUtils.getFromCookies("token",""),
        'Content-Type': undefined};
        
        //$http.post("http://54.152.74.194:8000/user/sendDataNotificationsToAll/", fd, {
        $http.post(appConstants.endpoint.baseUrl + "user/sendDataNotificationsToAll/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $scope.result= "Notification sent successfully";
          console.log(response);
          setSuccessDetails();
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl : appConstants.siteName.mitraSiteName + '/js/notifications/submittedSuccessView.html',
          })
          modalInstance.result.finally(function(){ 
            if ($scope.contentType == appConstants.code.contentCategory_TeachingAid ||
            	$scope.contentType == appConstants.code.contentCategory_Selflearning){
              $window.scrollTo(0, 0);
              $state.go('main.loggedIn.reviewContent',{'contentID' : $scope.objectID});
            }
            else if($scope.contentType == appConstants.code.NewsCategory_MAA){
                $window.scrollTo(0, 0);
                $state.go('main.loggedIn.previewNews',{'newsID' : $scope.objectID});
              }
            else
            	{
                $window.scrollTo(0, 0);
                $state.go('main.loggedIn.teachingAids');
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
          $scope.result ="Failed to send notifications.";
          var modalInstance = $uibModal.open({
            url: 'result',
            scope: $scope,
            templateUrl : appConstants.endpoint.baseUrl + appConstants.siteName.mitraSiteName + '/js/notifications/submittedErrorView.html',
          });
        });
      }
    
	    // Fetch news details in both languages
	    var fetchNewsDetails = function (iNewsID) {
	        var options = {};
	        var data = {"newsID": iNewsID};
	        options.data = data;
	        options.url = 'news/newsDetail/';
	        options.headers = {"authToken": appUtils.getFromCookies("token", "")};
	
	        appUtils.ajax(options,
	            function (responseBody) {
	                //get news set
	        		$scope.notification.thumbnailUrl = "";
	                $scope.newsDetail = responseBody.data[0];
	                console.log("responseBody.data[0]:");
	                console.log(responseBody.data[0]);
                    $scope.notification.objectTitle = $scope.newsDetail.marNewsTitle;
                    $scope.notification.objectDetail3 = commonService.getValueByCode($scope.newsDetail.newsCategory)[0].codeNameEn;
	                
                	$scope.notification.objectDetail2 = commonService.getValueByCode($scope.newsDetail.department)[0].codeNameMr;
                    
                	var objDate = $scope.newsDetail.publishDate.split(" ");
                    var newFormatedDate = objDate[0].split("-");
                    var MonthName = getMonthName(newFormatedDate[1]);
                    $scope.notification.objectDetail1 = (newFormatedDate[2]).substring(0, 2) + '-' + MonthName + '-' + newFormatedDate[0]; 
                    console.log($scope.newsDetail.imageURL);
                    $scope.notification.thumbnailUrl = $scope.newsDetail.imageURL;
	            },
	            function (responseBody) {
	            	console.log(responseBody);
	            }
	        );
	    };
	    
	    // Fetch content details
        var fetchContentDetails = function (iContentID) {
            var options = {};
            var data = {"contentID": iContentID};
            options.data = data;
            options.url = 'content/contentDetail/';
            options.headers = {"authToken": appUtils.getFromCookies("token", "")};

            appUtils.ajax(options,
                function (responseBody) {
                    //get content set
                    $scope.contentDeails = responseBody.data[0]; 
                    
                    $scope.notification.objectTitle = $scope.contentDeails.marContentTitle;
                    $scope.notification.objectDetail3 = $scope.contentDeails.marAuthor;
                    
                    if($scope.contentDeails.contentTypeCodeID == appConstants.code.contentCategory_TeachingAid)
                    	{
                        	$scope.notification.objectDetail1 = commonService.getValueByCode($scope.contentDeails.subjectCodeID)[0].codeNameMr;
                        	$scope.notification.objectDetail2 = 'Grade ' + commonService.getValueByCode($scope.contentDeails.gradeCodeIDs)[0].codeNameMr;
                        
	                        var videoId = parseYoutubeUrl($scope.contentDeails.fileName);
	                        $scope.notification.thumbnailUrl = "http://img.youtube.com/vi/" + videoId + "/0.jpg";
                    	}
                    else if($scope.contentDeails.contentTypeCodeID == appConstants.code.contentCategory_Selflearning)
                    	{
	                    	$scope.notification.objectDetail1 = commonService.getValueByCode($scope.contentDeails.topicCodeID)[0].codeNameMr;
	                    	$scope.notification.objectDetail2 = commonService.getValueByCode($scope.contentDeails.contentLanguageCodeID)[0].codeNameMr;
                    	}


                },
                function (responseBody) {
                	console.log(responseBody);
                }
            );
        };
        
        // Get month names in short
        function getMonthName (monthNumber) { 
            var monthNames = [ 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec' ];
            return monthNames[monthNumber - 1];
        }
        
        // Parse youtube videos to get the vID
	    function parseYoutubeUrl(url) {
	        var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
	        var match = url.match(regExp);
	        if (match && match[2].length == 11) {
	            return match[2];
	        } else {
	            //error
	        }
	    } 

      $scope.getCodeFromCodeList = function(codeID){};

      $scope.isNotificationTypeSelected = function (notificationTypeCodeID) {
          return ($scope.notificationTypeCodeID === notificationTypeCodeID);
        }
      
      // Check if ObjectID is exists.
      $scope.ifObjectIDExists = function () {
    	  if($scope.objectID != null && $scope.objectID != 0)
    		  {
    		  return true;
    		  }
          return false;
        }

      // Get notification type.
      var getNotificationTypes = function () {
        $scope.notificationTypeList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.notificationType
          );
      };

      var populateDropDowns = function() {
    	getNotificationTypes();
      };

      // Set succes details
      var setSuccessDetails = function() {
        $scope.success = {};
        
        $scope.success.uploaderName = "self";

        if ($scope.notificationTypeCodeID == appConstants.code.notificationType_TeachingAid)
        {
          $scope.success.message = "Teaching Aid notification sent successfully";
        }
        else if ($scope.notificationTypeCodeID == appConstants.code.notificationType_SelfLearning)
        {
            $scope.success.message = "Self learning notification sent successfully";
        }
        else if ($scope.notificationTypeCodeID == appConstants.code.notificationType_News)
        {
            $scope.success.message = "News notification sent successfully";
        }
        else
    	{
        	$scope.success.message = "Notification sent successfully";
    	}
        
      }


      $scope.$on('codesAvailable', function(event,data){
        populateDropDowns();
      });
      
      $scope.selectedImageNumber = "";
      

      var init = function () {
        $scope.submitted = false;
        $scope.notification = {};
        $scope.news = {};
        $scope.errorMessage = "";
        engNotificationTitle = "";
        
    	// Check content type and set related value to the scope.
		if ($scope.contentType == appConstants.code.contentCategory_TeachingAid) 
			{
				fetchContentDetails($scope.objectID);
				$scope.iconname = appConstants.mitraIconName.teachingAid;
    			$scope.notificationTypeCodeID = appConstants.code.notificationType_TeachingAid;
    			$scope.bottomContentName = "Author ";
			}
		else if ($scope.contentType == appConstants.code.contentCategory_Selflearning)
			{
				fetchContentDetails($scope.objectID);
				$scope.iconname = appConstants.mitraIconName.selflearning;
				$scope.notificationTypeCodeID = appConstants.code.notificationType_SelfLearning;
				$scope.bottomContentName = "Author ";
			}
		else if ($scope.contentType == appConstants.code.NewsCategory_MAA)
			{
				fetchNewsDetails($scope.objectID);
				$scope.iconname = appConstants.mitraIconName.news;
				$scope.notificationTypeCodeID = appConstants.code.notificationType_News;
				$scope.bottomContentName = "News Category ";
			}
		else
			{
				$scope.iconname = appConstants.mitraIconName.other;
				$scope.notificationTypeCodeID = appConstants.code.notificationType_Other;
				$scope.bottomContentName = "";
			}
        
        populateDropDowns();
        
      };

      init();
      getNotificationTypes();
    }]);
