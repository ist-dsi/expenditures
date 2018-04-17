package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class SelectSupplierForItemInformation extends ActivityInformation<RefundProcess> {

    private RefundItem item;
    private Supplier supplier;

    public SelectSupplierForItemInformation(final RefundProcess refundProcess,
            final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(refundProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return getItem() != null && isForwardedFromInput();
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public RefundItem getItem() {
        return item;
    }

    public void setItem(RefundItem item) {
        this.item = item;
        if (supplier == null && item.getSupplier() != null) {
            this.supplier = item.getSupplier();
        }
    }

}
