angular.module("mitraPortal").controller("reviewTeachingAidsController",
  ['$scope','$stateParams', 'appUtils', 'appConstants', 'commonService',
  function($scope, $stateParams, appUtils, appConstants, commonService) {
  	$scope.hello = $stateParams.contentID;
  	$scope.teachingAid = $stateParams.teachingAid;
    console.log($scope.teachingAid);
    
  	// if ($stateParams === undefined){
  	// 	var postData = {"fileTypeCodeID":fileTypeCodeId,"languageCodeIDs":101100};
   //          var header = {"authToken":"OF3eOof1qa5bDkHQjwPjlT24sRWb42J1",
   //                          "appLanguageCodeID":"113101"};
   //          return $http({method:'POST', url: 'http://54.152.74.194:8000' + '/content/searchTeachingAid/',
   //              data: postData,
   //              headers : header});
  	// }

  	console.log($stateParams);

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
      $scope.requirementList = [
        {'name':'computer', 'checked':false},
        {'name':'laptop', 'checked':false},
        {'name':'tab', 'checked':false},
        {'name':'mobile', 'checked':false},
      ]
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