select @CLASS_ID:=DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME='module.workflow.domain.ActivityLog';

update WORKFLOW_LOG WL, WORKFLOW_PROCESS WP 
SET WL.OJB_CONCRETE_CLASS='module.workflow.domain.ActivityLog',
WL.OID=(@CLASS_ID << 32) + WL.ID_INTERNAL
WHERE WL.OID_PROCESS=WP.OID AND 
WL.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.processes.GenericLog' 
AND WP.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess' 
AND OPERATION NOT LIKE '%.Create';

select @CLASS_ID:=DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME='module.workflow.domain.LabelLog';

update WORKFLOW_LOG WL, WORKFLOW_PROCESS WP 
SET WL.OJB_CONCRETE_CLASS='module.workflow.domain.LabelLog',
WL.OID=(@CLASS_ID << 32) + WL.ID_INTERNAL,
WL.BUNDLE="resources/AcquisitionResources",
WL.LABEL='label.pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess.Create',
OPERATION=NULL
WHERE WL.OID_PROCESS=WP.OID AND 
WL.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.processes.GenericLog' 
AND WP.OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess'
AND OPERATION='pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess.Create';




