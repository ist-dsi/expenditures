select @CLASS_ID := DOMAIN_CLASS_ID FROM FF$DOMAIN_CLASS_INFO WHERE DOMAIN_CLASS_NAME='module.workflow.domain.ActivityLog';

update WORKFLOW_LOG SET OJB_CONCRETE_CLASS='module.workflow.domain.ActivityLog', OID=(@CLASS_ID << 32) + ID_INTERNAL WHERE OJB_CONCRETE_CLASS='pt.ist.expenditureTrackingSystem.domain.acquisitions.OperationLog';
