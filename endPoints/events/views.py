from django.shortcuts import render

from rest_framework import viewsets,permissions
from rest_framework.response import Response

from mitraEndPoints import constants
from events.serializers import eventQuerySerializer
from events.CalenderService import EventsCalender

class EventViewSet(viewsets.ViewSet):
    """
    API endpoint to fetch list of events from calender
    """
    serializer_class=eventQuerySerializer
    calender = EventsCalender()
    
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def getEventsList(self,request):
        queryParameters=eventQuerySerializer(data=request.data)
        
        if not queryParameters.is_valid():
            return Response({"response_message": constants.messages.event_query_parameters_not_valid, 
                             "data": []
                            },
                            status=status.HTTP_401_UNAUTHORIZED
                            )
        
        lstEvents=calender.listEvents(**(queryParameters.data))
        return Response({"response_message": constants.messages.success, "data":lstEvents})