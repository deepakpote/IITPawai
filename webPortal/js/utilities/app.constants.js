angular.module("mitraPortal").service('appConstants', [
  function() {

	var service = {};

	service.endpoint = {
		baseUrl: 'http://localhost:8000/',
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
		codeListVersionKey: 'codeListVersion'
	}

	service.codeGroup = {
		contentLanguage: 101,
		subject: 103,
		grade: 104,
		topic: 105,
		contentType: 107,
		fileType: 108,
		requirement: 117
	}
	
	service.fileType = {
		video : 108100
	}
	
	service.statusCodeID = {
		published : 114102,
		sentForReview : 114101,
		created : 114100
	}

	return service;

}]);
