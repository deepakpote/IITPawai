angular.module("mitraPortal").controller("uploadController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'contentService', 'commonService',
  function($scope, $location, $log, appUtils, appConstants, contentService, commonService) {
  	


    var uploadContentSuccessCB = function (response) {
  		$scope.submitted = false;
  		$log.debug('in success cb of upload content');
  		$log.debug(response);
  	};
  	
  	var uploadContentErrorCB = function (response) {
  		$log.debug('in error cb of upload content');
  		$log.debug(response);
  	};
  	
  	$scope.uploadContent = function () {
  		contentService.contentUpload($scope.content, 
  				uploadContentSuccessCB,
  				uploadContentErrorCB);
  		$scope.submitted = true;
  	};

  	$scope.isContentTypeTeachingAid = function (response) {
  		return ($scope.content.contentTypeCodeID === 107100);
  	}
  	
  	$scope.isContentTypeSelfLearning = function (response) {
  		return ($scope.content.contentTypeCodeID === 107101);
  	}
  	
  	$scope.isFileTypeVideo = function (response) {
  		return ($scope.content.fileTypeCodeID === 108100);
  	}
  	
  	$scope.contentTypeOnClick = function (selectedContentTypeCodeID) {
  		$scope.content.contentTypeCodeID = selectedContentTypeCodeID;  
  	}
  	
  	$scope.isContentTypeSelected = function (contentTypeCodeID) {
  		return ($scope.content.contentTypeCodeID === contentTypeCodeID);
  	}

  	var getContentTypes = function () {
  		$scope.contentTypeList = commonService.getCodeListPerCodeGroup(
  				appConstants.codeGroup.contentType
  			);
  	};
  	
  	var getSubjects = function () {
  		$scope.subjectList = commonService.getCodeListPerCodeGroup(
  				appConstants.codeGroup.subject
  			);
  	};
  	
  	var getGrades = function () {
  		$scope.gradeList = commonService.getCodeListPerCodeGroup(
  				appConstants.codeGroup.grade
  			);
  	};
  	
  	var getFileTypes = function () {
  		$scope.fileTypeList = commonService.getCodeListPerCodeGroup(
  				appConstants.codeGroup.fileType
  			);
  	};
  	
  	var getTopics = function () {
  		$scope.topicList = commonService.getCodeListPerCodeGroup(
  				appConstants.codeGroup.topic
  			);
  	};
  	
  	var getContentLanguages = function () {
  		$scope.contentLanguageList = commonService.getCodeListPerCodeGroup(
  				appConstants.codeGroup.contentLanguage
  			);
  	};
  	
  	var populateDropDowns = function() {
  		getContentTypes();
  	  getSubjects();
  	  getGrades();
  	  getFileTypes();
  	  getTopics();
  	  getContentLanguages();
  	};

    $scope.$on('codesAvailable', function(event,data){
      $log.debug("codes available");
      populateDropDowns();
    });
  	
  	var init = function () {
  		$scope.submitted = false;
  		$scope.content = { "contentID": 0, "contentTypeCodeID": 0};
  	  $scope.errorMessage = "";
  	  
  	  populateDropDowns();
  	};
  	
  	init();
    getContentTypes();
  }
]);