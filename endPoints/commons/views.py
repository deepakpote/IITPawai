#from django.contrib.auth.models import User
from rest_framework import viewsets, permissions
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import list_route
from django.db.models import Max

from commons.models import code , news, configuration, newsImage , codeGroup
from commons.serializers import codeSerializer , newsSerializer 
from mitraEndPoints import constants
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
            versionNumber = int(appCodeVersionNumber)
        except:
            return Response({"response_message": constants.messages.code_list_version_number_must_be_integer, "data": []},
                            status=status.HTTP_406_NOT_ACCEPTABLE)
        
        # Get the version # stored in DB
        objConfiguration = configuration.objects.filter(key = constants.configurationKey.comCodeVersion).first();
        dbCodeVersionNumber = objConfiguration.value
         
        if dbCodeVersionNumber < appCodeVersionNumber:
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
    def newslist(self, request):
        """ Get the news list
        args:
            request: passed departmentCodeID, publishFromDate and publishToDate as parameter
        returns:
            Response: list of news

        """      
        departmentCodeID = request.data.get('departmentCodeID')        
        publishFromDate = request.data.get('publishFromDate') 
        publishToDate = request.data.get('publishToDate')        
       
        if not departmentCodeID and not publishFromDate and not publishToDate :
            queryset = news.objects.all().order_by('-createdOn').values()
        elif not departmentCodeID:
            if not publishFromDate:
                queryset = news.objects.filter(publishDate__lte = publishToDate).order_by('-createdOn').values()
            else:
                queryset = news.objects.filter( publishDate__gte = publishFromDate).order_by('-createdOn').values()
        else:
            if not publishFromDate and not publishToDate:
                queryset =  news.objects.filter(department = departmentCodeID).order_by('-createdOn').values()                
            elif not publishToDate :
                queryset =  news.objects.filter(department = departmentCodeID, publishDate__gte = publishFromDate).order_by('-createdOn').values()
            else:
                queryset = news.objects.filter(department = departmentCodeID, publishDate__lte = publishToDate).order_by('-createdOn').values()
                         
        for objNew in queryset:
            imageList = getNewsImageURL(objNew)            
            objNew['imageURL'] =  getNewsImageURL(objNew)          
        #print queryset         
        serializer = newsSerializer(queryset, many = True)
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

def getNewsImageURL(NewsObject):
    #declare array
    arrOut = []
    objImageList = newsImage.objects.filter(news= NewsObject['newsID'])
    for objImage in objImageList:
        arrOut.append(objImage.imageURL)
    
    return arrOut;    

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
