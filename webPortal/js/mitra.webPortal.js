var mitraPortal = angular.module("mitraPortal", ['ngCookies','ngMessages','ui.router','ui.bootstrap']);

angular.module("mitraPortal").
config(['$stateProvider', '$urlRouterProvider',
  function config($stateProvider, $urlRouterProvider) {
	
	$urlRouterProvider.otherwise('/home');

	
	$stateProvider
	.state('main', {
    abstract: true,
    url: '',
    templateUrl: 'common/mitraLayoutView.html'
  })
  .state('main.index', {
	abstract: true,
    url: '',
    views: {
    	'header': {
    		templateUrl: 'common/headerView.html'
    	},
    	'leftMenu': {
    		templateUrl: 'common/leftMenuView.html'
    	},
    	'contentBox': {
    		templateUrl: 'common/contentBoxView.html'
    	},
    },
  })
  .state('main.index.home', {
  	url: '/home',
  	views: {
  		'content': {
  			templateUrl: 'home/layoutView.html'
  			//template: "Setting up home layout"
  		},
    	'welcome@main.index.home': {
    		templateUrl: 'home/welcomeView.html',
    		controller: 'welcomeController'
    	},	
    	'map@main.index.home': {
    		templateUrl: 'home/mapView.html',
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
  			templateUrl: 'content/uploadView.html',
  			controller: 'uploadController'
  		}
  	}
  })
  .state('main.index.dashboard', {
    url: '/dashboard',
 	views: {
  		'content': {
  			templateUrl: 'dashboard/dashboardView.html',
 			controller: 'dashboardController'
  		}
  	}
});
}]);
/*
angular.module("mitraPortal").run(
		  ['$templateCache', function($templateCache){
		    $templateCache.put('node_modules/angular-ui-bootstrap/template/accordion/accordion-group.html', undefined);
		  }
		]);
*/
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
