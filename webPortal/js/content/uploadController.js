angular.module("mitraPortal").controller("uploadController",
  ['$scope', '$location', '$log', '$window', '$state', '$http', '$modal', 'appUtils', 'appConstants', 'contentService', 'commonService', '$filter',
  function($scope, $location, $log, $window, $state, $http, $modal, appUtils, appConstants, contentService, commonService, filter) {


    $scope.acceptedFileTypes = {
      "108100" : "",              //Video
      "108101" : "audio/*",       //Audio
      "108102" : ".ppt,.pptx",    //PPT
      "108103" : ".xls,.xlsx",    //Worksheet
      "108104" : ".pdf",          //PDF
      "108105" : "",              //Ek Step
    }

    $scope.inputs= {}

    $scope.selectedOption = "";
    //$scope.fileName = "https://www.youtube.com/watch?v=PT2_F-1esPk";

    $scope.setSelectedOption = function (selectedOption){
      if ($scope.selectedOption == selectedOption){
        $scope.selectedOption = null;
      }
      else{
        $scope.selectedOption = selectedOption;
      }
    }


    $scope.$watch('gradeList', function (gradeList){
      var checkedGrades = gradeList.filter(function(grade){ return (grade.checked == true)});
      var gradesString = "";
      var displayGradesString = "";
      if (checkedGrades.length > 0){
        gradesString = checkedGrades[0].codeID;
        displayGradesString = checkedGrades[0].codeNameEn;
      }
      for (i=1;i<checkedGrades.length;i++){

        gradesString += ',' + checkedGrades[i].codeID;
        displayGradesString += ', ' + checkedGrades[i].codeNameEn;
      }
      $scope.content.gradeCodeIDs = gradesString;
      $scope.displayGradesString = displayGradesString;
      $log.debug(gradesString);
      
    }, true);


    $scope.$watch('requirementList', function (requirementList){
      var checkedRequirements = requirementList.filter(function(requirement){ return (requirement.checked == true)});
      var requirementsString = "";
      var displayRequirementsString = "";
      if (checkedRequirements.length > 0){
        requirementsString = checkedRequirements[0].codeID;
        displayRequirementsString = checkedRequirements[0].codeNameEn;
      }
      for (i=1;i<checkedRequirements.length;i++){

        requirementsString += ',' + checkedRequirements[i].codeID;
        displayRequirementsString += ', ' + checkedRequirements[i].codeNameEn;
      }
      $scope.content.requirementCodeIDs = requirementsString;
      $scope.displayRequirementsString = displayRequirementsString;
      $log.debug(requirementsString);
      
    }, true);

    $scope.setDirty = function(form){
      angular.forEach(form.$error.required, function(field) {
        field.$dirty = true;
      });
    }

    $scope.save = function() {

      if(validateOptions()){
        $scope.statusCodeID = 114100;
        $log.debug("in save");
        submit();
      }
    }

    $scope.sendForReview = function() {

      if(validateOptions()){
        $scope.statusCodeID = 114101;
        $log.debug("in sfr");
        submit();
      }
    }

    var validateOptions = function() {
      if ($scope.content.fileTypeCodeID == 108100){

        var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
        var match = $scope.inputs.fileName.match(regExp);
        $log.debug($scope.inputs.fileName);
        if (match && match[2].length ==11){
          $log.debug("good youtube");
          return true;
        }
        else{
          alert("Invalid youtube URL. Please enter proper youtube URL.")
          return false;
        }
      }
      return true;
    }
    


    var submit = function(){

      $log.debug($scope.content);
      var fd = new FormData();
        //fd.append('contentID',);
        for  (var key in $scope.content){
          $log.debug(key);
          $log.debug($scope.content[key]);
          fd.append(key, $scope.content[key]);
        }
        fd.append('statusCodeID', $scope.statusCodeID);
        if ($scope.content.fileTypeCodeID == 108100){
          $log.debug("video");
          fd.append('fileName',$scope.inputs.fileName);
        }
        else{
          $log.debug("not a video");
          fd.append('uploadedFile', $scope.myFile);
        }

        for (var pair of fd.entries()) {
          console.log(pair[0]+ ', ' + pair[1]); 
        }

        var headers = { "authToken": appUtils.getFromCookies("token",""),
        "appLanguageCodeID":"113101",
        'Content-Type': undefined};

        $http.post("http://54.152.74.194:8000/content/uploadContent/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $scope.result= "uploaded successfully";
          setSuccessDetails();
          var modalInstance = $modal.open({
            url: 'result',
            scope: $scope,
            templateUrl : '/mitra.test/js/content/submittedSuccessView.html',
          })
          modalInstance.result.finally(function(){ 
            if ($scope.content.contentTypeCodeID == '107100'){
              $window.scrollTo(0, 0);
              $state.go('main.loggedIn.teachingAids');
            }
            else if ($scope.content.contentTypeCodeID == '107101') {
              $window.scrollTo(0, 0);
              $state.go('main.loggedIn.selfLearning');

            }
          });
          ;
        },
        function error(response){
          $log.debug("not 2xx");
          $scope.uploadErrorMessage = commonService.getValueByCode(response.data.response_message)[0].codeNameEn;
          $log.debug($scope.uploadErrorMessage);
          $scope.result ="Failed to upload content.";
          var modalInstance = $modal.open({
            url: 'result',
            scope: $scope,
            templateUrl : '/mitra.test/js/content/submittedErrorView.html',
          });
        });
      }

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



      var getContentTypes = function () {
        $scope.contentTypeList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.contentType
          );
        var icons = ["school","subscriptions","date_range"];
        for (var i=0; i<$scope.contentTypeList.length;i++){
          $scope.contentTypeList[i].icon = icons[i];
        }
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
        $scope.requirementList = commonService.getCodeListPerCodeGroup(
          appConstants.codeGroup.requirement
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

      var setSuccessDetails = function() {
        $scope.success = {};
        
        $scope.success.uploaderName = "self";


        if ($scope.statusCodeID == 114100){

          $scope.success.message = "Saved To Drafts";
        }
        else if ($scope.statusCodeID == 114101){
            $scope.success.message = "Sent For Review";
        }
        

        if ($scope.content.contentTypeCodeID == 107100){
          var checkedGrades = $scope.gradeList.filter(function(grade){ return (grade.checked == true)});
          var gradesString = "";
          if (checkedGrades.length > 0){
            gradesString = checkedGrades[0].codeNameEn;
          }
          for (i=1;i<checkedGrades.length;i++){

            gradesString += ', ' + checkedGrades[i].codeNameEn;
          }
          $scope.success.first = gradesString;
          $scope.success.second = commonService.getValueByCode($scope.content.subjectCodeID)[0].codeNameEn;
        }
        
        if ($scope.content.contentTypeCodeID == 107101){
          $scope.success.first = commonService.getValueByCode($scope.content.topicCodeID)[0].codeNameEn;
          $scope.success.second = commonService.getValueByCode($scope.content.contentLanguageCodeID)[0].codeNameEn;
        }

        

        $log.debug($scope.inputs.fileName);

        if ($scope.content.fileTypeCodeID == 108100 && $scope.inputs.fileName){
          $scope.success.image = "http://img.youtube.com/vi/" + parseYoutubeUrl($scope.inputs.fileName) + "/0.jpg";
          $log.debug("image");
        }
        else{
          $scope.success.image = "";
          $log.debug("no image");
        }
      }

      function parseYoutubeUrl(url) {
        var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
        var match = url.match(regExp);
        if (match && match[2].length == 11) {
            return match[2];
        } else {
            //error
            return "";
        }
    }

      $scope.$on('codesAvailable', function(event,data){
        populateDropDowns();
      });

      var init = function () {
        $scope.submitted = false;
        $scope.content = {};
        $scope.errorMessage = "";

        populateDropDowns();
      };

      init();
      getContentTypes();
    }
    ])
.directive('fileModel', ['$parse', '$log', function ($parse, $log) {
  return {
    restrict: 'A',
    link: function(scope, element, attrs) {


      element.bind('change', function(){
        scope.$parent.myFile = element[0].files[0];
        scope.$apply();
      });
    }
  };
}]);;