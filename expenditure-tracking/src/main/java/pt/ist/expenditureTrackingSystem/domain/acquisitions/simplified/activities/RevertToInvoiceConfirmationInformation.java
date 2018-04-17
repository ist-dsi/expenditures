package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class RevertToInvoiceConfirmationInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private static final long serialVersionUID = 1L;

    private AcquisitionInvoice invoice;

    public RevertToInvoiceConfirmationInformation(final SimplifiedProcedureProcess process, WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public AcquisitionInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(AcquisitionInvoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public boolean hasAllneededInfo() {
        return true;
    }

}
