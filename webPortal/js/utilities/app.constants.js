angular.module("mitraPortal").service('appConstants', ['$window',
  function($window) {

	var service = {};

	console.log($window.location);
	console.log("server url is ");
	console.log(getServerUrl());

	service.endpoint = {

		baseUrl: getServerUrl(),

//		baseUrl: 'http://127.0.0.1:8000/',    //'http://54.152.74.194:8000/',

		code : {
			list: 'code'
		},
		content : {
			upload: 'upload'
		},
		news : {
			add: 'add'
		},
		training : {
			add: 'add'
		}
	};

	service.localStorage = {
		baseKey: 'mitra',
		codeListKey: 'codeList',
		codeListVersionKey: 'codeListVersion'
	};

	service.codeGroup = {
		contentLanguage: 101,
		subject: 103,
		grade: 104,
		topic: 105,
		contentType: 107,
		fileType: 108,
		requirement: 117,
		NewsCategory : 115,
		department : 112,
		importance : 116,
		trainingCategory : 122,
		district: 102,
		block: 119,
		state: 121,
		notificationType : 123
	};

	service.fileTypeCode = {
	    video : 108100
    };

    service.statusCode = {
        created : 114100,
        sentForReview : 114101

    };

    service.contentTypeCode = {
        teachingAids : 107100,
        selfLearning : 107101
    };
    
    service.appLanguages = {
    		english : 113100,
    		marathi : 113101
    };
    
	function getServerUrl() {
	    if($window.location.host.includes("8000") ||
            $window.location.host.includes("localhost") ||
            $window.location.host.includes("192")) {
	        return 'http://maamitra.org.in:8000/';
        } else {
            return 'http://54.152.74.194/';
        }
    }

    service.role = {
    	admin : 1,
    	teacher : 2
    },
	
	service.code = { // News/Content/Training status
			contentOrNewsOrTrainingStatus_Created: 114100,
			contentOrNewsOrTrainingStatus_SentForReview: 114101, 
			contentOrNewsOrTrainingStatus_Published: 114102,
			
			// News Category
			NewsCategory_MAA: 115100,
			
			// Content Category
			contentCategory_TeachingAid: 107100,
			contentCategory_Selflearning: 107101,
			
			// Notification Category
			notificationType_TeachingAid: 123100,
			notificationType_SelfLearning: 123101,
			notificationType_News: 123102,
			notificationType_Training: 123103,
			notificationType_Other: 123104
		}

	return service;

}]);
