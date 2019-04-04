package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class CancelAdvancePaymentInvoiceInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private AcquisitionInvoice invoice;

    public CancelAdvancePaymentInvoiceInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && invoice != null && invoice.getAdvancePaymentInvoice();
    }

    public void setInvoice(final AcquisitionInvoice invoice) {
        this.invoice = invoice;
    }

    public AcquisitionInvoice getInvoice() {
        return invoice;
    }

}
