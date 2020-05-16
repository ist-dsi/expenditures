package module.internalrequest.domain.activity;

import java.io.Serializable;

import com.google.common.base.Strings;

import module.internalrequest.domain.InternalRequestItem;
import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.fenixframework.Atomic;

public class AddItemActivityInformation extends ActivityInformation<InternalRequestProcess> implements Serializable {

    private int quantity = 1;
    private String description;

    public AddItemActivityInformation(final InternalRequestProcess internalRequestProcess,
            final WorkflowActivity<InternalRequestProcess, ? extends ActivityInformation<InternalRequestProcess>> activity) {
        super(internalRequestProcess, activity);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean hasAllneededInfo() {
        return !Strings.isNullOrEmpty(this.description) && this.quantity > 0;
    }

    @Atomic
    public InternalRequestItem createNewInternalRequestItem() {
        return new InternalRequestItem(this.quantity, this.description, this.getProcess().getInternalRequest());
    }

}
