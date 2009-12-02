-- expenditure's GenericFile becomes ProcessFile

update GENERIC_FILE SET OJB_CONCRETE_CLASS='module.workflow.domain.ProcessFile' WHERE OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.processes.GenericFile';

select @CLASS_ID:=DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME='module.workflow.domain.ProcessFile';

update GENERIC_FILE SET OID=(@CLASS_ID<<32) + ID_INTERNAL WHERE OJB_CONCRETE_CLASS='module.workflow.domain.ProcessFile';

-- Since Invoices also became part of the ProcessFiles for
-- the AcquisitionInvoice and RefundableInvoiceFile we have
-- to add the OID_PROCESS 

CREATE TEMPORARY TABLE CONNECT(GF_OID bigint(20), P_OID bigint(20));

insert into CONNECT(GF_OID,P_OID)  
select GF.OID, A.OID_ACQUISITION_PROCESS FROM 
GENERIC_FILE GF, PAYMENT_PROCESS_INVOICE_REQUEST_ITEM PP, REQUEST_ITEM I, ACQUISITION A 
WHERE GF.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice' AND 
GF.OID = PP.OID_PAYMENT_PROCESS_INVOICE AND PP.OID_REQUEST_ITEM = I.OID AND I.OID_REQUEST=A.OID 
GROUP BY GF.OID;

update GENERIC_FILE GF, CONNECT C SET GF.OID_PROCESS=C.P_OID WHERE GF.OID=C.GF_OID;

DROP TEMPORARY TABLE CONNECT;

update GENERIC_FILE GF, REFUND_INVOICE RI, REQUEST_ITEM I, ACQUISITION A 
SET GF.OID_PROCESS=A.OID_PROCESS 
WHERE GF.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile' 
AND GF.OID_INVOICE=RI.OID AND RI.OID_REFUND_ITEM=I.OID AND I.OID_REQUEST=A.OID;

