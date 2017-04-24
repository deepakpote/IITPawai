
/* Delete Content */
truncate table usr_userContent;  
truncate table con_contentDetail;      
truncate table con_contentGrade;       
truncate table con_contentResponse;
delete from con_content;

/* We need to have only Subjects English, Maths and Marathi */	
truncate table usr_userSubject;
delete from com_code where codeID > 103102 and codeID < 103107;

/* We need to have only Grades 1,2,3,4 */
truncate table usr_userGrade;
delete from com_code where codeID > 104103 and codeID < 104110;

/* Update common code versions  */
update com_configuration set value = 31 where configurationID = 1;

-- update into com_configuration
update com_configuration set `value` = '55-mitraDBUpdateTable' where `key` = 'dbScriptExecutedUntil';