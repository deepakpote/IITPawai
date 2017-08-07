angular.module("mitraPortal").service('appUtils', ['$http', '$log', '$rootScope', '$cookies', '_', 'appConstants',
    function ($http, $log, $rootScope, $cookies, _, appConstants) {

        var service = {};

        /**
         * Common ajax call to be used across the application
         *
         * @param (object) options - options passed for ajax call
         *
         * @param (function) successCB - Callback function, in case of success
         *
         * @param (function) errorCB - Callback function, in case of error
         *
         */
        service.ajax = function (options, successCB, errorCB) {
            options = options || {};

            if (_.isEmpty(options)) {
                $log.error('Set options params for ajax call');
                return;
            }

            options.url = options.url || '';
            if (options.url === '') {
                $log.error('Set options.url param for ajax call');
                return;
            }

            options.url = appConstants.endpoint.baseUrl + options.url;

            options.method = options.method || 'post';

            // Make ajax call
            $http({
                method: options.method,
                url: options.url, headers: options.headers,
                data: options.data
            }).then(function successCallback(response) {
                successCB(response.data)
            }, function errorCallback(response) {
                errorCB(response.data)
            });
        };

        /**
         * Get the value stored in the local storage, against the said key.
         *
         * @param (string) key - The key, that is used to retrieve the stored value.
         *
         * @param (object) [defaultValue] - The default value to be returned by `getFromLocalStorage`
         * function in case, there's no value stored in local storage, against the said key.
         * This is an optional parameter. And it defaults to undefined, when not passed.
         */
        service.getFromLocalStorage = function (key, defaultValue) {

            var storedMitraObject = $rootScope.appGlobals;//$cookies.getObject(appConstants.localStorage.baseKey);
            storedValue = storedMitraObject ? storedMitraObject[key] : defaultValue;
            return storedValue
        };

        service.saveToLocalStorage = function (key, value) {
            //var storedMitraObject = $cookies.getObject(appConstants.localStorage.baseKey);
            var storedMitraObject;

            if ($rootScope.appGlobals !== undefined)
                storedMitraObject = $rootScope.appGlobals;//[appConstants.localStorage.baseKey];

            if (storedMitraObject === undefined)
                storedMitraObject = {};

            storedMitraObject[key] = value;
            //$cookies.putObject(appConstants.localStorage.baseKey, storedMitraObject);
            $rootScope.appGlobals = storedMitraObject;
        };

        /**
         * Get the value stored in the local storage, against the said key.
         *
         * @param (string) key - The key, that is used to retrieve the stored value.
         *
         * @param (object) [defaultValue] - The default value to be returned by `getFromLocalStorage`
         * function in case, there's no value stored in local storage, against the said key.
         * This is an optional parameter. And it defaults to undefined, when not passed.
         */
        service.getFromCookies = function (key, defaultValue) {

            var value = $cookies.get(key);
            if (value == undefined) {
                value = defaultValue;
            }

            return value;
        };

        service.saveToCookies = function (key, value) {
            var expireDate = new Date();
            expireDate.setDate(expireDate.getDate() + 1);
            var expires = {'expires': expireDate};
            $cookies.put(key,value,expires);
            console.log("put to cookie " + $cookies.get(key));
        };

        service.isLoggedInUser = function () {
            var key = "token";
            var cookie = $cookies.get(key);
            return cookie != undefined;

        };

        service.isAdmin = function () {
            var roleIDs = $cookies.get("roleIDs");
            if (roleIDs.split(',').indexOf(appConstants.role.admin.toString()) > -1){
                return true;
            }
            else{
                return false
            }
        };

        service.isTeacher = function () {
            var roleIDs = $cookies.get("roleIDs");
            if (roleIDs.split(',').indexOf(appConstants.role.teacher.toString()) > -1){
                return true;
            }
            else{
                return false
            }
        };

        return service;

    }]);
