angular.module("mitraPortal").controller("previewNewsController",
    ['$scope', '$stateParams', '$state', '$window', '$log', '$http', 'appUtils', 'appConstants', 'commonService',
        'newsService','$animate',
        function ($scope, $stateParams, $state, $window, $log, $http, appUtils, appConstants, commonService,newsService,$animate) {

      $scope.objNews = [];
  	  $scope.myInterval = 0;
	  $scope.myTransition = false;
	  $scope.noWrapSlides = false;
	  $scope.activeCarousel1 = 0;
	  $scope.activeNewsCarousel = 0;
	  $animate.enabled(true);  
	  $scope.myInterval = 3000;
	  $scope.myDate;

	  
	$scope.playVideo = function ( url ) {
		 $window.open(url, '_blank')
	};
	
	$scope.goTo = function ( state ) {
		$state.go(state)
	};
    	
    $scope.acceptedFileTypes = {
        "108100": "",              //Video
        "108101": "audio/*",       //Audio
        "108102": ".ppt,.pptx",    //PPT
        "108103": ".xls,.xlsx",    //Worksheet
        "108104": ".pdf",          //PDF
        "108105": ""             //Ek Step
    };
    $scope.inputs = {};
    $scope.isAdmin = appUtils.isAdmin();
    $scope.isTeacher = appUtils.isTeacher();

    $log.debug($scope.isAdmin, $scope.isTeacher, appUtils.isAdmin(), appUtils.isTeacher());

    $scope.mode = "PREVIEW"; // can be "EDIT" or "PPREVIEW" or "GIVE FEEDBACK"
    $scope.news = {};
    $scope.newsEditable = false;

            
        $scope.setDirty = function (form) {
            angular.forEach(form.$error.required, function (field) {
                field.$dirty = true;
            });
        };


        $scope.setSelectedOption = function (selectedOption) {
            if ($scope.mode == 'EDIT') {
                if ($scope.selectedOption == selectedOption) {
                    $scope.selectedOption = null;
                }
                else {
                    $scope.selectedOption = selectedOption;
                }
            }
        };

        $scope.setMode = function (mode) {
            if ($scope.mode == 'EDIT' && mode == 'PREVIEW' && $scope.originalContent) 
            {
                $scope.content = JSON.parse(JSON.stringify($scope.originalContent)); //deepcopy
            }
            $window.scrollTo(0, 0);
            $scope.mode = mode;
        };

        //publish the news
        $scope.publish = function () {
            var nextState = null;
            nextState = 'main.loggedIn.newsList';
            $state.go(nextState);
        };
        
        //Check string is empty or null
        var isUndefinedOrNull = function(val) 
        {
            return angular.isUndefined(val) || val === null 
        }
        
        $scope.selectedImageNumber = "";
        
        // Set selected image number.
        $scope.imageUpload = function (selectedImageNumber) {
      	  $log.debug(selectedImageNumber);
      	  $scope.selectedImageNumber = "";
          $scope.selectedImageNumber = selectedImageNumber;  
        }
        

        
        // remove news PDF
        $scope.removePDF = function (newsID,pdfName) {
        	console.log("removePDF");
        	console.log(newsID);
        	console.log(pdfName);
        	deleteNewsPDF(newsID,pdfName);
        	$scope.news.pdfFileURL = "Pdf deleted sucessfully";
          }
        
        // delete the news PDF
        var deleteNewsPDF = function(newsID,pdfName) {
            var nextState = null;

            var options = {};
            var data = {
                "newsID": newsID
            };
            options.data = data;
            options.url = 'news/removeNewsPDF/';
            options.headers = {"authToken": appUtils.getFromCookies("token", "")};

            appUtils.ajax(options,
                function (responseBody) {
                    $log.debug(responseBody);
                   // $state.go(nextState);
                   // $window.scrollTo(0, 0);
                 
//                    $state.transitionTo($state.current, $stateParams, {
//                        reload: true,
//                        inherit: false,
//                        notify: true
//                    });
                    
                },
                function (responseBody) {
                    $log.debug(responseBody);
                }
            );
        };
        
        // remove image news
        $scope.removeImage = function (newsID,imageNumber,imageURL) {
        	console.log("ERRORRRRRRR");
        	console.log(newsID);
        	console.log(imageNumber);
        	console.log(imageURL);
        	$scope.AllImageArray[imageNumber - 1] = "";
        	deleteNewsImage(newsID,imageNumber,imageURL);
          }
        
        var deleteNewsImage = function(newsID,imageNumber,imageURL) {
            var nextState = null;
            var options = {};
            var data = {
                "newsID": newsID,
                "imageNumber": imageNumber,
                "imageURL":imageURL
            };
            options.data = data;
            options.url = 'news/removeNewsImage/';
            options.headers = {"authToken": appUtils.getFromCookies("token", "")};

            appUtils.ajax(options,
                function (responseBody) {
                    $log.debug("success...IMAGE DELETEDDDDDDDDDDDDDD");
                    console.log(responseBody);
                   // $window.scrollTo(0, 0);
                 
//                    $state.transitionTo($state.current, $stateParams, {
//                        reload: true,
//                        inherit: false,
//                        notify: true
//                    });
                    
                },
                function (responseBody) {
                    $log.debug("error .IMAGE NOTTTDELETEDDDDDDDDDDDDDD");
                    $log.debug(responseBody);
                    
                }
            );
        };
        
        

        
        
        // Save/update the news details
        $scope.saveChanges = function () {
            $log.debug("IN saveChanges function");
            var fd = new FormData();

            for (var key in $scope.news) {
                $log.debug(key);
                $log.debug($scope.news[key]);
                if ($scope.news[key]) {
                    fd.append(key, $scope.news[key]);
                }

            }
            
            fd.append("newsCategoryCodeID", $scope.news.newsCategory); 
            fd.append("departmentCodeID", $scope.news.department);
            fd.append("newsImportanceCodeID", $scope.news.newsImportance);
            fd.append("newsID", $stateParams.newsID);
            
            if(!isUndefinedOrNull($scope.newsImage1))
        	{fd.append('imageOne', $scope.newsImage1);}
            
            if(!isUndefinedOrNull($scope.newsImage2))
        	{fd.append('imageTwo', $scope.newsImage2);}
            
            if(!isUndefinedOrNull($scope.newsImage3))
        	{fd.append('imageThree', $scope.newsImage3);}
            
            if(!isUndefinedOrNull($scope.newsImage4))
    		{fd.append('imageFour', $scope.newsImage4);}
        
            if(!isUndefinedOrNull($scope.newsImage5))
    		{fd.append('imageFive', $scope.newsImage5);}  
            
    		if(!isUndefinedOrNull($scope.myPDFFile))
			{fd.append('pdfFile', $scope.myPDFFile);}
    		
    		if(!isUndefinedOrNull($scope.news.publishDate))
    		{fd.append('publishDate', $scope.news.publishDate);}

            var headers = {
                "authToken": appUtils.getFromCookies("token", ""),
                'Content-Type': undefined
            };

            $http.post(appConstants.endpoint.baseUrl + "news/saveNews/", fd, {
                transformRequest: angular.identity,
                headers: headers
            })
                .then(function success(response) {
                        console.log(response);
                        $window.scrollTo(0, 0);
                        $state.transitionTo($state.current, $stateParams, {
                            reload: true,
                            inherit: false,
                            notify: true
                        });


                    },
                    function error(response) {
                    	console.log("ERRORRRRRRR");
                    	console.log(response);
                    	console.log(fd);
                        $log.debug("fd");
                    });
        };
        
        $animate.enabled(true);  
        
        $scope.myInterval = 3000;
     	
//             $scope.newsSlides = [
//                 { image: 'http://lorempixel.com/400/200/sports', id : 1 },    
//                 { image: 'http://lorempixel.com/400/200/', id : 2 },
//                 { image: 'http://lorempixel.com/400/200/people', id : 3 }, 
//             ]
        
      $scope.newsSlides = []
       
      // From comma seperated string of image URLS build the collection.
        var setNewsSlides = function (newsImageURL) {
            if ($scope.news.imageURL) {
                var newsImageArray = $scope.news.imageURL.split(',');
                $scope.AllImageArray = newsImageArray;
                for (var i = 0; i < newsImageArray.length; i++) 
                {
                	$scope.newsSlides.push({
                		image: newsImageArray[i],
    					id: i
    				})
                }
            }
            
        };

            // Fetch news details in both languages
            var fetchNewsDetails = function () {
                var options = {};
                $log.debug($stateParams.newsID);
                var data = {"newsID": $stateParams.newsID};
                options.data = data;
                options.url = 'news/newsDetail/';
                options.headers = {"authToken": appUtils.getFromCookies("token", "")};

                appUtils.ajax(options,
                    function (responseBody) {
                        //get news set
                        $scope.news = responseBody.data[0];
                        $scope.objNews = $scope.news;
                        
                        $scope.news.publishDate = moment($scope.news.publishDate).toDate();
                        
                        //$scope.AllImageArray = [];
                        // Build the collection from comma seperated string. i.e Comma seperated imageURL
                        setNewsSlides($scope.news.imageURL);

                        //make a copy in case user goes to edit and discards
                        $scope.originalNews = JSON.parse(JSON.stringify($scope.news)); //deepcopy
                    },
                    function (responseBody) {
                        $log.debug($scope.news);
                        $log.debug("error");
                        $log.debug(responseBody);
                    }
                );
            };

            
            // fetch the code list and populate the dropdowns
            var populateDropDowns = function () {
                $scope.NewsCategoryList = commonService.getCodeListPerCodeGroup(
                    appConstants.codeGroup.NewsCategory
                );
                $scope.departmentList = commonService.getCodeListPerCodeGroup(
                    appConstants.codeGroup.department
                );
                $scope.importanceList = commonService.getCodeListPerCodeGroup(
                    appConstants.codeGroup.importance
                );
            };

            $scope.$on('codesAvailable', function (event, data) {
                populateDropDowns();
                fetchNewsDetails();
            });
            
            var init = function () {
                $scope.submitted = false;
                $scope.news = {"newsID": 0, "newsCategoryCodeID": 0};
                $scope.errorMessage = "";
                fetchNewsDetails();
                populateDropDowns();
               
            	  
            };

            // Call to the init function.
            init();


        }
    ])
    .directive('fileModel', ['$parse', '$log', function ($parse, $log) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {


                element.bind('change', function () {
                	
                	
                	switch (scope.selectedImageNumber) {
                    case 1:
                    	scope.$parent.newsImage1 = element[0].files[0];
                        break;
                    case 2:
                    	scope.$parent.newsImage2 = element[0].files[0];
                        break;
                    case 3:
                    	scope.$parent.newsImage3 = element[0].files[0];
                        break;
                    case 4:
                    	scope.$parent.newsImage4 = element[0].files[0];
                        break;
                    case 5:
                    	scope.$parent.newsImage5 = element[0].files[0];
                        break;
                    case 'pdfFile':
                    	scope.$parent.myPDFFile = element[0].files[0];
                        break;
                    default:
                    	$log.debug("IN switch default");

                }
                	
                	
                    scope.$apply();
                });
            }
        };
    }]);
