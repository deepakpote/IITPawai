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
		codeListVersionKey: 'codeListVersion'
	};

	service.codeGroup = {
		contentLanguage: 101,
		subject: 103,
		grade: 104,
		topic: 105,
		contentType: 107,
		fileType: 108,
		requirement: 117
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

    service.role = {
    	admin : 1,
    	teacher : 2
    }

	return service;

}]);
