#from django.contrib.auth.models import User
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import list_route

from commons.models import code , news, configuration
from commons.serializers import codeSerializer , newsSerializer 
from mitraEndPoints import constants
from datetime import datetime
from users.models import token
 
 
class CodeViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """

    queryset = code.objects.all().order_by('codeNameEn')
    serializer_class = codeSerializer
        
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
        

class NewsViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to read the news
    """
    queryset = news.objects.all().order_by('-createdOn')
    serializer_class = newsSerializer
    
    def list(self, request):
        queryset = news.objects.all().order_by('-createdOn')
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
