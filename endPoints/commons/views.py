#from django.contrib.auth.models import User
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import list_route

from commons.models import code , news #district,
from commons.serializers import codeSerializer , newsSerializer #districtSerializer
 
#  
# class DistrictViewSet(viewsets.ModelViewSet):
#     """
#     API endpoint that allows users to be viewed or edited.
#     """
#     queryset = district.objects.all().order_by('districtName')
#     serializer_class = districtSerializer

class CodeViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = code.objects.all().order_by('codeNameEn')
    serializer_class = codeSerializer
    
    def list(self, request):
        queryset = code.objects.all()
        serializer = codeSerializer(queryset, many = True)
        return Response({"response_message": "success", "data": serializer.data})
    
class NewsViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to read the news
    """
    queryset = news.objects.all().order_by('createdOn')
    serializer_class = newsSerializer
    
    def list(self, request):
        queryset = news.objects.all()
        serializer = newsSerializer(queryset, many = True)
        return Response({"response_message": "success", "data": serializer.data})
    