
-- Inserted at 2008-09-25T18:05:20.509+01:00


create table PERSON_ACCOUNTING_UNIT (KEY_ACCOUNTING_UNIT int(11) not null, KEY_PERSON int(11) not null,  primary key (KEY_ACCOUNTING_UNIT, KEY_PERSON), key(KEY_ACCOUNTING_UNIT), key(KEY_PERSON)) type=InnoDB;
create table ACCOUNTING_UNIT (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_EXPENDITURE_TRACKING_SYSTEM` int(11),
  `NAME` text,
  primary key (ID_INTERNAL),
  index (KEY_EXPENDITURE_TRACKING_SYSTEM)
) type=InnoDB ;


-- Inserted at 2008-09-26T15:20:50.486+01:00

alter table FINANCER add column EFFECTIVE_PROJECT_FUND_ALLOCATION_ID text;
alter table FINANCER add column PROJECT_FUND_ALLOCATION_ID text;
alter table UNIT add column KEY_ACCOUNTING_UNIT int(11);
alter table UNIT add index (KEY_ACCOUNTING_UNIT);


-- Inserted at 2008-09-26T16:16:11.744+01:00

alter table FINANCER add column OJB_CONCRETE_CLASS text;






-- Inserted at 2008-09-30T14:25:45.112+01:00




create table PROCESS_COMMENT (
  `COMMENT` text,
  `DATE` timestamp NULL default NULL,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_COMMENTER` int(11),
  `KEY_PROCESS` int(11),
  primary key (ID_INTERNAL),
  index (KEY_COMMENTER),
  index (KEY_PROCESS)
) type=InnoDB ;




-- Inserted at 2008-09-30T15:00:29.874+01:00

alter table PROCESS_COMMENT add column KEY_EXPENDITURE_TRACKING_SYSTEM int(11);
alter table PROCESS_COMMENT add index (KEY_EXPENDITURE_TRACKING_SYSTEM);


