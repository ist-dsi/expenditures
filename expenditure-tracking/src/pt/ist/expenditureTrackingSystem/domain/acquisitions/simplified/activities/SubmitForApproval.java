package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;

public class SubmitForApproval extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	return user.getExpenditurePerson() == process.getRequestor()
		&& isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isInGenesis()
		&& process.getAcquisitionRequest().isFilled()
		&& process.getAcquisitionRequest().isEveryItemFullyAttributedToPayingUnits();
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	final RegularAcquisitionProcess process = activityInformation.getProcess();
	if (process.isSimplifiedProcedureProcess()
		&& ((SimplifiedProcedureProcess) process).getProcessClassification() != ProcessClassification.CT75000
		&& !process.hasAcquisitionProposalDocument()
		&& ((SimplifiedProcedureProcess) process).hasInvoiceFile()
		&& process.getTotalValue().isGreaterThan(ExpenditureTrackingSystem.getInstance().getMaxValueStartedWithInvoive())
		) {
	    final String message = BundleUtil.getStringFromResourceBundle(getUsedBundle(),
		    "activities.message.exception.exceeded.limit.to.start.process.with.invoice");
	    throw new DomainException(message);
	}

	process.submitForApproval();
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }
}
