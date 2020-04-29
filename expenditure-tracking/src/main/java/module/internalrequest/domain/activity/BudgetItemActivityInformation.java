package module.internalrequest.domain.activity;

import module.finance.util.Money;
import module.internalrequest.domain.InternalRequestItem;
import module.internalrequest.domain.InternalRequestProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import java.io.Serializable;

public class BudgetItemActivityInformation extends ActivityInformation<InternalRequestProcess> implements Serializable {

    private InternalRequestItem item;
    private Money price;
    private boolean unitPrice;
    private String observations;

    public BudgetItemActivityInformation(final InternalRequestProcess internalRequestProcess,
            final WorkflowActivity<InternalRequestProcess, ? extends ActivityInformation<InternalRequestProcess>> activity) {
        super(internalRequestProcess, activity);
    }

    public InternalRequestItem getItem() {
        return item;
    }
    public void setItem(InternalRequestItem item) {
        this.item = item;
    }

    public Money getPrice() {
        return price;
    }
    public void setPrice(Money value) {
        this.price = value;
    }

    public boolean isUnitPrice() { return unitPrice; }
    public void setUnitPrice(boolean unitPrice) { this.unitPrice = unitPrice; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    @Override
    public boolean hasAllneededInfo() {
        return this.item != null && item.getInternalRequest().getInternalRequestProcess().equals(this.getProcess())
                && price != null;
    }
}
