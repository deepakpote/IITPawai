angular.module("mitraPortal").service('contentService', ['appUtils', 'appConstants',
	function(appUtils, appConstants) {
    
		var service = {};
	
		service.contentUpload = function (content, successCB, errorCB) {
  		options = {};
  		options.url = appConstants.endpoint.content.upload;
  		
  		options.data = content;
  		/*options.data = {};
  		options.data.contentTitle = content.contentTitle;
  		options.data.contentTypeCodeID = content.contentTypeID;
  		options.data.subjectCodeID = content.subjectID;
  		options.data.gradeCodeIDs = content.gradeID;
  		options.data.contentTitle = content.topicID;
  		options.data.contentTitle = content.contentLanguageID;
  		options.data.fileTypeCodeID = content.fileTypeID;
  		options.data.fileName = content.fileUrl;
  		options.data.instruction = content.instruction;*/
  		
  		appUtils.ajax(options, successCB, errorCB);
		}
	
		return service;
	}
]);