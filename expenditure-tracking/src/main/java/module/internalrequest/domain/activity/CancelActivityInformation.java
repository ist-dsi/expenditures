package module.internalrequest.domain.activity;

import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import java.io.Serializable;

public class CancelActivityInformation extends ActivityInformation<InternalRequestProcess> implements Serializable {


    public CancelActivityInformation(final InternalRequestProcess internalRequestProcess,
            final WorkflowActivity<InternalRequestProcess, ? extends ActivityInformation<InternalRequestProcess>> activity) {
        super(internalRequestProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return true;
    }

}
