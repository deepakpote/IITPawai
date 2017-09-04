-- Upload content .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100159 as codeID, 100 as codeGroupID, 'ContentTitle cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100160 as codeID, 100 as codeGroupID, 'ContentType cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100161 as codeID, 100 as codeGroupID, 'FileType cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100162 as codeID, 100 as codeGroupID, 'FileName cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100163 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100164 as codeID, 100 as codeGroupID, 'SubjectCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100165 as codeID, 100 as codeGroupID, 'TopicCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100166 as codeID, 100 as codeGroupID, 'Invalid contentType provided' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100167 as codeID, 100 as codeGroupID, 'ContentType does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100168 as codeID, 100 as codeGroupID, 'FileType does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100169 as codeID, 100 as codeGroupID, 'Language does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100170 as codeID, 100 as codeGroupID, 'Content upload failed' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100171 as codeID, 100 as codeGroupID, 'GradeCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100172 as codeID, 100 as codeGroupID, 'Invalied file name' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100173 as codeID, 100 as codeGroupID, 'LanguageCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment;

-- Upload content .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100174 as codeID, 100 as codeGroupID, 'ContentID does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment;


-- Insert code group for App Language.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 113 as codeGroupID, 'App Language' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn
;

-- Insert into code - App Language.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 113100 as codeID, 113 as codeGroupID, 'English' as codeNameEn, 'इंग्रजी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 113101 as codeID, 113 as codeGroupID, 'Marathi' as codeNameEn, ' मराठी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- update com_codeGroup for Content Language
update com_codeGroup set codeGroupName = 'Content Language' where codeGroupID = 101;



INSERT into com_code (codeID, codeGroupID, codeNameEn, `comment`,  createdBy, createdOn, modifiedBy, ModifiedOn)

select 100175 as codeID,  100 as codeGroupID, ' Department does not exists' as codeNameEn, 'Get news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100176 as codeID,  100 as codeGroupID, ' Publish from date should not be greater than publish to date' as codeNameEn, 'Get news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn;


-- Add code .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100177 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save Code' as comment union
Select 100178 as codeID, 100 as codeGroupID, 'CodeGroupID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100179 as codeID, 100 as codeGroupID, 'CodeGroupID does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100180 as codeID, 100 as codeGroupID, 'CodeName English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100181 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100182 as codeID, 100 as codeGroupID, 'Code not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment union
Select 100183 as codeID, 100 as codeGroupID, 'Unable to save code details' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload Content' as comment;


-- Add messages for set password API .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100184 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'set password' as comment union
Select 100185 as codeID, 100 as codeGroupID, 'Password should not contain any space' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'set password' as comment union
Select 100186 as codeID, 100 as codeGroupID, 'Password cannot be empty.It should be gretter then 6 character and less then 255 character' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'set password' as comment;


-- Add messages for webSignIn API .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100187 as codeID, 100 as codeGroupID, 'PhoneNumber cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment union
Select 100188 as codeID, 100 as codeGroupID, 'Password cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment union
Select 100189 as codeID, 100 as codeGroupID, 'Invalid phoneNumber' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment union
Select 100190 as codeID, 100 as codeGroupID, 'Invalid login credentials' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'webSignIn' as comment;



INSERT into com_code (codeID, codeGroupID, codeNameEn, `comment`,  createdBy, createdOn, modifiedBy, ModifiedOn)

select 100191 as codeID,  100 as codeGroupID, 'User does not exists' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100192 as codeID,  100 as codeGroupID, 'News does not exists' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100193 as codeID,  100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100194 as codeID,  100 as codeGroupID, 'NewsID cannot be empty' as codeNameEn, 'Save user news' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100195 as codeID,  100 as codeGroupID, 'Department does not exists' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100196 as codeID,  100 as codeGroupID, 'Publish from date should not be greater than publish to date' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union 
select 100197 as codeID,  100 as codeGroupID, 'You have already saved this news' as codeNameEn, 'Save user news ' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union 
select 100198 as codeID,  100 as codeGroupID, 'User does not exists' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn
union
select 100199 as codeID,  100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, 'Get user''s news list' as comment, 1 as createdBy, now() as createdOn, 1 modifiedBy, now() modifiedOn;


delete from com_code where codeID in (100195, 100196);

update com_code set codeID = 100195 where codeID  = 100198;
update com_code set codeID = 100196 where codeID  = 100199;

-- Add messages for Search teaching Aid,self learning and contentList .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100198 as codeID, 100 as codeGroupID, 'App Language cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search teaching Aid,self learning and contentList ' as comment union
Select 100199 as codeID, 100 as codeGroupID, 'App Language not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search teaching Aid,self learning and contentList ' as comment;


