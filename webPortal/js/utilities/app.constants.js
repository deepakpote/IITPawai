angular.module("mitraPortal").service('appConstants', [
  function() {

	var service = {};

	service.endpoint = {
		baseUrl: 'http://54.152.74.194:8000/',
		code : {
			list: 'code'
		},
		content : {
			upload: 'upload'
		},
		news : {
			add: 'add'
		}
	};

	service.localStorage = {
		baseKey: 'mitra',
		codeListKey: 'codeList',
		codeListVersionKey: 'codeListVersion'
	}

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
		importance : 116
	}
	
	service.code = { // News/Content/Training status
			contentOrNewsOrTrainingStatus_Created: 114100,
			contentOrNewsOrTrainingStatus_SentForReview: 114101, 
			contentOrNewsOrTrainingStatus_Published: 114102,
			
			// News Category
			NewsCategory_MAA: 115100
		}

	return service;

}]);
