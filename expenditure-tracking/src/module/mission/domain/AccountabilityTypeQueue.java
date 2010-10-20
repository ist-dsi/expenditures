package module.mission.domain;

import module.organization.domain.AccountabilityType;
import module.workflow.domain.WorkflowQueue;
import pt.ist.fenixWebFramework.services.Service;

public class AccountabilityTypeQueue extends AccountabilityTypeQueue_Base {
    
    public AccountabilityTypeQueue() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    public AccountabilityTypeQueue(final AccountabilityType accountabilityType, final WorkflowQueue workflowQueue) {
        this();
        setAccountabilityType(accountabilityType);
        setWorkflowQueue(workflowQueue);
    }

    @Service
    public void delete() {
	removeWorkflowQueue();
	removeAccountabilityType();
	removeMissionSystem();
	deleteDomainObject();
    }
    
}
