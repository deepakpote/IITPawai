/* Save news to the draft or publish news
 * Created by : Dipak Pote
 * Date : 15 March-2017
 * 
 * */
angular.module("mitraPortal").controller("addNewsController",
  ['$scope', '$location', '$log', '$window', '$state', '$http', '$uibModal', 'appUtils', 'appConstants', 'newsService', 'commonService', '$filter',
  function($scope, $location, $log, $window, $state, $http, $uibModal, appUtils, appConstants, newsService, commonService, filter) {


    $scope.inputs= {}

      $scope.popup2 = {
          opened: false
      };

    $scope.selectedOption = "";

    $scope.setSelectedOption = function (selectedOption){
      if ($scope.selectedOption == selectedOption){
        $scope.selectedOption = null;
      }
      else{
        $scope.selectedOption = selectedOption;
      }
    }

      $scope.showDate = function() {
          console.log('show date');
          $scope.makeDateVisible = true;
          $scope.selectedDate = "";

      };

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

    // Validate news details
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

        var headers = { "authToken": appUtils.getFromCookies("token",""),
        'Content-Type': undefined};
        
        $log.debug("BEFORE fd");
        $log.debug(fd);

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
            templateUrl :  'webPortal/js/news-circulars/submittedSuccessView.html',
          })
          modalInstance.result.finally(function(){ 
            if ($scope.news.newsCategoryCodeID == appConstants.code.NewsCategory_MAA){
              $window.scrollTo(0, 0);
              $state.go('main.notLoggedIn.home');
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
            templateUrl : appConstants.endpoint.baseUrl + 'webPortal/js/news-circulars/submittedErrorView.html',
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

      // Check if the news category is M.A.A
      $scope.isNewsTypeMAA = function (response) {
        return ($scope.news.newsCategoryCodeID === appConstants.code.NewsCategory_MAA);
      }

      // Set news category 
      $scope.newsTypeOnClick = function (selectedNewsTypeCodeID) {
        $scope.news.newsCategoryCodeID = selectedNewsTypeCodeID;  
      }

      
      $scope.isNewsTypeSelected = function (newsTypeCodeID) {
        return ($scope.news.newsCategoryCodeID === newsTypeCodeID);
      }

      // Get news type/category.
      var getNewsTypes = function () {
        $scope.newsTypeList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.NewsCategory
          );
        var icons = ["LOGO HERE"];
        for (var i=0; i<$scope.newsTypeList.length;i++){
          $scope.newsTypeList[i].icon = icons[i];
        }
      };

      // Get departments for news
      var getDepartments = function () {
        $scope.departmentList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.department
          );
      };

      // Get news importance
      var getNewsImportance = function () {
        $scope.newsImportanceList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.importance
          );
      };


      var populateDropDowns = function() {
    	getNewsTypes();
    	getDepartments();
    	getNewsImportance();
      };

      var setSuccessDetails = function() {
        $scope.success = {};
        
        $scope.success.uploaderName = "self";


        if ($scope.statusCodeID == appConstants.code.contentOrNewsOrTrainingStatus_Created)
        {
          $scope.success.message = "Saved To Drafts";
        }
        else if ($scope.statusCodeID == contentOrNewsOrTrainingStatus_SentForReview)
        {
            $scope.success.message = "Sent For Review";
        }
        else if ($scope.statusCodeID == contentOrNewsOrTrainingStatus_Published)
        {
            $scope.success.message = "Published";
        }
        
      }


      $scope.$on('codesAvailable', function(event,data){
        populateDropDowns();
      });
      
      $scope.selectedImageNumber = "";
      
      // Set selected image number.
      $scope.imageUpload = function (selectedImageNumber) {
    	  $log.debug(selectedImageNumber);
    	  $scope.selectedImageNumber = "";
        $scope.selectedImageNumber = selectedImageNumber;  
      }

      var init = function () {
        $scope.submitted = false;
        $scope.news = {};
        $scope.errorMessage = "";
        $log.debug("IN init");
        populateDropDowns();
      };

      init();
      getNewsTypes();
    }])
    .directive('fileModel', ['$parse', '$log', function ($parse, $log) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {


                element.bind('change', function () {
                	$log.debug("IN bind");
                	$log.debug(scope);
                	

                	switch (scope.selectedImageNumber) {
                    case 1:
                    	scope.$parent.newsImage1 = element[0].files[0];
                        break;
                    case 2:
                    	scope.$parent.newsImage2 = element[0].files[0];
                        break;
                    case 3:
                    	scope.$parent.newsImage3 = element[0].files[0];
                        break;
                    case 4:
                    	scope.$parent.newsImage4 = element[0].files[0];
                        break;
                    case 5:
                    	scope.$parent.newsImage5 = element[0].files[0];
                        break;
                    case 'pdfFile':
                    	scope.$parent.myPDFFile = element[0].files[0];
                        break;
                    default:
                    	$log.debug("IN switch default");

                }
                        
                    scope.$apply();
                });
            }
        };
    }]);
;
