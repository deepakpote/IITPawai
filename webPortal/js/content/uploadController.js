angular.module("mitraPortal").controller("uploadController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'contentService', 'commonService', '$filter',
  function($scope, $location, $log, appUtils, appConstants, contentService, commonService, filter) {
  

    $scope.acceptedFileTypes = {
      "108100" : "",    //Video
      "108101" : "audio/*",    //Audio
      "108102" : ".ppt,.pptx",    //PPT
      "108103" : ".xls,.xlsx",    //Worksheet
      "108104" : ".pdf",    //PDF
      "108105" : "",    //Ek Step
    }

    $scope.selectedOption = "";

    $scope.setSelectedOption = function (selectedOption){
      $scope.selectedOption = selectedOption;
    }

    $scope.$watch('gradeList', function (gradeList){
      var checkedGrades = gradeList.filter(function(grade){ return (grade.checked == true)});
      var gradesString = "";
      if (checkedGrades.length > 0){
          gradesString = checkedGrades[0].codeID;
      }
      for (i=1;i<checkedGrades.length;i++){

          gradesString += ',' + checkedGrades[i].codeID;
      }
      $scope.content.gradeCodeIDs = gradesString;
      $log.debug(gradesString);
      
    }, true);
  
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

    $scope.getCodeFromCodeList = function(codeID){};

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

    $scope.showContentObject = function(){
      $log.debug($scope.content);
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
  		$scope.languageList = commonService.getCodeListPerCodeGroup(
  				appConstants.codeGroup.contentLanguage
  			);
  	};
  	
    var getRequirements = function () {
      $scope.languageList = commonService.getCodeListPerCodeGroup(
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
      getRequirements();
  	};

    $scope.$on('codesAvailable', function(event,data){
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