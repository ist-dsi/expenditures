package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisition;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;
import myorg.util.InputStreamUtil;

public class EditWorkingCapitalActivity extends WorkflowActivity<WorkingCapitalProcess, EditWorkingCapitalActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return workingCapital.getMovementResponsible().getUser() == user && !workingCapital.isCanceledOrRejected();
    }

    @Override
    protected void process(final EditWorkingCapitalActivityInformation activityInformation) {
	final WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction = activityInformation
		.getWorkingCapitalAcquisitionTransaction();
	if (workingCapitalAcquisitionTransaction.isPendingApproval()) {
	    final WorkingCapitalAcquisition workingCapitalAcquisition = workingCapitalAcquisitionTransaction
		    .getWorkingCapitalAcquisition();

	    if (activityInformation.getInputStream() != null) {
		workingCapitalAcquisition.edit(activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
			activityInformation.getDescription(), activityInformation.getAcquisitionClassification(),
			activityInformation.getValueWithoutVat(), activityInformation.getMoney(), InputStreamUtil
				.consumeInputStream(activityInformation.getInputStream()), activityInformation.getDisplayName(),
			activityInformation.getFilename());
	    } else {
		workingCapitalAcquisition.edit(activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
			activityInformation.getDescription(), activityInformation.getAcquisitionClassification(),
			activityInformation.getValueWithoutVat(), activityInformation.getMoney());
	    }

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

}
