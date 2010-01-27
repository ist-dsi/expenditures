alter table SAVED_SEARCH ADD COLUMN SEARCH_PROCESS_VALUES text;

update SAVED_SEARCH set SEARCH_PROCESS_VALUES='ACQUISITIONS' WHERE SEARCH_CLASS_NAME='pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess';
update SAVED_SEARCH set SEARCH_PROCESS_VALUES='REFUND' WHERE SEARCH_CLASS_NAME='pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess';
