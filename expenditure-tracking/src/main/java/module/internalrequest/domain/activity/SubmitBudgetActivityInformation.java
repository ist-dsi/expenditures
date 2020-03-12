package module.internalrequest.domain.activity;

import java.io.Serializable;

import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class SubmitBudgetActivityInformation extends ActivityInformation<InternalRequestProcess> implements Serializable {


    public SubmitBudgetActivityInformation(final InternalRequestProcess internalRequestProcess,
            final WorkflowActivity<InternalRequestProcess, ? extends ActivityInformation<InternalRequestProcess>> activity) {
        super(internalRequestProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return true;
    }

}
