
from __future__ import print_function
import httplib2
import os

from apiclient import discovery
from oauth2client import client
from oauth2client import tools
from oauth2client.file import Storage

import datetime

class EventsCalender:
	# If modifying these scopes, delete your previously saved credentials
	# at ~/.credentials/calendar-python-quickstart.json
	SCOPES = 'https://www.googleapis.com/auth/calendar'
	CLIENT_SECRET_FILE = 'client_secret.json'
	APPLICATION_NAME = 'Google Calender'

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
	
	"Gets upcoming 10 events"
	def getEvents(self):
		now = datetime.datetime.utcnow().isoformat() + 'Z' # 'Z' indicates UTC time
		print('Getting the upcoming 10 events')
		eventsResult = self.__service.events().list(
			calendarId='primary', timeMin=now, maxResults=10, singleEvents=True,
			orderBy='startTime').execute()
		events = eventsResult.get('items', [])

		if not events:
			print('No upcoming events found.')
		for event in events:
			start = event['start'].get('dateTime', event['start'].get('date'))
			print(start, event['summary'])


objCalender=EventsCalender()
objCalender.getEvents()

