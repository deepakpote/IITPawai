#from django.contrib.auth.models import User
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework import status
from rest_framework.decorators import list_route

from commons.models import code #district,
from commons.serializers import codeSerializer #districtSerializer
 
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
    