-- Insert code group for content status.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 114 as codeGroupID, 'Content Status' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - status
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 114100 as codeID, 114 as codeGroupID, 'Created' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 114101 as codeID, 114 as codeGroupID, 'Sent For Review' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 114102 as codeID, 114 as codeGroupID, 'Published' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Add messages for Search teaching Aid,self learning and contentList .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100201 as codeID, 100 as codeGroupID, 'Content status cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'content upload ' as comment union
Select 100200 as codeID, 100 as codeGroupID, 'Content status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'content upload ' as comment union
Select 100202 as codeID, 100 as codeGroupID, 'Content title Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'content upload ' as comment;

update com_code
set codeNameEn = 'Content title English cannot be empty'
where codeID = 100159;

-- Add messages for user role list .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100203 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'user role list' as comment union
Select 100204 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'user role list' as comment union
Select 100205 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'user role list ' as comment;

-- Add messages for Search teaching Aid,self learning and contentList .
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100206 as codeID, 100 as codeGroupID, 'fcmRegistrationRequired cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'VerifyOTP ' as comment union
Select 100207 as codeID, 100 as codeGroupID, 'fcmRegistrationRequired value must be boolean' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'VerifyOTP ' as comment;

update com_code set codeNameEn = 'Password cannot be empty.It should be greater then 6 characters and less then 16 characters' 
where codeID = 100186;

-- Add messages for search teaching AID & searchSelfLearning API.
Insert into com_code(codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100208 as codeID, 100 as codeGroupID, 'Status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search teaching aid/selff learning' as comment;


-- Insert code group for Content Requirements.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 117 as codeGroupID, 'Content Requirements' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;
-- Insert into code - Content Requirements.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 117100 as codeID, 117 as codeGroupID, 'Computer' as codeNameEn, 'कॉम्पुटर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117101 as codeID, 117 as codeGroupID, 'Laptop' as codeNameEn, 'लॅपटॉप' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117102 as codeID, 117 as codeGroupID, 'Tab' as codeNameEn, 'टॅब' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117103 as codeID, 117 as codeGroupID, 'Mobile' as codeNameEn, 'मोबाईल' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117104 as codeID, 117 as codeGroupID, 'Projector' as codeNameEn, 'प्रोजेक्टर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 117105 as codeID, 117 as codeGroupID, 'WiFi' as codeNameEn, 'वायफाय' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;


-- save news .
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100209 as codeID, 100 as codeGroupID, 'Content title English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100210 as codeID, 100 as codeGroupID, 'Content title Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100211 as codeID, 100 as codeGroupID, 'News category cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100212 as codeID, 100 as codeGroupID, 'News department cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100213 as codeID, 100 as codeGroupID, 'News importance cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100214 as codeID, 100 as codeGroupID, 'News status cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save News' as comment union
Select 100215 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100216 as codeID, 100 as codeGroupID, 'News status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100217 as codeID, 100 as codeGroupID, 'News category does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100218 as codeID, 100 as codeGroupID, 'News department does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100219 as codeID, 100 as codeGroupID, 'News importance does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment union
Select 100220 as codeID, 100 as codeGroupID, 'News save failed' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'save news' as comment;

-- NewsList & userNewsList.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100221 as codeID, 100 as codeGroupID, 'News category does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100222 as codeID, 100 as codeGroupID, 'News status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100223 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100224 as codeID, 100 as codeGroupID, 'AppLanguageCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment union
Select 100225 as codeID, 100 as codeGroupID, 'AppLanguageCodeID does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'News list' as comment;

-- Messages for contentDetail API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100226 as codeID, 100 as codeGroupID, 'ContentID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content Detail' as comment union
Select 100227 as codeID, 100 as codeGroupID, 'Content does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content Detail' as comment union
Select 100228 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content Detail' as comment;


-- Messages for save content status API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100229 as codeID, 100 as codeGroupID, 'ContentID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100230 as codeID, 100 as codeGroupID, 'StatusCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100231 as codeID, 100 as codeGroupID, 'Content does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100232 as codeID, 100 as codeGroupID, 'Status does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment union
Select 100233 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save content status' as comment;

-- Messages for Search teaching AID API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100234 as codeID, 100 as codeGroupID, 'Content filetype does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search Teaching AID' as comment;

-- Messages for content author list API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100235 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content author list' as comment union
Select 100236 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content author list' as comment union
Select 100237 as codeID, 100 as codeGroupID, 'App language cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content author list' as comment union
Select 100238 as codeID, 100 as codeGroupID, 'App language does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content author list' as comment;


-- Messages for content uploaded by users list API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100239 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content uploaded by list' as comment union
Select 100240 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Content uploaded by list' as comment;

-- Insert code group for content uploaded by self search.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 118 as codeGroupID, 'Content / News uploaded by user' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - for content / News uploaded by self search.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 118100 as codeID, 118 as codeGroupID, 'Me' as codeNameEn, 'मी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn; 


CREATE VIEW vw_con_contentDetail AS
SELECT CC.contentID,
	   CC.requirement,
       CC.fileName,
       CC.objectives,
       CC.createdOn,
       CC.modifiedOn,
       CC.contentTypeCodeID,
       CC.createdBy,
       CC.fileTypeCodeID,
       CC.languageCodeID,
       CC.modifiedBy,
       CC.subjectCodeID,
       CC.topicCodeID,
       CC.statusCodeID,
       CC.chapterID,
       CD.contentDetailID,
       CD.contentTitle,
       CD.instruction,
       CD.author,
       CD.appLanguageCodeID
	   FROM con_content CC INNER JOIN con_contentDetail CD ON CC.contentID = CD.contentID;


-- Messages for Add Chapter API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100241 as codeID, 100 as codeGroupID, 'Subject cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100242 as codeID, 100 as codeGroupID, 'Grade cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100243 as codeID, 100 as codeGroupID, 'Chapter name in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100244 as codeID, 100 as codeGroupID, 'Chapter name in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100245 as codeID, 100 as codeGroupID, 'Subject does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100246 as codeID, 100 as codeGroupID, 'Grade does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union
Select 100247 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment union 
Select 100254 as codeID, 100 as codeGroupID, 'Chapter does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add Chapter' as comment;

-- Messages for chapter List API.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100248 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment union
Select 100249 as codeID, 100 as codeGroupID, 'Subject cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment union
Select 100250 as codeID, 100 as codeGroupID, 'Grade cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment union
Select 100251 as codeID, 100 as codeGroupID, 'Subject does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment union
Select 100252 as codeID, 100 as codeGroupID, 'Grade does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment union
Select 100253 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment;

-- Messages for chapter integration in upload content.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100255 as codeID, 100 as codeGroupID, 'Chapter cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100256 as codeID, 100 as codeGroupID, 'Chapter does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Chapter list' as comment union
Select 100257 as codeID, 100 as codeGroupID, 'Select single grade' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100258 as codeID, 100 as codeGroupID, 'Instruction in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100259 as codeID, 100 as codeGroupID, 'Instruction in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100260 as codeID, 100 as codeGroupID, 'Author in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100261 as codeID, 100 as codeGroupID, 'Author in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment;

-- Messages for chapter integration in upload content. Conflict resolved
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100262 as codeID, 100 as codeGroupID, 'Either upload file or give filename' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100263 as codeID, 100 as codeGroupID, 'Upload valid file' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'upload content' as comment;

-- Add user type 1] Officer 2] Teacher 3] Other.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 109101 as codeID, 109 as codeGroupID, 'Officer' as codeNameEn, 'अधिकारी' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user type' as comment union
Select 109102 as codeID, 109 as codeGroupID, 'Other' as codeNameEn, 'इतर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user Type' as comment;

