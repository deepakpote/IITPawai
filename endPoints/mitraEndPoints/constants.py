import os
import settings
class messages():
    success = 100100
    registration_phone_number_cannot_be_empty = 100101
    registration_phone_number_is_invalid = 100102
    registration_otp_is_invalid = 100103
    registration_user_validation_failed = 100104

    login_token_cannot_be_empty = 100105
    login_user_token_invalid = 100106
    user_userid_cannot_be_empty = 100107    
    teaching_aid_search_contenttype_cannot_be_empty = 100108
    teaching_aid_search_filetype_cannot_be_empty = 100109
    sign_in_user_not_registered = 100110
    registration_user_already_registered = 100111
    authentication_type_cannot_be_empty = 100112
    teaching_aid_search_language_cannot_be_empty = 100113
    teaching_aid_search_no_records_found = 100114
    teaching_aid_search_user_not_exists = 100115
    self_learning_search_filetype_cannot_be_empty = 100116
    self_learning_search_language_cannot_be_empty = 100117
    self_learning_search_no_records_found = 100118
    self_learning_search_user_not_exists = 100119
    
    save_like_user_not_exists = 100120
    save_like_contentid_cannot_be_empty = 100121
    save_like_content_not_exists = 100122
    save_like_hasLiked_cannot_be_empty = 100123
    save_like_hasLike_value_must_be_boolean = 100124

    update_profile_user_not_exists = 100125
    user_userprofile_user_not_exists = 100126
    registration_fcm_device_id_cannot_be_empty = 100127
    signin_fcm_device_id_cannot_be_empty = 100128
    content_download_response__contentid_cannot_be_empty = 100129
    content_download_response_content_not_exists = 100130
    content_download_response_user_not_exists = 100131
    content_share_response__contentid_cannot_be_empty = 100132
    content_share_response_content_not_exists = 100133
    content_share_response_user_not_exists = 100134

    save_usercontent_contentid_cannot_be_empty = 100135
    save_usercontent_user_id_not_exists = 100136
    save_usercontent_content_id_not_exists = 100137
    get_content_response_contentid_cannot_be_empty = 100138
    get_content_response_content_not_exists = 100139
    get_content_response_user_not_exists = 100140
    save_usercontent_user_id_allready_exists = 100141
    usercontent_list_contenttype_codeid_cannnot_be_empty = 100142
    event_list_invalid_input = 100143 
    event_add_invalid_input = 100144 
    event_attend_eventid_cannot_be_empty = 100145 
    event_attend_user_does_not_exists = 100146
    event_attend_user_already_attending_event = 100147

    usercontent_list_user_id_not_exists = 100148
    usercontent_list_contenttype_code_id_does_not_exists = 100149
    usercontent_delete_contentid_cannot_be_empty = 100150
    usercontent_delete_userid_and_contentid_does_not_exists = 100151
    userlanguage_save_languagecode_id_cannot_be_empty = 100152
    userlanguage_save_user_not_exists = 100153
    usercontent_list_no_records_found = 100156


    user_uploadphoto_bytearray_data_cannot_be_empty = 100154
    user_uploadphoto_user_does_not_exists = 100155
    code_list_version_number_must_be_integer = 100157
    code_list_version_number_invalid = 100158
    
    uploadContent_contentTitle_cannot_be_empty = 100159
    uploadContent_contentType_cannot_be_empty = 100160
    uploadContent_fileType_cannot_be_empty = 100161
    uploadContent_fileName_cannot_be_empty = 100162
    uploadContent_user_not_exists = 100163
    uploadContent_subjectCodeID_cannot_be_empty = 100164 
    uploadContent_topicCodeID_cannot_be_empty = 100165
    uploadContent_contentType_invalid = 100166
    uploadContent_contentType_does_not_exists = 100167
    uploadContent_fileType_does_not_exists = 100168
    uploadContent_language_does_not_exists = 100169
    uploadContent_content_upload_failed = 100170 
    uploadContent_gradeCodeID_cannot_be_empty = 100171
    uploadContent_fileName_invaild = 100172
    uploadContent_languageCodeID_cannot_be_empty = 100173
    uploadContent_contentID_does_not_exists = 100174
    
    news_list_department_does_not_exists = 100175
    news_list_publishDate_invalid = 100176
    
    saveCode_userID_cannot_be_empty = 100177
    saveCode_codeGroupID_cannot_be_empty = 100178
    saveCode_codeGroupID_not_exists = 100179
    saveCode_codeNameEn_cannot_be_empty = 100180
    saveCode_userID_not_exists = 100181
    saveCode_codeID_not_exists = 100182
    saveCode_save_code_failed = 100183
    
    setPassword_user_not_exists = 100184
    setPassword_password_should_not_contain_space = 100185
    setPassword_password_cannot_be_empty_it_must_be_gretter_then_six_character = 100186
    
    webSignIn_phone_number_cannot_be_empty = 100187
    webSignIn_password_cannot_be_empty = 100188
    webSignIn_phone_number_is_invalid = 100189
    webSignIn_invalid_credentials = 100190
    

class webportalmessages():    
    web_admin_invalid_token = 200100
    web_admin_phoneno_not_registered = 200101
    web_admin_invalid_pass_key = 200102
    web_admin_valid_user = 200103

class authenticationTypes():
    registration = 110100
    signIn = 110101
    otpValidityHours = 18.5

class sms():
    authId = "MAOTGWZJI2NDAXNJHMMZ"
    authToken = "NjM3MzAxYzc2ZGQ1MTJjOTY5NjgyNDhhNzUxMDZi"
    srcPhoneNumber = "+919822365522"
    registrationMessage = "Thank you for registering with MITRA. Your OTP Code is : "
    signInMessage = "Your MITRA Sign In OTP is : "
    sendSMS = False

class mitraCodeGroup():
    subject = 103 
    grade = 104   
    topic = 105
    fileType = 108
    language = 101
    
class mitraCode():
    teachingAids = 107100
    selfLearning = 107101
    trainings = 107102
    like = 111100
    download = 111101
    share = 111102
    video = 108100

    
class contentSearchRecords():
    default = 20

class language():
    english = 101100
    marathi = 101101

class imageDir():
    path = "/static/user/"
    projectDir = settings.PROJECT_DIR
    currentDir = os.getcwd()
    currentDir = settings.PROJECT_DIR
    baseDir = currentDir + path
    
class fcm():
    FCM_SERVERKEY = "AAAAQH5DkUE:APA91bHzQT7zucQ6A807PrrQwzM63mUrUooqyUt_jJ4HTeR-QM-u2FW9dkAr4r_fOm7G0B2z7iFJdMDz2Nc3s4lZTrNYJ6mIovDvLSui0SpqZAiOZKCPhHsxYGCvDxDa3yo5niGsWd4haNyTKnHol1kCZEa3S2zZbw"
    DATA_NOTIFICATION_TITLE = "Mitra"
    
class webportal():
	AdminPassword = "softcorner"
    
class configurationKey():
    comCodeVersion = "comCodeVersion"