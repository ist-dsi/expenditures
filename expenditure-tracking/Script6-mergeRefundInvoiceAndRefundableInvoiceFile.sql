-- Add new fields needed for RefundableInvoiceFile

alter table `GENERIC_FILE` add column `REFUNDABLE_VALUE` text;
alter table `GENERIC_FILE` add column `VALUE` text;
alter table `GENERIC_FILE` add column `VAT_VALUE` text;

update GENERIC_FILE GF, REFUND_INVOICE RI set 
GF.VALUE=RI.VALUE,
GF.VAT_VALUE=RI.VAT_VALUE,
GF.REFUNDABLE_VALUE=RI.REFUNDABLE_VALUE,
GF.INVOICE_NUMBER=RI.INVOICE_NUMBER,
GF.INVOICE_DATE=RI.INVOICE_DATE,
GF.OID_SUPPLIER=RI.OID_SUPPLIER
WHERE
GF.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile'
AND GF.OID_INVOICE=RI.OID;


