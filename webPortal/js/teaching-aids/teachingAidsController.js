angular.module("mitraPortal").controller("teachingAidsController", TeachingAidsController);

TeachingAidsController.$inject = ['TeachingAidsService','commonService','$scope'];

function TeachingAidsController(TeachingAidsService,commonService,$scope) {
    var vm = this;
    vm.setStatus = setStatus;
    vm.setFileType = setFileType;
    vm.status = "";
    vm.fileType = 108100;
    vm.data = {};

    $scope.$on('codesAvailable', function(event,data){
        fetchTeachingAids();
    });

    function setStatus(status) {
        //TODO set other attributes of the view if needed
        vm.status = status;
        fetchTeachingAids();
    }

    function setFileType(fileType) {
        //TODO set other attributes of the view if needed
        vm.fileType = fileType;
        fetchTeachingAids();
    }

    function fetchTeachingAids() {
        TeachingAidsService.fetch(vm.fileType).then(
            function onSuccess(response) {
                var contents = response.data.data;
                console.log(contents);
                for(var i =0 ; i < contents.length ; i ++) {
                    var content = contents[i];
                    content.subjectName = commonService.getValueByCode(content.subject)[0].codeNameEn;
                    var ids = content.gradeCodeIDs.split(",");
                    var grades = "";
                    for (var j = 0 ; j < ids.length ; j++) {
                        grades = grades + " Grade " + commonService.getValueByCode(ids[j])[0].codeNameEn;
                    }
                    content.grades = grades;
                }
                console.log(vm.fileType);
                if(vm.fileType == 108100) {
                    console.log("contents . length" + contents.length);
                    for(i =0 ; i < contents.length ; i ++) {
                        console.log("inside for..");
                        var videoId = parseYoutubeUrl(contents[i].fileName);
                        console.log("video id is : " + videoId);
                        contents[i].thumbnailUrl = "http://img.youtube.com/vi/" + videoId + "/0.jpg";
                        console.log("thumbnail url " + contents[i].thumbnailUrl);
                    }
                }
                vm.data = response.data.data;
            },
            function onFailure(response) {

            }
        );
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
}