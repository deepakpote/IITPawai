angular.module("mitraPortal").controller("dashboardController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'contentService', 'commonService', '$state', '$window',
  function($scope, $location, $log, appUtils, appConstants, contentService, commonService, $state, $window) {	  
	  var responseTeachingAids = {
		  "data": [
			   {
				  "contentID": 257,
				  "contentTitle": "कोन",
				  "contentType": "107100",
				  "gradeCodeIDs": "104103,104104",
				  "subject": "103102",
				  "topic": null,
				  "requirementCodeIDs": "117100,117101,117102,117103",
				  "instruction": "",
				  "fileType": "108100",
				  "fileName": "https://youtu.be/TKKpihENPrA",
				  "author": "thefreemath.org",
				  "objectives": "कोन",
				  "language": "101101"
			  },
			  {
				  "contentID": 258,
				  "contentTitle": "कोन",
				  "contentType": "107100",
				  "gradeCodeIDs": "104103,104104",
				  "subject": "103102",
				  "topic": null,
				  "requirementCodeIDs": "117100,117101,117102,117103",
				  "instruction": "",
				  "fileType": "108100",
				  "fileName": "http://www.youtube.com/watch?v=iwGFalTRHDA",
				  "author": "thefreemath.org",
				  "objectives": "कोन",
				  "language": "101101"
			 },
			 {
				  "contentID": 259,
				  "contentTitle": "कोन",
				  "contentType": "107100",
				  "gradeCodeIDs": "104103,104104",
				  "subject": "103102",
				  "topic": null,
				  "requirementCodeIDs": "117100,117101,117102,117103",
				  "instruction": "",
				  "fileType": "108100",
				  "fileName": "https://youtu.be/TKKpihENPrA",
				  "author": "thefreemath.org",
				  "objectives": "कोन",
				  "language": "101101"
			 },
			 {
				  "contentID": 260,
				  "contentTitle": "कोन",
				  "contentType": "107100",
				  "gradeCodeIDs": "104103,104104",
				  "subject": "103102",
				  "topic": null,
				  "requirementCodeIDs": "117100,117101,117102,117103",
				  "instruction": "",
				  "fileType": "108100",
				  "fileName": "https://youtu.be/TKKpihENPrA",
				  "author": "thefreemath.org",
				  "objectives": "कोन",
				  "language": "101101"
			 },
			 {
				  "contentID": 261,
				  "contentTitle": "कोन",
				  "contentType": "107100",
				  "gradeCodeIDs": "104103,104104",
				  "subject": "103102",
				  "topic": null,
				  "requirementCodeIDs": "117100,117101,117102,117103",
				  "instruction": "",
				  "fileType": "108100",
				  "fileName": "https://youtu.be/TKKpihENPrA",
				  "author": "thefreemath.org",
				  "objectives": "कोन",
				  "language": "101101"
			 }]		  
	  };
	  
	  var responseSelfLearning = {
			  "data": [
				   {
					  "contentID": 262,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "https://youtu.be/TKKpihENPrA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				  },
				  {
					  "contentID": 263,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "http://www.youtube.com/watch?v=iwGFalTRHDA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				 },
				 {
					  "contentID": 264,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "https://youtu.be/TKKpihENPrA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				 },
				 {
					  "contentID": 265,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "https://youtu.be/TKKpihENPrA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				 },
				 {
					  "contentID": 262,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "https://youtu.be/TKKpihENPrA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				  },
				  {
					  "contentID": 262,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "https://youtu.be/TKKpihENPrA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				  },
				  {
					  "contentID": 262,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "https://youtu.be/TKKpihENPrA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				  },
				  {
					  "contentID": 262,
					  "contentTitle": "कोन",
					  "contentType": "107100",
					  "topic": null,
					  "requirementCodeIDs": "117100,117101,117102,117103",
					  "instruction": "",
					  "fileType": "108100",
					  "fileName": "https://youtu.be/TKKpihENPrA",
					  "author": "thefreemath.org",
					  "objectives": "कोन",
					  "language": "101101"
				  }]	  
		  };
	  
	  $scope.myInterval = 0;
	  $scope.myTransition = false;
	  $scope.noWrapSlides = false;
	  $scope.activeCarousel1 = 0;
	  $scope.activeCarousel2 = 0;
	  var teachingAidsCollection =  $scope.teachingAidsCollection = [];
	  var selfLearningCollection =  $scope.selfLearningCollection = [];

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
	  
	 $scope.add = function(slides, type) {
		 response = (type === 0) ? responseTeachingAids : responseSelfLearning;
		 
			for(var i=0; i< response.data.length; i++){
				slides.push({
					videoURL: response.data[i].fileName,
					image: 'http://img.youtube.com/vi/' + $scope.getFileName(response.data[i].fileName) + '/0.jpg',
					contentID: response.data[i].contentID
				})
			}
			return slides;
	};
	
	$scope.playVideo = function ( url ) {
		 $window.open(url, '_blank')
	};
	
	$scope.goTo = function ( state ) {
		$state.go(state)
	};
	
  	var init = function () {
  	  var teachingAids = []; 
  	  var selfLearningVideos = []; 
  	  
  	  teachingAids = $scope.add(teachingAids, 0);
	  $scope.makeCollectionOfThree(teachingAids, 0);
	  
	  selfLearningVideos = $scope.add(selfLearningVideos, 1);
	  $scope.makeCollectionOfThree(selfLearningVideos, 1)
  	}
  	
  	init();
  }
]);