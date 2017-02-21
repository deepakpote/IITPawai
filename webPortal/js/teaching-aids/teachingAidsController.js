angular.module("mitraPortal").controller("teachingAidsController", TeachingAidsController);

TeachingAidsController.$inject = ['TeachingAidsService'];

function TeachingAidsController(TeachingAidsService) {
    var vm = this;
    vm.setStatus = setStatus;
    vm.setFileType = setFileType;
    vm.status = "";
    vm.fileType = 108104;
    vm.data = {};

    fetchTeachingAids();

    function setStatus(status) {
        //TODO set other attributes of the view if needed
        vm.status = status;
        fetchTeachingAids();
    }

    function setFileType(fileType) {
        //TODO set other attributes of the view if needed
        vm.fileType = fileType;
        vm.fileType = 108100;
        fetchTeachingAids();
    }

    function fetchTeachingAids() {
        TeachingAidsService.fetch(vm.fileType).then(
            function onSuccess(response) {
                console.log("response received : ");
                vm.data = response.data.data;
                console.log(vm.data);
            },
            function onFailure(response) {

            }
        );
    }

}