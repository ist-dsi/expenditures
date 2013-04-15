package module.mission.domain;

import jvstm.cps.ConsistencyPredicate;
import module.organization.domain.AccountabilityType;
import module.workflow.domain.WorkflowQueue;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixframework.Atomic;

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

    @Atomic
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
    @Deprecated
    public boolean hasWorkflowQueue() {
        return getWorkflowQueue() != null;
    }

    @Deprecated
    public boolean hasAccountabilityType() {
        return getAccountabilityType() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

}
