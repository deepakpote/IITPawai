var mitraPortal = angular.module("mitraPortal", ['ngCookies','ngMessages','ui.router','ui.bootstrap','ngLoadingSpinner', 'ngAnimate']);
mitraPortal.
config(['$stateProvider', '$urlRouterProvider','loginModalStateProvider','$locationProvider',
    function config($stateProvider, $urlRouterProvider, loginModalStateProvider, $locationProvider) {

        //$locationProvider.html5Mode(true);
        $urlRouterProvider.otherwise('/home');

        $stateProvider
            .state('main', {
                abstract: true,
                url: '',
                templateUrl: 'webPortal/js/common/mitraLayoutView.html',
                controller : 'mainController'
            })
            .state('main.notLoggedIn', {
                abstract: true,
                url: '',
                views: {
                    'header': {
                        templateUrl: 'webPortal/js/common/headerView.html'
                    },
                    'leftMenu': {
                        templateUrl: 'webPortal/js/common/leftMenuView.html'
                    },
                    'contentBox': {
                        templateUrl: 'webPortal/js/common/contentBoxView.html'
                    }
                }
            })
            .state('main.notLoggedIn.home', {
                url: '/home',
                views: {
                    'content': {
                        templateUrl: 'webPortal/js/home/layoutView.html'
                        //template: "Setting up home layout"
                    },
                    'welcome@main.notLoggedIn.home': {
                        templateUrl: 'webPortal/js/home/welcomeView.html',
                        controller: 'welcomeController'
                    },
                    'map@main.notLoggedIn.home': {
                        templateUrl: 'webPortal/js/home/mapView.html',
                        controller: function($scope) {
                            console.log('hit map controller');
                        }
                    }
                }
            })
            .state('main.loggedIn', {
                url: '',
                abstract : true,
                views: {
                    'header': {
                        templateUrl: 'webPortal/js/common/logged-in/headerView.html'
                    },
                    'leftMenu': {
                        templateUrl: 'webPortal/js/common/logged-in/leftMenuView.html'
                    },
                    'contentBox': {
                        templateUrl: 'webPortal/js/common/contentBoxView.html'
                    }
                }
            })
            .state('main.loggedIn.home', {
                url: '/dashboard',
                views: {
			  		'content': {
			  			templateUrl: 'webPortal/js/dashboard/dashboardView.html',
			 			controller: 'dashboardController'
			  		}
			  	}
            })
            .state('main.loggedIn.contentUpload', {
                url: '/content/upload',
                views: {
                    'content': {
                        templateUrl: 'webPortal/js/content/uploadView.html',
                        controller: 'uploadController'
                    }
                }
            })
            .state('main.loggedIn.selfLearning', {
                url: '/selfLearning',
                views: {
                    'content': {
                        templateUrl : 'webPortal/js/self-learning/selfLearningView.html',
                        controller : 'selfLearningController',
                        controllerAs : 'selfLearning'
                    }
                }
            })
            .state('main.loggedIn.teachingAids', {
                url: '/teachingAids',
                views : {
                    'content' : {
                        templateUrl : 'webPortal/js/teaching-aids/teachingAidsView.html',
                        controller : 'teachingAidsController',
                        controllerAs : 'teachingAids'
                    }
                }
            });
            // define login route
            loginModalStateProvider.state('main.notLoggedIn.home.login', {
                url: '/login',
                templateUrl : 'webPortal/js/login/loginView.html',
                controller:'loginController',
                controllerAs : 'login'
            });
            loginModalStateProvider.state('main.notLoggedIn.home.requestotp', {
                url: '/requestOtp',
                templateUrl : 'webPortal/js/login/requestOtpView.html',
                controller:'requestOtpController',
                controllerAs : 'requestOTP'
            });
            loginModalStateProvider.state('main.notLoggedIn.home.setpassword', {
                url: '/setPassword',
                templateUrl : 'webPortal/js/login/setPasswordView.html',
                controller:'setPasswordController',
                controllerAs : 'setPassword'
            });
    }]);
/*
 angular.module("mitraPortal").run(
 ['$templateCache', function($templateCache){
 $templateCache.put('node_modules/angular-ui-bootstrap/template/accordion/accordion-group.html', undefined);
 }
 ]);
 */
// run.$inject = ['$rootScope', '$location', '$cookies', '$http'];
// function run($rootScope, $location, $cookies, $http) {
//
//     // keep user logged in after page refresh
//     $rootScope.globals = $cookies.getObject('globals') || {};
// //    if ($rootScope.globals.currentUser) {
// //        $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.token;
// //    }
//
//     $rootScope.$on('$locationChangeStart', function (event, next, current) {
//         // redirect to login page if not logged in and trying to access a restricted page
//         var restrictedPage = ($location.path()).indexOf('/login') === -1;
//         var loggedIn = $rootScope.globals.currentUser;
//         if (restrictedPage && !loggedIn) {
//             $location.path('/login');
//         }
//     });
// }