-- Add new content review status "Archive".
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 114103 as codeID, 114 as codeGroupID, 'Archive' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new content review status' as comment;

-- Messages for News Detail API
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100264 as codeID, 100 as codeGroupID, 'NewsID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Upload content' as comment union
Select 100265 as codeID, 100 as codeGroupID, 'News does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'upload content' as comment union
Select 100266 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'upload content' as comment;

-- Messages for removeNewsImage & removeNewsPDF
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100267 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news images' as comment union
Select 100268 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news images' as comment union
Select 100269 as codeID, 100 as codeGroupID, 'News does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news images' as comment;

Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100270 as codeID, 100 as codeGroupID, 'UserID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news pdf' as comment union
Select 100271 as codeID, 100 as codeGroupID, 'User does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news pdf' as comment union
Select 100272 as codeID, 100 as codeGroupID, 'News does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Remove news pdf' as comment;

-- Messages for update user profile
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100273 as codeID, 100 as codeGroupID, 'Update user profile validation failed.' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Update user profile' as comment;

Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100274 as codeID, 100 as codeGroupID, 'Register user profile validation failed.' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Register user profile' as comment;

-- Messages for set New EmailID
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100275 as codeID, 100 as codeGroupID, 'User with this email already exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Set emailID' as comment;

-- Add new user types
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 109104 as codeID, 109 as codeGroupID, 'HM' as codeNameEn, ' प्राचार्य' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user type' as comment union
Select 109105 as codeID, 109 as codeGroupID, 'Parent' as codeNameEn, 'पालक' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Add new user type' as comment;



