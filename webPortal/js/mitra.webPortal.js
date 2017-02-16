//var mitraPortal = angular.module("mitraPortal", ['ngCookies','ngMessages','ui.router','ui.bootstrap']);

var mitraPortal = angular.module("mitraPortal", ['ngCookies','ngMessages','ui.router','ui.bootstrap']);
mitraPortal.
config(['$stateProvider', '$urlRouterProvider','loginModalStateProvider','$locationProvider',
    function config($stateProvider, $urlRouterProvider, loginModalStateProvider, $locationProvider) {

    //$locationProvider.html5Mode(true);
	 $urlRouterProvider.otherwise('/home');

	$stateProvider
	.state('main', {
    abstract: true,
    url: '',
    templateUrl: '/js/common/mitraLayoutView.html'
  })
  .state('main.index', {
	abstract: true,
    url: '',
    views: {
    	'header': {
    		templateUrl: '/js/common/headerView.html'
    	},
    	'leftMenu': {
    		templateUrl: '/js/common/leftMenuView.html'
    	},
    	'contentBox': {
    		templateUrl: '/js/common/contentBoxView.html'
    	},
    },
  })
  .state('main.index.home', {
  	url: '/home',
  	views: {
  		'content': {
  			templateUrl: '/js/home/layoutView.html'
  			//template: "Setting up home layout"
  		},
    	'welcome@main.index.home': {
    		templateUrl: '/js/home/welcomeView.html',
    		controller: 'welcomeController'
    	},
    	'map@main.index.home': {
    		templateUrl: '/js/home/mapView.html',
        controller: function($scope) {
        	console.log('hit map controller');
        }
    	}
    }
  })
  .state('main.index.contentUpload', {
  	url: '/content/upload',
  	views: {
  		'content': {
  			templateUrl: '/js/content/uploadView.html',
  			controller: 'uploadController'
  		}
  	}
  });
  // define login route
    loginModalStateProvider.state('main.index.home.login', {
        url: '/login',
        templateUrl : '/js/login/loginView.html',
        controller:'loginController',
        controllerAs : 'login'
    });
    loginModalStateProvider.state('main.index.home.requestotp', {
        url: '/requestOtp',
        templateUrl : '/js/login/requestOtpView.html',
        controller:'requestOtpController',
        controllerAs : 'requestOTP'
    });
    loginModalStateProvider.state('main.index.home.setpassword', {
        url: '/setPassword',
        templateUrl : '/js/login/setPasswordView.html',
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
