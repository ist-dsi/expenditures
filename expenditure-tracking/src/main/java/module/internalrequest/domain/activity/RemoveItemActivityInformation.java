package module.internalrequest.domain.activity;

import java.io.Serializable;

import module.internalrequest.domain.InternalRequestItem;
import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class RemoveItemActivityInformation extends ActivityInformation<InternalRequestProcess> implements Serializable {

    private InternalRequestItem item;

    public RemoveItemActivityInformation(final InternalRequestProcess internalRequestProcess,
            final WorkflowActivity<InternalRequestProcess, ? extends ActivityInformation<InternalRequestProcess>> activity) {
        super(internalRequestProcess, activity);
    }

    public InternalRequestItem getItem() {
        return item;
    }

    public void setItem(InternalRequestItem item) {
        this.item = item;
    }

    @Override
    public boolean hasAllneededInfo() {
        return this.item != null && item.getInternalRequest().getInternalRequestProcess().equals(this.getProcess());
    }
}
