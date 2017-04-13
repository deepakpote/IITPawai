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
		};

		service.getChapterList = function(subjectId, gradeId,success,failure) {
		    var options = {};
            var authToken = appUtils.getFromCookies("token","");
            options.headers = {"authToken" : authToken , "appLanguageCodeID" : "113101"};
		    options.data = {"subjectCodeID":subjectId,"gradeCodeID":gradeId};
		    options.url = "content/chapterList/";
            options.method = "POST";
		    appUtils.ajax(options, success,failure);
        };
	
		return service;
	}
]);