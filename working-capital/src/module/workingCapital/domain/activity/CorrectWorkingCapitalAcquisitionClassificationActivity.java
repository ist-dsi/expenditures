package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisition;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class CorrectWorkingCapitalAcquisitionClassificationActivity extends
	WorkflowActivity<WorkingCapitalProcess, EditWorkingCapitalActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess workingCapitalProcess, final User user) {
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected() && workingCapital.hasAcquisitionPendingVerification(user);
    }

    @Override
    protected void process(final EditWorkingCapitalActivityInformation activityInformation) {
	final WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction = activityInformation
		.getWorkingCapitalAcquisitionTransaction();
	if (workingCapitalAcquisitionTransaction.isPendingVerificationByUser()) {
	    final WorkingCapitalAcquisition workingCapitalAcquisition = workingCapitalAcquisitionTransaction
		    .getWorkingCapitalAcquisition();

	    workingCapitalAcquisition.edit(activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
		    activityInformation.getDescription(), activityInformation.getAcquisitionClassification(),
		    activityInformation.getValueWithoutVat());
	}
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	return new EditWorkingCapitalActivityInformation(process, this);
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
	return false;
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return false;
    }

}
