update con_contentDetail set appLanguageCodeID = 113101 where contentID not in (select contentID from con_content where languageCodeID = 101100);

-- update into com_configuration
update com_configuration set `value` = '28-update-content-applanguage' where `key` = 'dbScriptExecutedUntil';