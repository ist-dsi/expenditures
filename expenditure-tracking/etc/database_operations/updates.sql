
-- Inserted at 2008-09-25T18:05:20.509+01:00


create table PERSON_ACCOUNTING_UNIT (KEY_ACCOUNTING_UNIT int(11) not null, KEY_PERSON int(11) not null,  primary key (KEY_ACCOUNTING_UNIT, KEY_PERSON), key(KEY_ACCOUNTING_UNIT), key(KEY_PERSON)) type=InnoDB;
create table ACCOUNTING_UNIT (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_EXPENDITURE_TRACKING_SYSTEM` int(11),
  `NAME` text,
  primary key (ID_INTERNAL),
  index (KEY_EXPENDITURE_TRACKING_SYSTEM)
) type=InnoDB ;

