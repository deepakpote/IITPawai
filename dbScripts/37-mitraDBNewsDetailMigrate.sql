-- Insert content details into the con_contentDetails table.
Insert into com_newsDetail(newsID , newsTitle, author , content , appLanguageCodeID , tags )
select newsID , newsTitle , author , content, '113101' ,  '' from com_news;

-- update into com_configuration
update com_configuration set `value` = '37-mitraDBNewsDetailMigrate' where `key` = 'dbScriptExecutedUntil';