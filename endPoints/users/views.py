from django.utils.crypto import get_random_string
from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from rest_framework import status
#from users.models import user 
from rest_framework import viewsets,permissions
from users.serializers import userSerializer, otpSerializer

from users.models import user, otp, token, userSubject, userSkill, userTopic, userGrade, userAuth
from commons.models import code
from mitraEndPoints import constants
import random
import plivo
from datetime import datetime, timedelta
from django.utils import timezone

 
class UserViewSet(viewsets.ModelViewSet):
    """
    API endpoint that allows users to be viewed or edited.
    """
    queryset = user.objects.all().order_by('userName')
    serializer_class = userSerializer

    http_method_names = ['get', 'post']

    """
    API to send OTP
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def requestOtp(self,request):
        phoneNumber = request.data.get('phoneNumber')
        authenticationType = request.data.get('authenticationType')
        
        if not phoneNumber:
            return Response({"response_message": constants.messages.registration_phone_number_cannot_be_empty, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
            
        objOtp = otpSerializer(data = request.data)
        if not objOtp.is_valid():
            return Response({"response_message": constants.messages.registration_phone_number_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # Check if authentication type is not empty
        if not authenticationType:
            return Response ({"response_message": constants.messages.authentication_type_cannot_be_empty, "data": []},
                            status=status.HTTP_406_NOT_ACCEPTABLE)
        
        # Check if the authentication type is 'Registration' or 'SignIn'
        isPhoneNumberRegistered = user.objects.filter(phoneNumber=phoneNumber).exists()
        
        if(isPhoneNumberRegistered and authenticationType == constants.authenticationTypes.registration):
            return Response({"response_message": constants.messages.registration_user_already_registered, "data": []},
                            status=status.HTTP_200_OK)
            
        if (not isPhoneNumberRegistered and authenticationType == constants.authenticationTypes.signIn):
            return Response({"response_message": constants.messages.sign_in_user_not_registered, "data": []},
                            status=status.HTTP_200_OK)
        
        generatedOTP = random.randint(100000, 999999)
        objOtp = otp(phoneNumber = phoneNumber, otp = generatedOTP)
        objOtp.save()
        
        if authenticationType == constants.authenticationTypes.registration:
            otpMessage = constants.sms.registrationMessage
        else:
            otpMessage = constants.sms.signInMessage
        
        # Send OTP SMS Call
        sendOtpSms(phoneNumber, generatedOTP, constants.language.english, otpMessage)
        
        # make call to plivo here
        return Response({"response_message": constants.messages.success, "data":[]})

    """
    API to verify OTP
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def verifyOtp(self,request):
        # get inputs
        phoneNumber = request.data.get('phoneNumber')
        authenticationType = request.data.get('authenticationType')
        otp_string = request.data.get('otp')
        
        # Check if phone # is passed in post param
        if not phoneNumber:
            return Response({"response_message": constants.messages.registration_phone_number_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
        
        # Validate phone #
        objOtp = otpSerializer(data = request.data)
        if not objOtp.is_valid():
            return Response({"response_message": constants.messages.registration_phone_number_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # Check if authentication type is not empty
        if not authenticationType:
            return Response ({"response_message": constants.messages.authentication_type_cannot_be_empty, "data": []},
                            status=status.HTTP_406_NOT_ACCEPTABLE)
        
        # Check if user with given phone number exists or not
        isPhoneNumberRegistered = user.objects.filter(phoneNumber=phoneNumber).exists()
        if authenticationType == constants.authenticationTypes.registration:
            if isPhoneNumberRegistered:
                return Response({"response_message": constants.messages.registration_user_already_registered, "data": []},
                            status=status.HTTP_200_OK)
        
        # If verification is for Sign In, then check if the user with given phone number is registered or not 
        if authenticationType == constants.authenticationTypes.signIn:
            if not isPhoneNumberRegistered:
                return Response({"response_message": constants.messages.sign_in_user_not_registered, "data": []},
                            status=status.HTTP_200_OK)
                
        
        # Check if the OTP is generated in the last 24 hours    
        currentDate = timezone.now()
        otpList = otp.objects.filter(phoneNumber=phoneNumber, otp=otp_string).order_by('-createdOn').first()
        if not otpList:
            return Response({"response_message": constants.messages.registration_otp_is_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # If OTP is generated before 24 hours, return error
        if(otpList.createdOn < (currentDate - timedelta(hours=constants.authenticationTypes.otpValidityHours))):
            return Response({"response_message": constants.messages.registration_otp_is_invalid, "data": []},
                        status=status.HTTP_401_UNAUTHORIZED)
        
        # For registration call, do not send auth token
        if authenticationType == constants.authenticationTypes.registration:
            return Response({"response_message": constants.messages.success, "data": []})
        
        # For sign-in call, send auth token in response
        if authenticationType == constants.authenticationTypes.signIn:   
            objSignedInUser = user.objects.get(phoneNumber = phoneNumber)
            objToken = token.objects.get(user = objSignedInUser)
            response = { 'userID' : objSignedInUser.userID,
                        'token' : objToken.token }
            return Response({"response_message": constants.messages.success, "data": [response]})
        
    """
    API to register user
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def register(self,request):
        # Get input data
        phoneNumber = request.data.get('phoneNumber')
        otp_string = request.data.get('otp')

        # validate user information
        objUserSerializer = userSerializer(data = request.data)#, context={'request': request})
        if not objUserSerializer.is_valid():
            return Response({"response_message": constants.messages.registration_user_validation_failed, "data":[ objUserSerializer.errors]},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # validate OTP
        otpList = otp.objects.filter(phoneNumber = phoneNumber,otp = otp_string).first()
        if not otpList:
            return Response({"response_message": constants.messages.registration_otp_is_invalid, "data":[]},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        
        # If user information is valid, save it
        objUserSerializer.save()
        
        #Once user is saved, update it's created by and modified by fields
        objCreatedUser = user.objects.get(phoneNumber = phoneNumber)
        user.objects.filter(phoneNumber = phoneNumber).update(createdBy = objCreatedUser, modifiedBy = objCreatedUser)
        
        #Save user subject
        subjectCodeIDs = request.data.get('subjectCodeIDs')
        userSubjectSave(subjectCodeIDs, objUserSerializer.instance)
        
        # Save user skill
        skillCodeIDs = request.data.get('skillCodeIDs')
        userSkillSave(skillCodeIDs, objUserSerializer.instance)
         
        # save user grade.
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        userGradeSave(gradeCodeIDs, objUserSerializer.instance)
         
        # save user topics
        topicCodeIDs  = request.data.get('topicCodeIDs')
        userTopicSave(topicCodeIDs, objUserSerializer.instance)
#         
        # Generate auth token for user
        tokenAreadyExists = True
        while tokenAreadyExists:
            # Generate token
            token_string = get_random_string(length = 32)#"qqqqwwwweeee" + str(user.instance.userID)
            
            # Check if that token is already taken up. 
            # If yes re-generate the token in while loop
            # If NO then the generated token is a good pick, break out of while loop
            if token.objects.filter(token = token_string).count() == 0:
                tokenAreadyExists = False
        
        # Save the auth generated token
        objToken = token(user=objUserSerializer.instance, token = token_string).save()
        
        # Add user data, along with the generated token to the response
        #response = objUserSerializer.data
        response = { 'userID' : objCreatedUser.userID }  #objCreatedUser.userID
        response['token'] = token_string
        return Response({"response_message": constants.messages.success, "data": [response]})
    
    """
    API to update user profile.
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def updateProfile(self,request):
        # Get input data
        userID = request.data.get('userID') 
        phoneNumber = request.data.get('phoneNumber') 
        emailID = request.data.get('emailID') 
        userName = request.data.get('userName')
        udiseCode = request.data.get('udiseCode')
        userTypeCodeID = request.data.get('userTypeCodeID')
        preferredLanguageCodeID = request.data.get('preferredLanguageCodeID') 
        districtCodeID = request.data.get('districtCodeID') 
        
        # validate user information
        try:
            objUser = user.objects.get(userID = userID)
        except user.DoesNotExist:
            return Response({"response_message": constants.messages.update_profile_user_not_exists,
                         "data": []},
                        status = status.HTTP_404_NOT_FOUND)
        
        # If user valid, update the details.
        user.objects.filter(userID = userID).update(userName = userName , 
                                                               emailID = emailID ,
                                                               udiseCode = udiseCode , 
                                                               userType = userTypeCodeID , 
                                                               preferredLanguage = preferredLanguageCodeID , 
                                                               district = districtCodeID ,
                                                               modifiedBy = userID)
        
        #Save user subject
        subjectCodeIDs = request.data.get('subjectCodeIDs')
        userSubjectSave(subjectCodeIDs, objUser)
        
        # Save user skill
        skillCodeIDs = request.data.get('skillCodeIDs')
        userSkillSave(skillCodeIDs, objUser)
         
        # save user grade.
        gradeCodeIDs = request.data.get('gradeCodeIDs')
        userGradeSave(gradeCodeIDs, objUser)
         
        # save user topics
        topicCodeIDs  = request.data.get('topicCodeIDs')
        userTopicSave(topicCodeIDs, objUser)
#         
        return Response({"response_message": constants.messages.success, "data": []})
    
    """
    API to login
    """
    @list_route(methods=['post'], permission_classes=[permissions.AllowAny])
    def login(self,request):
        # get inputs
        phoneNumber = request.data.get('phoneNumber')
        authtoken = request.data.get('token')
        
        # Check if phoneNumber is passed in post param
        if not phoneNumber:
            return Response({"response_message": constants.messages.registration_phone_number_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)
            
        # Check if token is passed in post param
        if not authtoken:
            return Response({"response_message": constants.messages.login_token_cannot_be_empty,
                             "data": []},
                             status = status.HTTP_401_UNAUTHORIZED)            
                  
        # Check if phone number exists.
        objUser = user.objects.filter(phoneNumber = phoneNumber).first()
        if not objUser:
            return Response({"response_message": constants.messages.registration_phone_number_is_invalid, "data": []},
                    status=status.HTTP_401_UNAUTHORIZED)

        
        #authenticate user with it's token.
        tokenList = token.objects.filter(user = objUser , token = authtoken).first()
        
        # Check authentication result.
        if not tokenList:
            return Response({"response_message": constants.messages.login_user_token_invalid, "data": []},
                            status=status.HTTP_401_UNAUTHORIZED)
        
        # Fetch userAuth for respective phoneNumber
        objuserAuth = userAuth.objects.filter(loginID = phoneNumber).first()
        
        # If first time login then make entry into the userAuth
        if not objuserAuth:
            userAuth(loginID = phoneNumber, authToken = authtoken, createdBy = objUser , modifiedBy = objUser).save()  
        else:
            #Update the lastLoggedInOn and modifiedOn
#             objuserAuth.lastLoggedInOn = datetime.datetime.now()
#             objuserAuth.modifiedOn = datetime.datetime.now()
            objuserAuth.save()
        
        return Response({"response_message": constants.messages.success, "data": []})
    
#     @list_route(methods=['get','post'], permission_classes=[permissions.AllowAny])
#     def opentoAll(self,request):
#         return Response({"hello"})

#     @list_route(methods=['get','post'])
#     def myinfo(self,request):
#         print request.user
#         return Response(UserSerializer(request.user).data)
def sendOtpSms(recepientPhoneNumber, generatedOtp, languageCodeID, otpMessage):
    
    print ("Entered SMS OTP")
    #Verify the Plivo account
    objPlivo = plivo.RestAPI(constants.sms.authId, constants.sms.authToken)
    
    #Set the SMS Parameters
    params = {
        'src': constants.sms.srcPhoneNumber, # Sender's phone number with country code
        'dst' : recepientPhoneNumber, # Receiver's phone Number with country code
        'text' : otpMessage + str(generatedOtp), # Your SMS Text Message - English
        'url' : "http://example.com/report/", # The URL to which with the status of the message is sent
        'method' : 'POST' # The method used to call the url
    }
        
    #Send SMS Call
    response = objPlivo.send_message(params)
    
    #Print Response
    print (response)
"""
Function to save the user subjects.
"""
def userSubjectSave(subjectCodeIDs, objUser):
    if not subjectCodeIDs:
        return
    
    # Delete all the subjects of respective user from userSubject.
    userSubject.objects.filter(user = userObj).delete()
    
    # save the user subject.
    subjectCodeList = subjectCodeIDs.split(',')
    for subjectCodeID in subjectCodeList:
         objCode = code.objects.get(codeID = subjectCodeID)
         userSubject(subject = objCode, user = objUser).save()
    
    return
"""
Function to save the user skills.
"""
def userSkillSave(skillCodeIDs, userObj):
    if not skillCodeIDs:
        return
    
    # Delete all the skills of respective user from userSkill.
    userSkill.objects.filter(user = userObj).delete()
        
    # save the user skills.
    skillCodeList = skillCodeIDs.split(',')
    for skillCodeID in skillCodeList:
         objCode = code.objects.get(codeID = skillCodeID)
         userSkill(skill = objCode, user = userObj).save()
    
    return
"""
Function to save the user topics.
"""
def userTopicSave(topicCodeIDs, userObj):
    if not topicCodeIDs:
        return
    
    # Delete all the topics of respective user from userTopic.
    userTopic.objects.filter(user = userObj).delete()
        
    # save the user topics.
    topicCodeList = topicCodeIDs.split(',')
    for topicCodeID in topicCodeList:
         objCode = code.objects.get(codeID=topicCodeID)
         userTopic(topic = objCode, user = userObj ).save()
    
    return
"""
Function to save the user grades.
"""
def userGradeSave(gradeCodeIDs , userObj):
    if not gradeCodeIDs:
        return 
    
    # Delete all the grades of respective user from userGrade.
    userGrade.objects.filter(user = userObj).delete()
    
    # save the user Grade.
    gradeCodeList = gradeCodeIDs.split(',')
    for gradeCodeID in gradeCodeList:
         objCode = code.objects.get(codeID = gradeCodeID)
         userGrade(grade = objCode, user = userObj).save()

    return