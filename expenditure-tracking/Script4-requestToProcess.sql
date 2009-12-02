-- Found trash in the database, deleting it.
delete FROM GENERIC_FILE WHERE OID=176093659337;

-- Migrate AcquisitionProposalDocument from the AcquisitionRequest to The AcquisitionProcess

alter table WORKFLOW_PROCESS add column OID_ACQUISITION_PROPOSAL_DOCUMENT bigint(20);

update GENERIC_FILE F, WORKFLOW_PROCESS WP SET F.OID_PROCESS=WP.OID, WP.OID_ACQUISITION_PROPOSAL_DOCUMENT = F.OID WHERE F.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProposalDocument' AND
F.OID_ACQUISITION_REQUEST=WP.OID_ACQUISITION_REQUEST;

-- Migrate PurchaseOrderDocument from the AcquisitionRequest to The AcquisitionProcess

alter table WORKFLOW_PROCESS add column OID_PURCHASE_ORDER_DOCUMENT bigint(20);

update GENERIC_FILE F, WORKFLOW_PROCESS WP SET F.OID_PROCESS=WP.OID, WP.OID_PURCHASE_ORDER_DOCUMENT = F.OID WHERE F.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.PurchaseOrderDocument' AND
F.OID_ACQUISITION_REQUEST=WP.OID_ACQUISITION_REQUEST;


-- Migrate AfterTheFactInvoice from the AfterTheFactAcquisitionRequest to AfterTheFactAcquisitionProcess

alter table WORKFLOW_PROCESS add column OID_INVOICE bigint(20);

update GENERIC_FILE F, WORKFLOW_PROCESS WP SET F.OID_PROCESS=WP.OID, WP.OID_INVOICE = F.OID WHERE F.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactInvoice' AND
F.OID_ACQUISITION=WP.OID_ACQUISITION_AFTER_THE_FACT;


