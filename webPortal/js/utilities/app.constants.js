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
		}
	};

	service.localStorage = {
		baseKey: 'mitra',
		codeListKey: 'codeList',
		codeListVersionKey: 'codeListVersion',
		appLanguage: 'appLanguage'
	};

	service.codeGroup = {
		contentLanguage: 101,
		subject: 103,
		grade: 104,
		topic: 105,
		contentType: 107,
		fileType: 108
	};
	
	service.appAvailableLanguages = {
			english: {
				codeID : 113100,
				codeName: 'English',
				shortCode: 'en'
			},
			marathi: {
				codeID : 113101,
				codeName: 'Marathi',
				shortCode: 'mr'
			},
	}

	return service;

}]);
