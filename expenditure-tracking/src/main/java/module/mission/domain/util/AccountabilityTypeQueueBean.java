package module.mission.domain.util;

import java.io.Serializable;

import module.mission.domain.AccountabilityTypeQueue;
import module.mission.domain.MissionSystem;
import module.organization.domain.AccountabilityType;
import module.workflow.domain.WorkflowQueue;
import pt.ist.fenixframework.Atomic;

public class AccountabilityTypeQueueBean implements Serializable {

    private AccountabilityType accountabilityType;
    private WorkflowQueue workflowQueue;

    public AccountabilityType getAccountabilityType() {
        return accountabilityType;
    }

    public void setAccountabilityType(AccountabilityType accountabilityType) {
        this.accountabilityType = accountabilityType;
    }

    public WorkflowQueue getWorkflowQueue() {
        return workflowQueue;
    }

    public void setWorkflowQueue(WorkflowQueue workflowQueue) {
        this.workflowQueue = workflowQueue;
    }

    @Atomic
    public AccountabilityTypeQueue create() {
        for (final AccountabilityTypeQueue accountabilityTypeQueue : MissionSystem.getInstance().getAccountabilityTypeQueuesSet()) {
            if (accountabilityTypeQueue.getAccountabilityType() == accountabilityType) {
                accountabilityTypeQueue.setWorkflowQueue(workflowQueue);
            }
        }
        return new AccountabilityTypeQueue(accountabilityType, workflowQueue);
    }

}
