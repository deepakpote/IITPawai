from __future__ import print_function
import httplib2
import os

from apiclient import discovery
from oauth2client import client
from oauth2client import tools
from oauth2client.file import Storage

from events.serializers import eventSerializer

import datetime

class EventsCalender:
	# If modifying these scopes, delete your previously saved credentials
	# at ~/.credentials/GoogleCalenderCredentials.json
	SCOPES = 'https://www.googleapis.com/auth/calendar.readOnly'
	CALENDER_ID = 'primary'
	
	CREDENTIAL_DIR = os.path.join(os.path.expanduser('~'), '.credentials')

	""" Init calender service and returnes service object """
	def __init__(self):
		credentials = self.__get_credentials()
		http = credentials.authorize(httplib2.Http())
		self.__service = discovery.build('calendar', 'v3', http=http)
		
	
	""" 
		returns Credentials from stored credential file
	"""
	def __get_credentials(self):
		store = Storage(os.path.join(EventsCalender.CREDENTIAL_DIR,'GoogleCalenderCredentials.json'))
		credentials = store.get()
		return credentials
	
	# Gets list of events from server
	# input:query parameters (timeMin=datetime.datetime.utcnow().isoformat(), maxResults=10, singleEvents=True,orderBy='startTime' , ...)
	# returns:Array of events in dict format (validated using eventSerializer)		
	def listEvents(self,**kwargs):
		eventsResult = self.__service.events().list(calendarId=EventsCalender.CALENDER_ID,**kwargs).execute()
		objSerializer = eventSerializer(data=eventsResult.get('items', []),many=True)
		return objSerializer.data if objSerializer.is_valid() else []
	
	# Adds new event in calender 
	# input : event data dict
	# returns : (server response) if <success> else <False> 
	def addEvent(self,dict_event):
		# pass data to eventSerializer for format validation
		event=eventSerializer(data=dict_event)
		if event.is_valid():
			server_response = self.__service.events().insert(calendarId=EventsCalender.CALENDER_ID, body=event.data,sendNotifications=True).execute()
			return server_response
		else:
			return False
        

#usage
""" Calender object """ 
# objCalender=EventsCalender()

""" Uncomment this to fetch events """

#print(objCalender.listEvents(timeMin=datetime.datetime.utcnow().isoformat()+"Z", maxResults=10, singleEvents=True,orderBy='startTime'))

""" Uncomment following lines to add event """

#print(objCalender.addEvent({
#  'summary': 'Test event',
#  'location': 'Kothrud,pune',
#  'description': 'Calender Demo',
# 'start': {
#    'dateTime': '2016-12-03T09:00:00+05:30',
#    'timeZone': 'Asia/Kolkata',
#  },
#  'end': {
#    'dateTime': '2016-12-04T17:00:00+05:00',
#    'timeZone': 'Asia/Kolkata',
#  },
#  'recurrence': [
#    'RRULE:FREQ=DAILY;COUNT=2'
#  ],
#  'attendees': [
#    {'email': 'thokesaurabh@gmail.com'},
#    {'email': 'rahulkatre007@gmail.com'},
#  ]
#}))
