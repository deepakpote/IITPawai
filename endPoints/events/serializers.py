from rest_framework import serializers

class eventTimeSerializer(serializers.DictField):
	dateTime=serializers.DateTimeField()
	timeZone=serializers.CharField()

class attendeeSerializer(serializers.DictField):
	email=serializers.EmailField()

class remindersSerializer(serializers.DictField):
	useDefault=serializers.BooleanField()

class listAttendeeSerializer(serializers.ListField):
	attendee=attendeeSerializer()

# nested object serializer for event
class eventSerializer(serializers.Serializer):
	summary = serializers.CharField()
	location = serializers.CharField()
	description = serializers.CharField()
	end=eventTimeSerializer()
	start=eventTimeSerializer()
	recurrence=serializers.ListField(serializers.CharField())
	attendees=listAttendeeSerializer()
	reminders=remindersSerializer()

# object serializer for query filter parameters
class eventQuerySerializer(serializers.Serializer):
	timeMin = serializers.CharField()
	timeMax = serializers.CharField()
	orderBy = serializers.CharField()
	singleEvents = serializers.BooleanField()
	maxResults = serializers.IntegerField()
	#subjectCodeIDs = serializers.CharField()
	#districtCodeIDs = serializers.CharField()
	