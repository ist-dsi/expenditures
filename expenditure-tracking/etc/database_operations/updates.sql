



-- Inserted at 2009-01-21T18:32:29.600Z

alter table PERSON add column KEY_REFUNDEE int(11);
alter table PERSON add index (KEY_REFUNDEE);


create table REFUNDEE (
  `FISCAL_CODE` text,
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `KEY_EXPENDITURE_TRACKING_SYSTEM` int(11),
  `KEY_PERSON` int(11),
  `NAME` text,
  primary key (ID_INTERNAL),
  index (KEY_EXPENDITURE_TRACKING_SYSTEM),
  index (KEY_PERSON)
) type=InnoDB ;