-- Insert code group for FCM Notification type.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 123 as codeGroupID, 'FCM Notification type' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - FCM Notification type
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 123100 as codeID, 123 as codeGroupID, 'Teaching aids' as codeNameEn, ' शैक्षणिक साधने' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 123101 as codeID, 123 as codeGroupID, 'Self learning' as codeNameEn, ' स्वअध्ययन' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 123102 as codeID, 123 as codeGroupID, 'News' as codeNameEn, 'बातम्या' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 123103 as codeID, 123 as codeGroupID, 'Trainings' as codeNameEn, ' प्रशिक्षणे' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union
Select 123104 as codeID, 123 as codeGroupID, 'Other' as codeNameEn, 'इतर' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn; 


-- Add messages for get content details for specific all language.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100294 as codeID, 100 as codeGroupID, 'App language cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , ' get content details for specific all language' as comment union
Select 100295 as codeID, 100 as codeGroupID, 'App language not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , ' get content details for specific all language' as comment;

-- Add messages for get content details for specific all language.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100296 as codeID, 100 as codeGroupID, 'Send notification user not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send push notifications' as comment union
Select 100297 as codeID, 100 as codeGroupID, 'Notification type not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Send push notifications' as comment;


-- Insert code group for Calendar.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 120 as codeGroupID, 'Training Status' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - Training Status
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 120100 as codeID, 120 as codeGroupID, 'Published' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn union 
Select 120101 as codeID, 120 as codeGroupID, 'Cancelled ' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;





-- Add messages for save training status.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100276 as codeID, 100 as codeGroupID, 'EventDetailID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100277 as codeID, 100 as codeGroupID, 'Status cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100278 as codeID, 100 as codeGroupID, 'Event does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100279 as codeID, 100 as codeGroupID, 'Status does not exist' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment union
Select 100280 as codeID, 100 as codeGroupID, 'User not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Save training Status' as comment;


-- Insert code group for Calendar.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 121 as codeGroupID, 'State' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - Training Status
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 121100 as codeID, 121 as codeGroupID, 'Maharashtra' as codeNameEn, 'महाराष्ट्र' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;


-- Add messages for save training status.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100281 as codeID, 100 as codeGroupID, 'No training records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search trainings' as comment union
Select 100282 as codeID, 100 as codeGroupID, 'No alternate training records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search alternate trainings' as comment;

-- Add messages for save training status.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100283 as codeID, 100 as codeGroupID, 'EventID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search trainings' as comment union
Select 100284 as codeID, 100 as codeGroupID, 'Event not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Search alternate trainings' as comment;


-- Insert code group for Training Category.
Insert into com_codeGroup (codeGroupID, codeGroupName, createdBy, createdOn, modifiedBy, modifiedOn)
Select 122 as codeGroupID, 'Training Category' as codeGroupName, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn;

-- Insert into code - Training Category type
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn)
Select 122100 as codeID, 122 as codeGroupID, 'State' as codeNameEn, 'महाराष्ट्र' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn; 

-- Add messages for block list.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100285 as codeID, 100 as codeGroupID, 'DistrictCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'block list' as comment union
Select 100286 as codeID, 100 as codeGroupID, 'DistrictCodeID not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'block list' as comment union
Select 100287 as codeID, 100 as codeGroupID, 'No records found' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'block list' as comment;


-- Add messages for training.
Insert into com_code (codeID, codeGroupID, codeNameEn, codeNameMr, createdBy, createdOn, modifiedBy, modifiedOn, comment)
Select 100298 as codeID, 100 as codeGroupID, 'event does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100299 as codeID, 100 as codeGroupID, 'event date cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100300 as codeID, 100 as codeGroupID, 'stateCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100301 as codeID, 100 as codeGroupID, 'districtCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100302 as codeID, 100 as codeGroupID, 'blockCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100303 as codeID, 100 as codeGroupID, 'trainer name in Marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100304 as codeID, 100 as codeGroupID, 'trainer name in English cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100305 as codeID, 100 as codeGroupID, 'statusCodeID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100306 as codeID, 100 as codeGroupID, 'user does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100307 as codeID, 100 as codeGroupID, 'event title in marathi cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100308 as codeID, 100 as codeGroupID, 'eventDetailID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100309 as codeID, 100 as codeGroupID, 'eventDetailID does not exists' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment union
Select 100310 as codeID, 100 as codeGroupID, 'userID cannot be empty' as codeNameEn, '' as codeNameMr, 1 as createdBy, now() as createdOn,  1 as modifiedBy, now() as modifiedOn , 'Training calendar' as comment;





Insert into com_configuration(configurationID, `key`, `value`)
values (2, 'dbScriptExecutedUntil', '04-DBMessagesForContentUpload.sql');



