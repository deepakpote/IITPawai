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
    registration_fcm_device_id_cannot_be_empty = 100127
    signin_fcm_device_id_cannot_be_empty = 100128
    content_download_response__contentid_cannot_be_empty = 100129
    content_download_response_content_not_exists = 100130
    content_download_response_user_not_exists = 100131
    content_share_response__contentid_cannot_be_empty = 100132
    content_share_response_content_not_exists = 100133
    content_share_response_user_not_exists = 100134
    get_content_response_contentid_cannot_be_empty = 100138
    get_content_response_content_not_exists = 100139
    get_content_response_user_not_exists = 100140
    
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

class mitraCodeGroup():
    subject = 103 
    grade = 104   
    topic = 105
    
class mitraCode():
    teachingAids = 107100
    selfLearning = 107101
    trainings = 107102
    like = 110100
    download = 110101
    share = 110102
    
class contentSearchRecords():
    default = 20

class language():
    english = 101100
    marathi = 101101