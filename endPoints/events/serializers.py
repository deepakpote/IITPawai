from rest_framework import serializers
from events.models import userEvent

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
	id = serializers.CharField(required=True)
	summary = serializers.CharField()
	location = serializers.CharField()
	description = serializers.CharField()
	end=eventTimeSerializer()
	start=eventTimeSerializer()
	#recurrence=serializers.ListField(serializers.CharField(),required=False)
	#attendees=listAttendeeSerializer(required=False)
	#reminders=remindersSerializer(required=False)

# object serializer for query filter parameters
class eventQuerySerializer(serializers.Serializer):
	timeMin = serializers.DateTimeField()
	timeMax = serializers.DateTimeField()
	orderBy = serializers.CharField()
	singleEvents = serializers.BooleanField()
	maxResults = serializers.IntegerField()
	#subjectCodeIDs = serializers.CharField()
	#districtCodeIDs = serializers.CharField()

# model serializer for user and attended event relationship	
class userEventModelSerializer(serializers.ModelSerializer):
    class Meta:
        model = userEvent
        fields = ('event','user')
        
    def create(self, validated_data):
        objEvent = userEvent(eventID=validated_data['event'],userID=validated_data['user'])
        objEvent.save()
        return objEvent