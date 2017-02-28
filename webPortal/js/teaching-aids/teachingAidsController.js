angular.module("mitraPortal").controller("teachingAidsController", TeachingAidsController);

TeachingAidsController.$inject = ['TeachingAidsService','commonService','$scope','appConstants','$filter'];

function TeachingAidsController(TeachingAidsService,commonService,$scope,appConstants,filter) {
    var vm = this;
    vm.setStatus = setStatus;
    vm.setFileType = setFileType;
    vm.status = 114101;
    vm.fileType = 108100;
    vm.data = {};
    vm.selectedOption = "";
    vm.setSelectedOption = setSelectedOption;
    vm.dataFilter = {
        "subjectCodeIDs" : "",
        "gradeCodeIDs" : ""
    };
    vm.showDataFilters = showDataFilters;

    activate();

    ////////////////

    function activate() {
        console.log(commonService.isCodeListEmpty());
        if (commonService.isCodeListEmpty()) {
            $scope.$on('codesAvailable', function(event,data){
                getSubjects();
                getGrades();
                fetchTeachingAids();
                setGradeAndSubjectWatchers();
            });
        } else {
            getSubjects();
            getGrades();
            fetchTeachingAids();
            setGradeAndSubjectWatchers();
        }
    }

    function setStatus(status) {
        vm.status = status;
        fetchTeachingAids();
    }

    function setFileType(fileType) {
        vm.fileType = fileType;
        fetchTeachingAids();
    }

    function fetchTeachingAids() {
        TeachingAidsService.fetch(vm.fileType, vm.status, vm.dataFilter ,onSuccess, onFailure);
        function onSuccess(response) {
            var contents = response.data;
            for(var i = 0 ; i < contents.length ; i ++) {
                var content = contents[i];
                content.subjectName = commonService.getValueByCode(content.subject)[0].codeNameEn;
                var ids = content.gradeCodeIDs.split(",");
                var grades = "";
                for (var j = 0 ; j < ids.length ; j++) {
                    grades = grades + " Grade " + commonService.getValueByCode(ids[j])[0].codeNameEn;
                }
                content.grades = grades;
            }
            if(vm.fileType == 108100) {
                for(i = 0 ; i < contents.length ; i ++) {
                    var videoId = parseYoutubeUrl(contents[i].fileName);
                    contents[i].thumbnailUrl = "http://img.youtube.com/vi/" + videoId + "/0.jpg";
                }
            }
            vm.data = contents;
        }
        function onFailure(response) {

        }
    }

    function parseYoutubeUrl(url) {
        var regExp = /^.*(youtu\.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
        var match = url.match(regExp);
        if (match && match[2].length == 11) {
            return match[2];
        } else {
            //error
        }
    }

    function getSubjects() {
        $scope.subjectList = commonService.getCodeListPerCodeGroup(
            appConstants.codeGroup.subject
        );
    }

    function setSelectedOption(option) {
        console.log("selected option : " + option);
        console.log("current optoin : " + vm.selectedOption);
        if(option === vm.selectedOption) {
            vm.selectedOption = "";
        } else {
            vm.selectedOption = option;
        }
    }

    function showDataFilters() {
        console.log(vm.dataFilter);
    }

    function setGradeAndSubjectWatchers() {
        /**
         * watch grade list to make the server call on change
         * */
        $scope.$watch('gradeList', function (gradeList){
            if(gradeList != undefined) {
                var checkedGrades = gradeList.filter(function(grade){ return (grade.checked == true)});
                var gradesString = "";
                if (checkedGrades.length > 0){
                    gradesString = checkedGrades[0].codeID;
                }
                for (var i = 1;i < checkedGrades.length; i++){
                    gradesString += ',' + checkedGrades[i].codeID;
                }
                vm.dataFilter.gradeCodeIDs = gradesString;
                fetchTeachingAids();
            }
        }, true);

        /**
         * watch subject list to make the server call on change
         * */
        $scope.$watch('subjectList', function (subjectList){
            if(subjectList != undefined) {
                var checkedSubjects = subjectList.filter(function(subject){ return (subject.checked == true)});
                var subjectString = "";
                if (checkedSubjects.length > 0){
                    subjectString = checkedSubjects[0].codeID;
                }
                for (var i = 1;i < checkedSubjects.length; i++){
                    subjectString += ',' + checkedSubjects[i].codeID;
                }
                vm.dataFilter.subjectCodeIDs = subjectString;
                fetchTeachingAids();
            }
        }, true);
    }

    function getGrades() {
        $scope.gradeList = commonService.getCodeListPerCodeGroup(
            appConstants.codeGroup.grade
        );
    }
}