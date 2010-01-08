
select @CLASS_ID := DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME='module.workflow.domain.LabelLog';

-- AddAcquisitionProposalDocument
update WORKFLOW_LOG SET LABEL='label.pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.AddAcquisitionProposalDocument', BUNDLE='resources/AcquisitionResources', OJB_CONCRETE_CLASS='module.workflow.domain.LabelLog', STATE = NULL, OID = (@CLASS_ID << 32) + ID_INTERNAL WHERE OPERATION='AddAcquisitionProposalDocument';

-- ChangeAcquisitionProposalDocument
update WORKFLOW_LOG SET LABEL='label.pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ChangeAcquisitionProposalDocument', BUNDLE='resources/AcquisitionResources', OJB_CONCRETE_CLASS='module.workflow.domain.LabelLog', STATE = NULL, OID = (@CLASS_ID << 32) + ID_INTERNAL WHERE OPERATION='ChangeAcquisitionProposalDocument';

-- ReceiveInvoice
update WORKFLOW_LOG SET LABEL='label.pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.ReceiveInvoice', BUNDLE='resources/AcquisitionResources', OJB_CONCRETE_CLASS='module.workflow.domain.LabelLog', STATE = NULL, OID = (@CLASS_ID << 32) + ID_INTERNAL WHERE OPERATION='ReceiveInvoice';

-- RemoveInvoice
update WORKFLOW_LOG SET LABEL='label.pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.RemoveInvoice', BUNDLE='resources/AcquisitionResources', OJB_CONCRETE_CLASS='module.workflow.domain.LabelLog', STATE = NULL, OID = (@CLASS_ID << 32) + ID_INTERNAL WHERE OPERATION='RemoveInvoice';