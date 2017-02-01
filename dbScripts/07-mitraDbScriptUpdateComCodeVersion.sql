-- update com code version
update com_configuration set value = value + 1 where `key` = 'comCodeVersion';