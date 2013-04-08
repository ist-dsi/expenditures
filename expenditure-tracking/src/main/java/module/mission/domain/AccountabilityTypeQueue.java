package module.mission.domain;

import jvstm.cps.ConsistencyPredicate;
import module.organization.domain.AccountabilityType;
import module.workflow.domain.WorkflowQueue;
import pt.ist.bennu.core.domain.VirtualHost;
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

    @Override
    public boolean isConnectedToCurrentHost() {
        return getMissionSystem() == VirtualHost.getVirtualHostForThread().getMissionSystem();
    }

    @ConsistencyPredicate
    public boolean checkHasMissionSystem() {
        return hasMissionSystem();
    }

    @ConsistencyPredicate
    public boolean checkHasAccountabilityType() {
        return hasAccountabilityType();
    }

    @ConsistencyPredicate
    public boolean checkHasWorkflowQueue() {
        return hasWorkflowQueue();
    }
}
