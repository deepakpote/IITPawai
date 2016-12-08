from django.shortcuts import render

from rest_framework import viewsets,permissions
from rest_framework.response import Response
from rest_framework.decorators import detail_route, list_route
from rest_framework import status

from mitraEndPoints import constants
from events.serializers import eventQuerySerializer,eventSerializer,userEventModelSerializer
from events.CalenderService import EventsCalender

class EventViewSet(viewsets.ViewSet):
    """
    API endpoint to fetch list of events from calender
    """
    serializer_class = userEventModelSerializer
    calender = EventsCalender()
    
    http_method_names = ['get', 'post']
    
    """
    API to Fetch list of events
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def listEvents(self, request):
        queryParameters = eventQuerySerializer(data=request.data)
        
        if not queryParameters.is_valid():
            return Response({"response_message": constants.messages.event_query_parameters_not_valid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        lstEvents=EventViewSet.calender.listEvents(**(queryParameters.data))
        return Response({"response_message": constants.messages.success, "data":lstEvents})
    
    """
    API to add event
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def addEvent(self, request):        
        objEventSerializer=eventSerializer(data=request.data)
        if not objEventSerializer.is_valid():
            return Response({"response_message": constants.messages.event_add_parameters_not_valid,"data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        result=EventViewSet.calender.addEvent(objEventSerializer.data)
        return Response({"response_message": constants.messages.success, "data":result})
    
    """
    API to attend event
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def attendEvent(self, request):        
        objUserEventSerializer=userEventModelSerializer(data=request.data)
        if not objUserEventSerializer.is_valid():
            return Response({"response_message": constants.messages.event_attend_parameters_not_valid,"data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        objUserEvent=objUserEventSerializer.create(objUserEventSerializer.data)
        return Response({"response_message": constants.messages.success, "data":[]})
    