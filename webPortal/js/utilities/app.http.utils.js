angular.module("mitraPortal").service('HttpUtils', [
    function() {

        var service = {
            isSuccessful : isSuccessful
        };

        function isSuccessful(data) {
            if(100100 == data.response_message) {
                return true;
            }
            return false;
        }

        return service;

    }]);