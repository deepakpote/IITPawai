from django.shortcuts import render

from rest_framework import viewsets,permissions
from rest_framework.response import Response
from rest_framework.decorators import detail_route, list_route
from rest_framework import status

from mitraEndPoints import constants
from events.serializers import eventQuerySerializer,eventSerializer,eventModelSerializer
from events.CalenderService import EventsCalender

class EventViewSet(viewsets.ViewSet):
    """
    API endpoint to fetch list of events from calender
    """
    serializer_class = eventQuerySerializer
    calender = EventsCalender()
    
    http_method_names = ['get', 'post']
    
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def listEvents(self, request):
        queryParameters=eventQuerySerializer(data=request.data)
        
        if not queryParameters.is_valid():
            return Response({"response_message": constants.messages.event_query_parameters_not_valid, 
                             "data": []
                            },
                            status=status.HTTP_401_UNAUTHORIZED
                            )
        
        lstEvents=EventViewSet.calender.listEvents(**(queryParameters.data))
        return Response({"response_message": constants.messages.success, "data":lstEvents})
    
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def addEvent(self, request):
        objEventSerializer=eventSerializer(data=request.data)
        
        if not objEventSerializer.is_valid():
            return Response({"response_message": constants.messages.event_query_parameters_not_valid, 
                             "data": []
                            },
                            status=status.HTTP_401_UNAUTHORIZED
                            )
        
        result=EventViewSet.calender.addEvent(objEventSerializer.data)
        objEventModelSerializer=eventModelSerializer()
        objEventModelSerializer.create(result)
        return Response({"response_message": constants.messages.success, "data":result})