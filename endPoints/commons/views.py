#from django.contrib.auth.models import User
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import list_route

from commons.models import code , news 
from commons.serializers import codeSerializer , newsSerializer 
from mitraEndPoints import constants
from datetime import datetime
 
 
class CodeViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """

    queryset = code.objects.all().order_by('codeNameEn')
    serializer_class = codeSerializer
        
    def list(self, request):
        #Get query param
        l = request.query_params.get('l')

        #If l is 0 mean feth all the code list
        if l == "0" or l is None:
            queryset = code.objects.all()
        # Date time in UTC format    
        else:
            # Convert query param to date time 
            try:
                dt = datetime.strptime(l, "%Y-%m-%d %H:%M:%S")
                queryset = code.objects.filter(createdOn__gte=dt)  
            except : 
                return Response({"response_message": constants.messages.code_list_invalid_date_format,
                             "data": []},
                            status = status.HTTP_404_NOT_FOUND)
            
        serializer = codeSerializer(queryset, many = True)
        return Response({"response_message": constants.messages.success, "data": serializer.data})
        

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