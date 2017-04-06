angular.module("mitraPortal").controller("reviewContentController",
  ['$scope','$stateParams', '$state', '$window', '$log', '$http', 'appUtils', 'appConstants', 'commonService',
  function($scope, $stateParams, $state, $window, $log, $http, appUtils, appConstants, commonService) {

    $scope.acceptedFileTypes = {
                "108100" : "",              //Video
                "108101" : "audio/*",       //Audio
                "108102" : ".ppt,.pptx",    //PPT
                "108103" : ".xls,.xlsx",    //Worksheet
                "108104" : ".pdf",          //PDF
                "108105" : ""             //Ek Step
            };
    $scope.inputs= {};
    $scope.isAdmin = appUtils.isAdmin();
    $scope.isTeacher = appUtils.isTeacher();

    $log.debug($scope.isAdmin, $scope.isTeacher, appUtils.isAdmin(), appUtils.isTeacher());

    $scope.mode = "PREVIEW"; // can be "EDIT" or "PREVIEW" or "GIVE FEEDBACK"
    $scope.content = {};
    $scope.contentEditable = false;

    $scope.checked = {
      subject: false,
      language: false,
      requirements: false,
      grades: false,
      topic: false,
      marAuthor: false,
      marContentTitle: false,
      marInstruction: false,
      engAuthor: false,
      engContentTitle: false,
      engInstruction: false,
      fileName: false,
      fileType: false
    }

    $scope.setDirty = function(form){
                angular.forEach(form.$error.required, function(field) {
                    field.$dirty = true;
                });
            };


    $scope.setSelectedOption = function (selectedOption){
      if($scope.mode == 'EDIT'){
        if ($scope.selectedOption == selectedOption){
          $scope.selectedOption = null;
        }
        else{
          $scope.selectedOption = selectedOption;
        }
      }
    }

    $scope.toggle = function(option){
      $log.debug($scope.checked[option]);
      $scope.checked[option] = !$scope.checked[option];
      $log.debug($scope.checked[option]);

      $log.debug($scope.checked);
    }

    $scope.setMode = function(mode) {
        console.log("function arg mode" + mode);
        console.log("scope mode" + $scope.mode);
      if ($scope.mode == 'EDIT' && mode == 'PREVIEW' && $scope.originalContent){
        $scope.content = JSON.parse(JSON.stringify($scope.originalContent)); //deepcopy
        setGradesFromContent();
        setRequirementsFromContent();
        $log.debug($scope.content);
      }
      $window.scrollTo(0, 0);
      $scope.mode = mode;
    }

    $scope.publish = function(){
      var nextState = null;
      if ($scope.content.contentTypeCodeID == '107100'){
        if ($scope.checked.subject && $scope.checked.language && $scope.checked.requirements && $scope.checked.grades && 
            $scope.checked.marAuthor && $scope.checked.marContentTitle && $scope.checked.marInstruction && 
            $scope.checked.engAuthor && $scope.checked.engContentTitle && $scope.checked.engInstruction && 
            $scope.checked.fileName && $scope.checked.fileType) {
          nextState = 'main.loggedIn.teachingAids';
        }
        else{
          alert("Please check all fields and mark as correct");
          return
        }
      }
      else if ($scope.content.contentTypeCodeID == '107101'){
        if ($scope.checked.language && $scope.checked.topic && 
            $scope.checked.marAuthor && $scope.checked.marContentTitle && $scope.checked.marInstruction && 
            $scope.checked.engAuthor && $scope.checked.engContentTitle && $scope.checked.engInstruction && 
            $scope.checked.fileName && $scope.checked.fileType) {
          nextState = 'main.loggedIn.selfLearning';
        }
        else{
          alert("Please check all fields and mark as correct");
          return
        } 
      }

      var options = {};
      var data={"contentID" : $stateParams.contentID, 
                "statusCodeID": 114102};
      options.data = data;
      options.url='content/saveContentStatus/';
      options.headers = { "authToken": appUtils.getFromCookies("token","") };

      appUtils.ajax(options,
        function(responseBody){
                $log.debug("success");
                $log.debug(responseBody);
                $window.scrollTo(0, 0);
                $state.go(nextState);

              },
              function(responseBody){$log.debug($scope.content.gradeCodeIDs);
                $log.debug("error");
                $log.debug(responseBody);
              }
          );
    }

    $scope.saveChanges = function(){
      $log.debug("dsfsdfg");
      var fd = new FormData();
        //fd.append('contentID',);
        for  (var key in $scope.content){
          $log.debug(key);
          $log.debug($scope.content[key]);
          if ($scope.content[key]){
            fd.append(key, $scope.content[key]);
          }
          
        }

        fd.append("contentID", $stateParams.contentID);


        if ($scope.content.newFileTypeCodeID){
          // file changed
          if ($scope.content.newFileTypeCodeID == appConstants.fileTypeCode.video){
              $log.debug("video");
              fd.append('fileName',$scope.inputs.newFileName);
          }
          else{
              $log.debug("not a video");
              $log.debug($scope.myFile);
              fd.append('uploadedFile', $scope.myFile);
          }
        }
        else{
          // file not changed. so don't send anything.
          fd.delete("fileName");
        }
        
        var headers = { "authToken": appUtils.getFromCookies("token",""),
        "appLanguageCodeID":"113101",
        'Content-Type': undefined};

        $http.post(appConstants.endpoint.baseUrl + "content/uploadContent/", fd, {
          transformRequest: angular.identity,
          headers: headers
        })
        .then (function success(response){
          $log.debug("success");
          $window.scrollTo(0, 0);
          $state.transitionTo($state.current, $stateParams, {
              reload: true,
              inherit: false,
              notify: true
          });


          
        },
        function error(response){
          $log.debug("error");
        });
    }

    
    var fetchContentDetails = function(){
      var options = {};
      var data={"contentID" : $stateParams.contentID };
      options.data = data;
      options.url='content/contentDetail/';
      options.headers = { "authToken": appUtils.getFromCookies("token","") };

      appUtils.ajax(options,
        function(responseBody){
                //get content set
                $scope.content = responseBody.data[0];
                
                setGradesFromContent();
                setRequirementsFromContent();
                
                //make a copy in case user goes to edit and discards
                $scope.originalContent = JSON.parse(JSON.stringify($scope.content)); //deepcopy
              },
              function(responseBody){$log.debug($scope.content.gradeCodeIDs);
                $log.debug("error");
                $log.debug(responseBody);
              }
          );
    }

    var setGradesFromContent = function(){
      if ($scope.content.gradeCodeIDs){
        var gradesArray = $scope.content.gradeCodeIDs.split(',');
          for (var i=0;i<$scope.gradeList.length;i++){
          if (gradesArray.indexOf($scope.gradeList[i].codeID.toString()) > -1) {
              $scope.gradeList[i].checked = true;
            }
          else{
             $scope.gradeList[i].checked = false;
        }
       }
      }
     }

   var setRequirementsFromContent = function(){
    if ($scope.content.requirementCodeIDs){
      var requirementsArray = $scope.content.requirementCodeIDs.split(',');
      $log.debug($scope.content.requirementCodeIDs);
      for (var i=0;i<$scope.requirementList.length;i++){
        if (requirementsArray.indexOf($scope.requirementList[i].codeID.toString()) > -1) {
         $scope.requirementList[i].checked = true;
          }
          else{
            $scope.requirementList[i].checked = false;
          }
        }
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
      
    }, true);

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
      fetchContentDetails();
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
  ])
  .directive('fileModel', ['$parse', '$log', function ($parse, $log) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {


                element.bind('change', function () {
                    scope.$parent.myFile = element[0].files[0];
                    scope.$apply();
                });
            }
        };
    }]);