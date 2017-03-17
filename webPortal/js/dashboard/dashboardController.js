angular.module("mitraPortal").controller("dashboardController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'contentService', 'commonService', '$state', '$window', 
	  'TeachingAidsService', 'SelfLearningService',
  function($scope, $location, $log, appUtils, appConstants, contentService, commonService, $state, $window, 
		  TeachingAidsService, SelfLearningService) {	  
	  
	  $scope.myInterval = 0;
	  $scope.myTransition = false;
	  $scope.noWrapSlides = false;
	  $scope.activeCarousel1 = 0;
	  $scope.activeCarousel2 = 0;

	  var responseSelfLearning = [];
	  var teachingAidsCollection =  $scope.teachingAidsCollection = [];
	  var selfLearningCollection =  $scope.selfLearningCollection = [];
	  
	  $scope.fetchTeachingAids = function() {
		  var dataFilter = {
			        "subjectCodeIDs" : "",
			        "gradeCodeIDs" : ""
			    };
		  
		  var fileType = ""
		  TeachingAidsService.fetch(fileType, appConstants.statusCodeID.created, dataFilter, onSuccess, onFailure)
		  	function onSuccess(response) {
			  var slides = [];
		  		for(var i = 0; i < response.data.length; i++){
		  			var fileType = response.data[i].fileType;
		  			var fileName = response.data[i].fileName;
		  			var image = "";
		  			if(fileType === appConstants.fileType.video.toString()) {
						image = 'http://img.youtube.com/vi/' + $scope.getFileName(response.data[i].fileName) + '/0.jpg'
					}
		  			
					slides.push({
						contentID : response.data[i].contentID,
//						contentTitle : response.data[i].contentTitle,
//						subjectName : commonService.getValueByCode(response.data[i].subject)[0].codeNameEn,
//						grades : $scope.getGrades(response.data[i].gradeCodeIDs),
//						author : response.data[i].author,
						image : image,
						videoURL : fileName
					})
				}
		  		$scope.makeCollectionOfThree(slides, 0);	
		  		
		  	}
		  	function onFailure(error) {
		  		console.log("error is", error);  
		  	}		  		
	  }
	  
	  $scope.fetchSelfLearningVideos = function() {
		  var dataFilter = {
		            "languageCodeIDs" : "",
		            "topicCodeIDs" : "",
		            "fileTypeCodeID" : appConstants.fileType.video
		        };
		 
		  SelfLearningService.fetch(dataFilter, appConstants.statusCodeID.created, onSuccess, onFailure);

          	function onSuccess(response){
              var slides = [];
              for(var i =0 ; i < response.data.length ; i++) {
		  		  var image = 'http://img.youtube.com/vi/' + $scope.getFileName(response.data[i].fileName) + '/0.jpg';
		  			
                  slides.push({
                	  contentID : response.data[i].contentID,
//                	  topicName : commonService.getValueByCode(response.data[i].topic)[0].codeNameEn,
//                	  language : commonService.getValueByCode(response.data[i].language)[0].codeNameEn
                	  image : image,
                	  videoURL : response.data[i].fileName
                  })
              }
              $scope.makeCollectionOfThree(slides, 1)
          	}
		  	function onFailure(error) {
		  		console.log("error is", error);  
		  	}		  		
	  }
      
      $scope.getGrades = function(gradeCodeIDs) {
    	  var ids = content.gradeCodeIDs.split(",");
    	  var grades = "";
          for (var j = 0 ; j < ids.length ; j++) {
              grades = " Grade " + commonService.getValueByCode(ids[j])[0].codeNameEn;
          }
          return grades;
      }
	  
	  $scope.makeCollectionOfThree = function(slides, type) {
		  for (var i = 0; i < slides.length; i+=3) {
			  var slideGroup = {
					  	id: i/3,
					  	first : slides[i] || {},
			  			second: slides[i+1] || {},
					  	third : slides[i+2] || {}
			  }
			  if(type === 0) {
				  teachingAidsCollection.push(slideGroup); 
			  }
			  else{
				  selfLearningCollection.push(slideGroup)
			  }
		  }
	  };
	  
	 $scope.getFileName = function(url) {
		    var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
		    var match = url.match(regExp);
		    return (match&&match[7].length==11)? match[7] : false;
	  };
	  
	
	$scope.playVideo = function ( url ) {
		 $window.open(url, '_blank')
	};
	
	$scope.goTo = function ( state ) {
		$state.go(state)
	};
	
  	var init = function () { 	  
  	  $scope.fetchTeachingAids();
  	  $scope.fetchSelfLearningVideos();
  	}
  	
  	init();
  }
]);