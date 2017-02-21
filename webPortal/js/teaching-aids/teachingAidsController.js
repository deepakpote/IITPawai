angular.module("mitraPortal").controller("teachingAidsController", TeachingAidsController);

TeachingAidsController.$inject = [];

function TeachingAidsController() {
    var vm = this;
    vm.setFilter = setFilter;
    vm.filter = "";

    teachingAidsService.fetch().then(
        function onSuccess(response) {

        },
        function onFailure(response) {

        }
    );



    function setFilter(filterType) {
        console.log("filter set  : " + filterType);
        vm.filter = filterType;
    }

}