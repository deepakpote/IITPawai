#from django.contrib.auth.models import User
from rest_framework import viewsets, permissions
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import list_route
from django.db.models import Max

from commons.models import code , news, configuration, newsImage , codeGroup, userNews, newsDetail
from commons.serializers import codeSerializer , newsSerializer , customNewsSerializer
from mitraEndPoints import constants, settings , utils
from datetime import datetime
from users.models import token , user
from users.authentication import TokenAuthentication
 
 
class CodeViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """

    queryset = code.objects.all().order_by('codeNameEn')
    serializer_class = codeSerializer
    
    permission_classes=[permissions.AllowAny]    
    def list(self, request):
        #Get query param
        appCodeVersionNumber = request.query_params.get('version')
        
        #If version is None set it to 1
        if appCodeVersionNumber is None:
            appCodeVersionNumber = 1
        
        # verify that the version number passed in, is an integer.
        try:
            intAppVersionNumber = int(appCodeVersionNumber)
        except:
            return Response({"response_message": constants.messages.code_list_version_number_must_be_integer, "data": []},
                            status=status.HTTP_406_NOT_ACCEPTABLE)
        
        # Get the version # stored in DB
        objConfiguration = configuration.objects.filter(key = constants.configurationKey.comCodeVersion).first();
        dbCodeVersionNumber = objConfiguration.value
        intDBVersionNumber = int(dbCodeVersionNumber)
         
        if intDBVersionNumber < intAppVersionNumber:
            return Response({"response_message": constants.messages.code_list_version_number_invalid, "data": []},
                            status=status.HTTP_417_EXPECTATION_FAILED)
        
        
        response = {}
        response['version'] = dbCodeVersionNumber
        response['codeList'] = []
        if dbCodeVersionNumber == appCodeVersionNumber:
                return Response({"response_message": constants.messages.success, "data": [response]})
            
        queryset = code.objects.all()
            
        serializer = codeSerializer(queryset, many = True)
        response['codeList'] = serializer.data

        return Response({"response_message": constants.messages.success, "data": [response]})
    
    """
    API to save com code.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def saveCode(self,request):
        # Get input data
        codeID = request.data.get('codeID') 
        codeGroupID = request.data.get('codeGroupID') 
        codeNameEn = request.data.get('codeNameEn') 
        codeNameMr = request.data.get('codeNameMr')  
        displayOrder = request.data.get('displayOrder') 
        comment = request.data.get('comment') 
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
        #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
        
        # check userID.
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.saveCode_userID_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # validate user information
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.saveCode_userID_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
        
        # check codeGroupID is passed as parameter 
        if not codeGroupID:
            return Response({"response_message": constants.messages.saveCode_codeGroupID_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # validate codeGroupID
        try:
            objCodeGroup = codeGroup.objects.get(codeGroupID = codeGroupID)
        except codeGroup.DoesNotExist:
            return Response({"response_message": constants.messages.saveCode_codeGroupID_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)

        # check codeNameEn is passed as parameter in post    
        if not codeNameEn:
            return Response({"response_message": constants.messages.saveCode_codeNameEn_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
      
        try:
            # Check codeID is provided or not.
            if not codeID or codeID == 0:
                #Get latest codeID from codeGroup to insert new code.
                codeID = getLatestCodeIDfromCodeGroup(codeGroupID)
                
                # Save the content.
                ObjCode =code.objects.create(
                                            codeID = codeID , 
                                            codeGroup = objCodeGroup , 
                                            codeNameEn = codeNameEn.strip() ,
                                            codeNameMr = codeNameMr.strip() ,
                                            displayOrder = displayOrder ,
                                            comment = comment.strip() ,
                                            createdBy = objUser ,
                                            modifiedBy = objUser
                                            )
                
                ObjCode.save()      
                
            else:
                # If codeID parameter is passed, then check codeID exists or not and update the code details.       
                try:
                    objcode = code.objects.get(codeID = codeID)
                except code.DoesNotExist:
                    return Response({"response_message": constants.messages.saveCode_codeID_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                 
                # If contentID valid, update the details.
                code.objects.filter(codeID = codeID).update(
                                                            codeNameEn = codeNameEn.strip(),
                                                            codeNameMr = codeNameMr.strip(),
                                                            displayOrder = displayOrder,
                                                            comment = comment.strip(),
                                                            createdBy = objUser,
                                                            modifiedBy = objUser
                                                            )
        
        except Exception as e:
            # Error occured while saving the code.
            print e
            return Response({"response_message": constants.messages.saveCode_save_code_failed,
                     "data": []},
                     status = status.HTTP_400_BAD_REQUEST)
            
        # Return the success response.
        return Response({"response_message": constants.messages.success, "data": []})
        

class NewsViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to read the news
    """
    queryset = news.objects.all().order_by('-createdOn')
    serializer_class = newsSerializer

    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def newsList(self, request):
        """ Get the news list
        args:
            request: passed departmentCodeID, publishFromDate and publishToDate as parameter
        returns:
            Response: list of news

        """      
        departmentCodeID = request.data.get('departmentCodeID')        
        publishFromDate = request.data.get('publishFromDate') 
        publishToDate = request.data.get('publishToDate')      
        newsCategoryCodeID = request.data.get('newsCategoryCodeID')
        statusCodeID = request.data.get('statusCodeID')
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')
                    
        # Check if statusCodeID is passed.
        if not statusCodeID or statusCodeID is None:  
            statusCodeID = constants.mitraCode.published
        else:
            # If statusCodeID parameter is passed, then check user is exists or not
            try:
                objStatusCode = code.objects.get(codeID = statusCodeID)
            except code.DoesNotExist:
                return Response({"response_message": constants.messages.news_list_status_does_not_exists,
                                 "data": []},
                                status = status.HTTP_404_NOT_FOUND)
        
        # validate News List Parameters
        responseData = validateNewListParameters(departmentCodeID, publishFromDate, publishToDate, newsCategoryCodeID , appLanguageCodeID)
        
        # Validation failed
        if responseData:
            return Response({"response_message": responseData['message'], "data": []}, status= responseData['status'])
        
        # Get filtered news data.
        newsData = getNewsList(departmentCodeID, publishFromDate, publishToDate, None , newsCategoryCodeID, statusCodeID , appLanguageCodeID)    
        
        #If result set id empty.
        if not newsData:
            return Response({"response_message": constants.messages.news_list_no_records_found,
                     "data": []},
                    status = status.HTTP_200_OK) 
                      
        serializer = customNewsSerializer(newsData, many = True)
        
        # Create object of common class 
        objCommon = utils.common()
        
        basicURL = objCommon.getBaseURL(constants.staticFileDir.newsPDFDir)
        
        #basicURL = getBaseURL(constants.staticFileDir.newsPDFDir)
        for objNew in serializer.data :
            if objNew['pdfFileURL'] :
                objNew['pdfFileURL'] = basicURL +str(objNew['pdfFileURL'])
                
        return Response({"response_message": constants.messages.success, "data": serializer.data})
    
    """
    API to save news.
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def saveNews(self,request):
        # get inputs
        newsID = request.data.get('newsID')
        
        engNewsTitle = request.data.get('engNewsTitle')
        marNewsTitle = request.data.get('marNewsTitle')
        
        engContent = request.data.get('engContent')
        marContent = request.data.get('marContent') 
        
        engAuthor = request.data.get('engAuthor')
        marAuthor = request.data.get('marAuthor')
        
        engTags = request.data.get('engTags')
        marTags = request.data.get('marTags')
        
        newsCategoryCodeID = request.data.get('newsCategoryCodeID') 
        publishDate = request.data.get('publishDate') 
        departmentCodeID = request.data.get('departmentCodeID')
        newsImportanceCodeID = request.data.get('newsImportanceCodeID')
        statusCodeID = request.data.get('statusCodeID')
        pdfFileURL = request.data.get('pdfFileURL')
        
        #Get user token
        authToken = request.META.get('HTTP_AUTHTOKEN')
        
       #Get userID from authToken
        userID = getUserIDFromAuthToken(authToken)
               
        # Check if userID is passed in post param
        if not userID:
            return Response({"response_message": constants.messages.user_userid_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
        
        
        # Check if NewsTitle for english is passed in post param
        if not engNewsTitle or engNewsTitle is None or engNewsTitle.isspace():
            return Response({"response_message": constants.messages.saveNews_newsTitle_english_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if NewsTitle for marathi is passed in post param
        if not marNewsTitle or marNewsTitle is None or marNewsTitle.isspace():
            return Response({"response_message": constants.messages.saveNews_newsTitle_marathi_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
            
        # Check if newsCategoryCodeID is passed in post param
        if not newsCategoryCodeID and newsCategoryCodeID != 0:
            return Response({"response_message": constants.messages.saveNews_newsCategory_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
        
        # Check if departmentCodeID is passed in post param
        if not departmentCodeID and departmentCodeID != 0:
            return Response({"response_message": constants.messages.saveNews_departmentCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED) 
                     
        # Check if newsImportanceCodeID is passed in post param
        if not newsImportanceCodeID and newsImportanceCodeID != 0:
            return Response({"response_message": constants.messages.saveNews_newsImportanceCodeID_cannot_be_empty,
                     "data": []},
                     status = status.HTTP_401_UNAUTHORIZED)    
              
        # Check if statusCodeID is passed in post param
        if not statusCodeID:
            return Response({"response_message": constants.messages.saveNews_statusCodeID_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
                  
        # If userID parameter is passed, then check user exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.saveNews_user_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
        # If statusCodeID parameter is passed, then check status exists or not
        try:
            objStatus = code.objects.get(codeID = statusCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.saveNews_status_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
        # If newsCategoryCodeID parameter is passed, then check newsCategoryCodeID exists or not
        try:
            objnewsCategory = code.objects.get(codeID = newsCategoryCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.saveNews_newsCategory_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
             
        # If departmentCodeID parameter is passed, then check departmentCodeID exists or not    
        try:
            objDepartment = code.objects.get(codeID = departmentCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.saveNews_department_does_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)
        
        # If newsImportanceCodeID parameter is passed, then check newsImportanceCodeID exists or not        
        try:
            objNewsImportance = code.objects.get(codeID = newsImportanceCodeID)
        except code.DoesNotExist:
            return Response({"response_message": constants.messages.saveNews_newsImportance_does_not_exists,
                     "data": []},
                    status = status.HTTP_404_NOT_FOUND)  
                       
        # Get app language instances for english and marathi.
        objAppLanguageEng = code.objects.get(codeID = constants.appLanguage.english)
        objAppLanguageMar = code.objects.get(codeID = constants.appLanguage.marathi)
        
        try:
            # Check newsID is provided or not.
            if not newsID or newsID == 0:
                # Save the newsID.
                objNews =news.objects.create(department = objDepartment, 
                                            publishDate = publishDate,
                                            pdfFileURL = pdfFileURL,
                                            newsCategory = objnewsCategory,
                                            newsImportance = objNewsImportance,
                                            status = objStatus,
                                            createdBy = objUser,
                                            modifiedBy = objUser)
                
                objNews.save()
                
                
                #Save the newsID details for multiple language.
                newsDetail.objects.bulk_create(
                                                    [
                                                    newsDetail(news = objNews,
                                                                  appLanguage = objAppLanguageEng,
                                                                  newsTitle = engNewsTitle.strip(),
                                                                  author = engAuthor , 
                                                                  content = engContent,
                                                                  tags = engTags),
                                                    newsDetail(news = objNews,
                                                                  appLanguage = objAppLanguageMar ,
                                                                  newsTitle = marNewsTitle.strip(), 
                                                                  author = marAuthor , 
                                                                  content = marContent,
                                                                  tags = marTags),
                                                    ]
                                                 )
             
                newsID =  objNews.newsID 
            
            else:
                # If news parameter is passed, then check news exists or not and update the newsID details.       
                try:
                    objnews = news.objects.get(newsID = newsID)
                except news.DoesNotExist:
                    return Response({"response_message": constants.messages.uploadContent_contentID_does_not_exists,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
                 
                # If newsID valid, update the details.
                news.objects.filter(newsID = newsID).update(department = objDepartment, 
                                                            publishDate = publishDate,
                                                            pdfFileURL = pdfFileURL,
                                                            newsCategory = objnewsCategory,
                                                            newsImportance = objNewsImportance,
                                                            status = objStatus,
                                                            createdBy = objUser,
                                                            modifiedBy = objUser)
                
                # update news details for english language.
                newsDetail.objects.filter(news = objnews,
                                             appLanguage = objAppLanguageEng).update(newsTitle = engNewsTitle.strip(),
                                                                                      author = engAuthor , 
                                                                                      content = engContent,
                                                                                      tags = engTags)
                # update news details for marathi language.
                newsDetail.objects.filter(news = objnews,
                                             appLanguage = objAppLanguageMar).update(newsTitle = marNewsTitle.strip(), 
                                                                                      author = marAuthor , 
                                                                                      content = marContent,
                                                                                      tags = marTags)
                                               
        except Exception as e:
            # Error occured while uploading the content.
            #print e
            return Response({"response_message": constants.messages.saveNews_news_save_failed,
                     "data": []},
                     status = status.HTTP_400_BAD_REQUEST)

        #Return the response
        return Response({"response_message": constants.messages.success, "data": []})
     
    """
     saves users preferred news
    """
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def saveUserNews(self, request):
        #get Input data
        authToken = request.META.get('HTTP_AUTHTOKEN')
        newsID = request.data.get('newsID')
        
        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.save_userNews_user_id_cannot_be_empty, "data": []}, status = status.HTTP_401_UNAUTHORIZED)
        
        if not newsID :
            return Response({"response_message": constants.messages.save_userNews_news_id_cannot_be_empty, "data": []}, status = status.HTTP_401_UNAUTHORIZED)
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.save_userNews_user_does_not_exist, "data": []}, status = status.HTTP_404_NOT_FOUND)
        
        # check news exists or not
        try:
            objNews = news.objects.get(newsID = newsID)
        except news.DoesNotExist:
            return Response({"response_message": constants.messages.save_userNews_news_does_not_exist, "data": []}, status = status.HTTP_404_NOT_FOUND)  
        
        if userNews.objects.filter(user = userID, news = newsID).exists()  :
            return Response({"response_message": constants.messages.save_userNews_newsID_already_saved, "data": []}, status = status.HTTP_200_OK)
                       
        objUserNews = userNews(news = objNews, user = objUser)
        objUserNews.save()
        
        return Response({"response_message": constants.messages.success, "data": []})
    """
    Get user's preferred news
    """   
    @list_route(methods=['post'], permission_classes=[permissions.IsAuthenticated],authentication_classes = [TokenAuthentication])
    def userNewsList(self, request):
        authToken = request.META.get('HTTP_AUTHTOKEN')
        departmentCodeID = request.data.get('departmentCodeID')        
        publishFromDate = request.data.get('publishFromDate') 
        publishToDate = request.data.get('publishToDate')  
        newsCategoryCodeID = request.data.get('newsCategoryCodeID')
        appLanguageCodeID = request.META.get('HTTP_APPLANGUAGECODEID')
                     
        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.userNews_list_user_id_cannot_be_empty, 
                             "data": []}, status = status.HTTP_401_UNAUTHORIZED)
              
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.userNews_list_user_does_not_exist, 
                             "data": []}, status = status.HTTP_404_NOT_FOUND)
        
       
        # Validate input param.
        responseData = validateNewListParameters(departmentCodeID, publishFromDate, publishToDate , newsCategoryCodeID , appLanguageCodeID)
        
        if responseData:
            return Response({"response_message": responseData['message'], 
                             "data": []}, status= responseData['status'])
            
       
        #Get news list for respective user.
        newsData = getNewsList(departmentCodeID, publishFromDate, publishToDate, objUser , newsCategoryCodeID, None , appLanguageCodeID)   
        
        #If result set is empty.
        if not newsData:
            return Response({"response_message": constants.messages.news_list_no_records_found,
                     "data": []},
                    status = status.HTTP_200_OK)   
                     
        # Serialize the news data.
        serializer = customNewsSerializer(newsData, many = True)
        
        # Create object of common class 
        objCommon = utils.common()
        #Get basic URL.
        basicURL = objCommon.getBaseURL(constants.staticFileDir.newsPDFDir)
        
        #Get basic URL.
        #basicURL = getBaseURL(constants.staticFileDir.newsPDFDir)
        
        #Build the pdfFileURL.
        for objNew in serializer.data :
            if objNew['pdfFileURL'] :
                objNew['pdfFileURL'] = basicURL +str(objNew['pdfFileURL'])
        
        return Response({"response_message": constants.messages.success, "data": serializer.data})
        
        
def getCodeIDs(codeGroupID):
    
    #Declare array.
    arrCodeIDs = []

    # Get all codeIDs for respective CodeGroup.
    objCodeList= code.objects.filter(codeGroup = codeGroupID)
    for objCode in objCodeList:
        arrCodeIDs.append(objCode.codeID)
        
    if len(arrCodeIDs) > 0:
        return arrCodeIDs
    
"""
Common function used to get the array from comma seprated string.
"""    
def getArrayFromCommaSepString(CommaSepString):
    
    #Declare array.
    arrOut = []

    if CommaSepString:
        arrOut = CommaSepString.split(',')
        return arrOut
    
    return arrOut

"""
Common function used to get the userID from authToken.
"""   
def getUserIDFromAuthToken(authToken):
    
    #Check input token exists or not.
    try:
        objToken= token.objects.get(token = authToken)
    except token.DoesNotExist:
        #If no token exists. return zero
        return 0
    
    # Convert userID to int.
    userID = int(objToken.user.userID)
    # return userID
    return userID
"""
fuction to get comma separated string of Image URLs for a news
"""
def getNewsImageURL(NewsObject):
    #declare array
    #basicURL = getBaseURL(constants.staticFileDir.newsImageDir)
    
    # Create object of common class 
    objCommon = utils.common()
    #Get basic URL.
    basicURL = objCommon.getBaseURL(constants.staticFileDir.newsImageDir)
    
    arrOut = []
    userImageURL = None
    objImageList = newsImage.objects.filter(news= NewsObject['newsID'])
    for objImage in objImageList:
        if objImage.imageURL:
            arrOut.append(basicURL + str(objImage.imageURL))
    
    userImageURL = ",".join(arrOut)
    # return image url string
    return userImageURL;    

"""
Common function used to get the userID from authToken.
"""   
def getLatestCodeIDfromCodeGroup(codeGroupID):
    
    codeID = None
    
    #objCodeGroup= codeGroup.objects.get(codeGroupID = codeGroupID)
    
    #Check codeID's for  exists or not.
    try:
        objCode = code.objects.filter(codeGroup = codeGroupID).aggregate(Max('codeID'))
    except token.DoesNotExist:
        #If no token exists. return zero
        return 0
    
    # Increment codeID and return codeId
    codeID = objCode['codeID__max'] + 1
    return codeID

"""
Common function to get news List
"""
def getNewsList(departmentCodeID, publishFromDate, publishToDate, objUser , newsCategoryCodeID, statusCodeID , appLanguageCodeID):
    
    if not objUser:
        # get all news of respective appLanguageCodeID.
        queryset = newsDetail.objects.filter(appLanguage = appLanguageCodeID)
    else:
        # Get list of newsID of login user
        objNewsList = list(userNews.objects.filter(user=objUser).values_list('news_id', flat=True))        
        queryset = newsDetail.objects.filter(news__in=objNewsList , appLanguage = appLanguageCodeID)
    
    # check input department code exists or not
    if departmentCodeID:            
        # if department codeID is passed then add filter for department
        queryset = queryset.filter(news__department=departmentCodeID) 
             
    # if published from date is passed then add filter
    if publishFromDate:
        queryset = queryset.filter(news__publishDate__gte=publishFromDate)
        
    # if published to date is passed then add filter     
    if publishToDate:
        queryset = queryset.filter(news__publishDate__lte=publishToDate)
        
    # check input newsCategoryCodeID exists or not
    if newsCategoryCodeID:
        queryset = queryset.filter(news__newsCategory = newsCategoryCodeID)
        
    # check input newsCategoryCodeID exists or not
    if statusCodeID:
        queryset = queryset.filter(news__status = statusCodeID)
        
    # descending order of publish date
    queryset = queryset.order_by('news__publishDate')

    return queryset
"""
common function for news filter validation
"""
def validateNewListParameters(departmentCodeID, publishFromDate, publishToDate, newsCategoryCodeID , appLanguageCodeID):
      # check input department code exists or not
    errors ={}
    
    # Check if appLanguageCodeID is passed in header
    if not appLanguageCodeID:
            errors['message'] = constants.messages.news_list_appLanguageCodeID_cannot_be_empty
            errors['status'] = status.HTTP_401_UNAUTHORIZED    
            return errors
        
    # If appLanguageCodeID parameter is passed, then check appLanguageCodeID is exists or not
    try:
        objAppLanguageCode = code.objects.get(codeID = appLanguageCodeID)
    except code.DoesNotExist:
            errors['message'] = constants.messages.news_list_appLanguageCodeID_not_exists
            errors['status'] = status.HTTP_404_NOT_FOUND    
            return errors
    # Check for departmentCodeID
    if departmentCodeID:            
        try:
            objDepartmentCodeID = code.objects.get(codeID=departmentCodeID)
        except code.DoesNotExist:
            errors['message'] = constants.messages.news_list_department_does_not_exists
            errors['status'] = status.HTTP_404_NOT_FOUND     
            return errors
    
    # Check for newsCategoryCodeID
    if newsCategoryCodeID:            
        try:
            objNewsCategory= code.objects.get(codeID=newsCategoryCodeID)
        except code.DoesNotExist:
            errors['message'] = constants.messages.news_list_newsCategory_does_not_exists
            errors['status'] = status.HTTP_404_NOT_FOUND  
            return errors
                 
    # check publish from date is less than publish to date, otherwise raise an error 
    if publishFromDate and publishToDate and publishFromDate > publishToDate :
        errors['message'] = constants.messages.news_list_publishDate_invalid
        errors['status'] = status.HTTP_404_NOT_FOUND
        
    return errors
    