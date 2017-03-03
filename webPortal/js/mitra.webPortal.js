var mitraPortal = angular.module("mitraPortal", ['ngCookies','ngMessages','ui.router','ui.bootstrap','ngLoadingSpinner']);
mitraPortal.
config(['$stateProvider', '$urlRouterProvider','loginModalStateProvider','$locationProvider',
    function config($stateProvider, $urlRouterProvider, loginModalStateProvider, $locationProvider) {

        var templateChange = function() {
            return "";
        };
        //$locationProvider.html5Mode(true);
        $urlRouterProvider.otherwise('/home');

        $stateProvider
            .state('main', {
                abstract: true,
                url: '',
                templateUrl: templateChange() + '/js/common/mitraLayoutView.html',
                controller : 'mainController'
            })
            .state('main.notLoggedIn', {
                abstract: true,
                url: '',
                views: {
                    'leftMenu': {
                        templateUrl: templateChange() + '/js/common/leftMenuView.html',
                        controller : 'leftMenuController'
                    },
                    'contentBox': {
                        templateUrl: templateChange() + '/js/common/contentBoxView.html'
                    }
                }
            })
            .state('main.notLoggedIn.home', {
                url: '/home',
                views: {
                    'header': {
                        templateUrl: templateChange() + '/js/common/headerView.html'
                    },
                    'content': {
                        templateUrl: templateChange() + '/js/home/layoutView.html'
                        //template: "Setting up home layout"
                    },
                    'welcome@main.notLoggedIn.home': {
                        templateUrl: templateChange() + '/js/home/welcomeView.html',
                        controller: 'welcomeController'
                    },
                    'map@main.notLoggedIn.home': {
                        templateUrl: templateChange() + '/js/home/mapView.html',
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
                    'leftMenu': {
                        templateUrl: templateChange() + '/js/common/logged-in/leftMenuView.html',
                        controller : 'leftMenuController'
                    },
                    'contentBox': {
                        templateUrl: templateChange() + '/js/common/contentBoxView.html'
                    }
                }
            })
            .state('main.loggedIn.home', {
                url: '/admin',
                views: {
                    'header': {
                        templateUrl: templateChange() + '/js/common/logged-in/headerView.html'
                    },
                    'content': {
                        templateUrl: templateChange() + '/js/home/layoutView.html',
                        //template: "Setting up home layout"
                        controller: function($scope,appUtils,$state) {
                            !appUtils.isLoggedInUser() ? $state.go('main.notLoggedIn.home') : console.log("");
                        }
                    },
                    'welcome@main.loggedIn.home': {
                        templateUrl: templateChange() + '/js/home/welcomeView.html',
                        controller: 'welcomeController'
                    },
                    'map@main.loggedIn.home': {
                        templateUrl: templateChange() + '/js/home/mapView.html'
                    }
                }
            })
            .state('main.loggedIn.contentUpload', {
                url: '/content/upload',
                views: {
                    'header': {
                        templateUrl: templateChange() + '/js/common/logged-in/headerView.html',
                        controller : function($scope) {
                            $scope.title = 'Upload Content';
                            $scope.showBackArrow = true;
                        }
                    },
                    'content': {
                        templateUrl: templateChange() + '/js/content/uploadView.html',
                        controller: 'uploadController'
                    }
                }
            })
            .state('main.loggedIn.selfLearning', {
                url: '/selfLearning',
                views: {
                    'header': {
                        templateUrl: templateChange() + '/js/common/logged-in/headerView.html',
                        controller : function($scope) {
                            $scope.title = 'Self Learning';
                            $scope.showBackArrow = true;
                        }
                    },
                    'content': {
                        templateUrl : templateChange() + '/js/self-learning/selfLearningView.html',
                        controller : 'selfLearningController',
                        controllerAs : 'selfLearning'
                    }
                }
            })
            .state('main.loggedIn.teachingAids', {
                url: '/teachingAids',
                views : {
                    'header': {
                        templateUrl: templateChange() + '/js/common/logged-in/headerView.html',
                        controller : function($scope) {
                            $scope.title = 'Teaching Aids';
                            $scope.showBackArrow = true;
                        }
                    },
                    'content' : {
                        templateUrl : templateChange() + '/js/teaching-aids/teachingAidsView.html',
                        controller : 'teachingAidsController',
                        controllerAs : 'teachingAids'
                    }
                }
            })
            .state('main.index.reviewTeachingAids', {        //check proper routing for this.
                url: '/teachingAids/review/:contentID',
                views : {
                    'header': {
                        templateUrl: templateChange() + '/js/common/logged-in/headerView.html'
                    },
                    'content' : {
                        templateUrl: '/js/teaching-aids/review/reviewTeachingAidsView.html',
                        controller: 'reviewTeachingAidsController'
                    }
                },
                params :{
                    'teachingAid' : null,
                    'contentID' : null
                }
            });
            // define login route
            loginModalStateProvider.state('main.notLoggedIn.home.login', {
                url: '/login',
                templateUrl : templateChange() + '/js/login/loginView.html',
                controller:'loginController',
                controllerAs : 'login'
            });
            loginModalStateProvider.state('main.notLoggedIn.home.requestotp', {
                url: '/requestOtp',
                templateUrl : templateChange() + '/js/login/requestOtpView.html',
                controller:'requestOtpController',
                controllerAs : 'requestOTP'
            });
            loginModalStateProvider.state('main.notLoggedIn.home.setpassword', {
                url: '/setPassword',
                templateUrl : templateChange() + '/js/login/setPasswordView.html',
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