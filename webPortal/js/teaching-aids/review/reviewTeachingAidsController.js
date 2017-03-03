angular.module("mitraPortal").controller("reviewTeachingAidsController",
  ['$scope','$stateParams', '$log', 'appUtils', 'appConstants', 'commonService',
  function($scope, $stateParams, $log, appUtils, appConstants, commonService) {

    $scope.mode = "PREVIEW"; // can be "EDIT" or "PREVIEW" or "GIVE FEEDBACK"

    $scope.checked = {
      subject: false,
      language: false,
      requirements: false,
      grades: false,
      marAuthor: false,
      marContentTitle: false,
      marInstruction: false,
      engAuthor: false,
      engContentTitle: false,
      engInstruction: false,
      topic: false,
      contentType: false,
      file: false
    }

    $scope.content = {
    };
    
  	var fetchContentDetails = function(){
            var options = {};
            var data={"contentID" : $stateParams.contentID };//parseInt($stateParams.contentID) };
            options.data = data;
            options.url='content/contentDetail/';
            options.headers = { "authToken": appUtils.getFromCookies("token","") };

            appUtils.ajax(options,
              function(responseBody){
                $log.debug("success");
                $scope.content = responseBody.data[0];
                $log.debug($scope.content);
                $log.debug($scope.contentLanguageList);
              },
              function(responseBody){
                $log.debug("error");
                $log.debug(responseBody);
              }
            );
  	}

    var populateDropDowns = function() {
      $scope.contentTypeList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.contentType
        );
      $scope.subjectList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.subject
        );
      $scope.gradeList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.grade
        );
      $scope.fileTypeList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.fileType
        );
      $scope.topicList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.topic
        );
      $scope.contentLanguageList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.contentLanguage
        );
      $scope.requirementList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.requirement
          );
    };

    $scope.$on('codesAvailable', function(event,data){
      populateDropDowns();
    });
    
    var init = function () {
      $scope.submitted = false;
      $scope.content = { "contentID": 0, "contentTypeCodeID": 0};
      $scope.errorMessage = "";
      fetchContentDetails();
      populateDropDowns();
    };
    
    init();


  }
]);