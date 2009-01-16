
UPDATE GENERIC_LOG SET OPERATION="UnAuthorize" WHERE OPERATION="UnAuthorizeAcquisitionProcess";



-- Inserted at 2009-01-16T15:53:27.188Z

alter table FILE add column KEY_INVOICE int(11);
alter table FILE add index (KEY_INVOICE);


create table REFUND_INVOICE (
  `ID_INTERNAL` int(11) NOT NULL auto_increment,
  `INVOICE_DATE` text,
  `INVOICE_NUMBER` int(11),
  `KEY_EXPENDITURE_TRACKING_SYSTEM` int(11),
  `KEY_FILE` int(11),
  `KEY_REFUND_ITEM` int(11),
  `KEY_SUPPLIER` int(11),
  `REFUNDABLE_VALUE` text,
  `VALUE` text,
  `VAT_VALUE` text,
  primary key (ID_INTERNAL),
  index (KEY_EXPENDITURE_TRACKING_SYSTEM),
  index (KEY_FILE),
  index (KEY_REFUND_ITEM),
  index (KEY_SUPPLIER)
) type=InnoDB ;

