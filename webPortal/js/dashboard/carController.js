angular.module('mitraPortal').controller('carController', function ($scope) {
  $scope.myInterval = 5000;
  $scope.noWrapSlides = false;
  $scope.active = 0;
  var slides = $scope.slides = [];
  var currIndex = 0;
  var slideCollection =  $scope.slideCollection = [];

  $scope.makeCollectionOfFour = function() {
	  for (var i = 0; i < slides.length; i+=4) {
		  slideCollection.splice(i, 0, slides[i].image) 
		  slideCollection.splice(i+1, 0, slides[i+1].image)
		  slideCollection.splice(i+2, 0, slides[i+2].image)
		  slideCollection.splice(i+3, 0, slides[i+3].image)
	  }
  }
  
  $scope.addSlide = function() {
    var newWidth = 600 + slides.length + 1;
    slides.push({
      image: 'http://lorempixel.com/' + newWidth + '/300',
      contentID: currIndex++
    });
    $scope.makeCollectionOfFour()
  };
  
  for (var i = 0; i < 10; i++) {
    $scope.addSlide();
  }
  
//  $scope.addImage = function() {
//	  slides.push({
//		  image : 'http://www.youtube.com/watch?v=iwGFalTRHDA',
//		  contentID: 570
//	  	},
//	  	{
//		  image: 'http://www.youtube.com/watch?v=iwGFalTRHDA',
//		  contentID: 567
//	  	})
//  }
//  
//  $scope.addImage();
  
});
