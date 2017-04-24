-- Assign Admin role to user 1] 9545300303 2] Suresh Bharathi 77588 77888 3] Atul 9998396393- Meghana 


update usr_userRole set roleID = 1 where userID in (8,10,14);

-- update into com_configuration
update com_configuration set `value` = '58-mitraDBAssignAdminRole' where `key` = 'dbScriptExecutedUntil';