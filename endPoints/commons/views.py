#from django.contrib.auth.models import User
from rest_framework import viewsets, permissions
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import list_route
from django.db.models import Max

from commons.models import code , news, configuration, newsImage , codeGroup, userNews
from commons.serializers import codeSerializer , newsSerializer 
from mitraEndPoints import constants, settings
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
        
        responseData = validateNewListParameters(departmentCodeID, publishFromDate, publishToDate)
        if responseData:
            return Response({"response_message": responseData['message'], "data": []}, status= responseData['status'])
            
       
        newsData = getNewsList(departmentCodeID, publishFromDate, publishToDate, None)     
                     
        serializer = newsSerializer(newsData, many = True)   
        return Response({"response_message": constants.messages.success, "data": serializer.data})
     
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
        
        #get UserID from auth token
        userID  =  getUserIDFromAuthToken(authToken)
        
        # check user is not null 
        if not userID or userID == 0:
            return Response({"response_message": constants.messages.userNews_list_user_id_cannot_be_empty, "data": []}, status = status.HTTP_401_UNAUTHORIZED)
              
        # check userID exists or not
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.userNews_list_user_does_not_exist, "data": []}, status = status.HTTP_404_NOT_FOUND)
        
       
        responseData = validateNewListParameters(departmentCodeID, publishFromDate, publishToDate)
        if responseData:
            return Response({"response_message": responseData['message'], "data": []}, status= responseData['status'])
            
       
        newsData = getNewsList(departmentCodeID, publishFromDate, publishToDate, objUser)     
                     
        serializer = newsSerializer(newsData, many = True)
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
    basicURL = getBaseURL(constants.staticFileDir.newsImageDir)
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
def getNewsList(departmentCodeID, publishFromDate, publishToDate, objUser):
    
    if not objUser:
        queryset = news.objects.all()
    else:
        # Get list of newsID of login user
        objNewsList = list(userNews.objects.filter(user=objUser).values_list('news_id', flat=True))        
        queryset = news.objects.filter(newsID__in=objNewsList)
     
    # check input department code exists or not
    if departmentCodeID:            
        # if department codeID is passed then add filter for department
        queryset = queryset.filter(department=departmentCodeID)    
             
    # if published from date is passed then add filter
    if publishFromDate:
        queryset = queryset.filter(publishDate__gte=publishFromDate)
        
    # if published to date is passed then add filter     
    if publishToDate:
        queryset = queryset.filter(publishDate__lte=publishToDate)
    # descending order of publish date
    queryset = queryset.order_by('-publishDate').values()
               
    for objNew in queryset:
        basicURL = getBaseURL(constants.staticFileDir.newsPDFDir)
        imageList = getNewsImageURL(objNew)            
        objNew['imageURL'] = getNewsImageURL(objNew)
        if objNew['pdfFileURL'] :
              objNew['pdfFileURL'] = basicURL +str(objNew['pdfFileURL'])
   
    return queryset
"""
common function for news filter validation
"""
def validateNewListParameters(departmentCodeID, publishFromDate, publishToDate):
      # check input department code exists or not
    errors ={}
    if departmentCodeID:            
        try:
            objDepartmentCodeID = code.objects.get(codeID=departmentCodeID)
        except code.DoesNotExist:
            errors['message'] = constants.messages.news_list_department_does_not_exists
            errors['status'] = status.HTTP_404_NOT_FOUND     
                 
    # check publish from date is less than publish to date, otherwise raise an error 
    if publishFromDate and publishToDate and publishFromDate > publishToDate :
        errors['message'] = constants.messages.news_list_publishDate_invalid
        errors['status'] = status.HTTP_404_NOT_FOUND
        
    return errors
"""
common function - to get basic url for all files
"""
def getBaseURL(dirName):
    basicURL  = settings.DOMAIN_NAME + settings.STATIC_URL + dirName 
    return basicURL
    