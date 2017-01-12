var webPortal = angular.module("webPortal", ['ngRoute','ngCookies','ngMessages'])
				.config(config)
				.run(run);

config.$inject = ['$routeProvider', '$locationProvider'];
function config($routeProvider, $locationProvider) {
    $routeProvider.
    when('/sendDataNotifications', {
        templateUrl: 'js/notifications/sendDataNotificationView.html'
     }).
     when('/login', {
         templateUrl: 'js/login/loginView.html'
      }).
     otherwise({
        redirectTo: '/login'
     });
}


run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
function run($rootScope, $location, $cookies, $http) {
    // keep user logged in after page refresh
    $rootScope.globals = $cookies.getObject('globals') || {};
//    if ($rootScope.globals.currentUser) {
//        $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.token;
//    }

    $rootScope.$on('$locationChangeStart', function (event, next, current) {
        // redirect to login page if not logged in and trying to access a restricted page
        var restrictedPage = ($location.path()).indexOf('/login') === -1;
        var loggedIn = $rootScope.globals.currentUser;
        if (restrictedPage && !loggedIn) {
            $location.path('/login');
        }
    });
}
