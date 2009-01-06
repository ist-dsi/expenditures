alter table ACQUISITION_REQUEST_ITEM RENAME TO REQUEST_ITEM;
ALTER TABLE REQUEST_ITEM ADD COLUMN OJB_CONCRETE_CLASS VARCHAR(255);
UPDATE REQUEST_ITEM SET OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem';



-- Inserted at 2008-12-16T13:30:36.328Z

alter table ACQUISITION add column KEY_PROCESS int(11);
alter table ACQUISITION add column KEY_REQUESTOR int(11);
alter table ACQUISITION add index (KEY_PROCESS);
alter table ACQUISITION add index (KEY_REQUESTOR);
alter table FILE add column INVOICE_VALUE text;
alter table FILE add column INVOICE_VAT text;
alter table FILE add column KEY_REFUND_ITEM int(11);
alter table FILE add index (KEY_REFUND_ITEM);
alter table GENERIC_PROCESS add column KEY_REQUEST int(11);
alter table GENERIC_PROCESS add index (KEY_REQUEST);
alter table REQUEST_ITEM add column KEY_REQUEST int(11);
alter table REQUEST_ITEM add column VALUE_ESTIMATION text;
alter table REQUEST_ITEM add column VALUE_SPENT text;
alter table REQUEST_ITEM add index (KEY_REQUEST);

