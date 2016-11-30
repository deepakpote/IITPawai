from __future__ import print_function
import httplib2
import os

from apiclient import discovery
from oauth2client import client
from oauth2client import tools
from oauth2client.file import Storage

from eventsCalender.serializers import eventSerializer

import datetime

class EventsCalender:
	# If modifying these scopes, delete your previously saved credentials
	# at ~/.credentials/GoogleCalenderCredentials.json
	SCOPES = 'https://www.googleapis.com/auth/calendar'
	CLIENT_SECRET_FILE = 'client_secret.json'
	APPLICATION_NAME = 'Google Calender'
	CALENDER_ID = 'primary'
	
	credential_dir = os.path.join(os.path.expanduser('~'), '.credentials')

	""" Init calender service and returnes service object """
	def __init__(self):
		credentials = self.__get_credentials()
		http = credentials.authorize(httplib2.Http())
		self.__service = discovery.build('calendar', 'v3', http=http)
		
	
	"""Gets valid user credentials from storage.
	If nothing has been stored, or if the stored credentials are invalid,
	the OAuth2 flow is completed to obtain the new credentials.

	Returns:
		Credentials, the obtained credential.
	"""
	def __get_credentials(self):
		if not os.path.exists(EventsCalender.credential_dir):
			os.makedirs(EventsCalender.credential_dir)
		credential_path = os.path.join(EventsCalender.credential_dir,
				           'GoogleCalenderCredentials.json')

		store = Storage(credential_path)
		credentials = store.get()
		if not credentials or credentials.invalid:
			flow = client.flow_from_clientsecrets(EventsCalender.CLIENT_SECRET_FILE, EventsCalender.SCOPES)
			flow.user_agent = EventsCalender.APPLICATION_NAME
			credentials = tools.run_flow(flow, store, None)
			print('Storing credentials to ' + credential_path)
		
		return credentials
	
	# Gets list of events from server
	# input:query parameters (timeMin=datetime.datetime.utcnow().isoformat(), maxResults=10, singleEvents=True,orderBy='startTime' , ...)
	# returns:Array of events in dict format (validated using eventSerializer)		
	def listEvents(self,**kwargs):
		eventsResult = self.__service.events().list(calendarId=EventsCalender.CALENDER_ID,**kwargs).execute()
		return [eventData.data for eventData in [eventSerializer(data=event) for event in eventsResult.get('items', [])] if eventData.is_valid()]
	
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
