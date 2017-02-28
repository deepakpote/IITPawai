angular.module("mitraPortal").controller("dashboardController",
  ['$scope', '$location', '$log', 'appUtils', 'appConstants', 'contentService', 'commonService', '$state',
  function($scope, $location, $log, appUtils, appConstants, contentService, commonService, $state) {	  
	  var response = {
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
				  "fileName": "https://youtu.be/TKKpihENPrA",
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
			 },
			 {
				  "contentID": 262,
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
	  
	  $scope.myInterval = 0;
	  $scope.noWrapSlides = false;
	  $scope.active = 0;
	  var slides = $scope.slides = [];
	  var currIndex = 0;
	  var slideCollection =  $scope.slideCollection = [];

	  $scope.makeCollectionOfThree = function() {
		  for (var i = 0; i < slides.length; i+=3) {
			  var slideGroup = {
					  	id: i/3,
					  	first : $scope.slides[i] || {},
			  			second: $scope.slides[i+1] || {},
					  	third : $scope.slides[i+2] || {}
			  }
			  slideCollection.push(slideGroup)
		  }
	  };
	  
	 $scope.getFileName = function(url) {
		    var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
		    var match = url.match(regExp);
		    return (match&&match[7].length==11)? match[7] : false;
	  };
	  
	 $scope.addSlides = function() {
			for(var i=0; i< response.data.length; i++){
				slides.push({
					image: 'http://img.youtube.com/vi/' + $scope.getFileName(response.data[i].fileName) + '/0.jpg',
					contentID: response.data[i].contentID
				})
			}
	};
	
	$scope.goTo = function ( state ) {
		$state.go(state)
	};
	
  	var init = function () {
  	  $scope.addSlides();
	  $scope.makeCollectionOfThree()
  	}
  	
  	init();
  }
]);