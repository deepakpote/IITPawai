-- Insert content details into the con_contentDetails table.
Insert into con_contentDetail(contentID , appLanguageCodeID , contentTitle , instruction , author )
select contentID , '113100' , contentTitle , instruction , author from con_content;

-- update into com_configuration
update com_configuration set `value` = '25-mitra-db-insert-contentDetail' where `key` = 'dbScriptExecutedUntil';