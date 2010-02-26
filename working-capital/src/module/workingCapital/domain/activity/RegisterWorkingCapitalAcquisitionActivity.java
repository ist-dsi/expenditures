package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisition;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import myorg.domain.User;
import myorg.util.BundleUtil;
import myorg.util.InputStreamUtil;

public class RegisterWorkingCapitalAcquisitionActivity extends
	WorkflowActivity<WorkingCapitalProcess, RegisterWorkingCapitalAcquisitionActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity."
		+ getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return workingCapital.getMovementResponsible().getUser() == user && !workingCapital.isCanceledOrRejected()
		&& workingCapital.getBalance().isPositive();
    }

    @Override
    protected void process(final RegisterWorkingCapitalAcquisitionActivityInformation activityInformation) {
	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	new WorkingCapitalAcquisition(workingCapital, activityInformation.getDocumentNumber(), activityInformation.getSupplier(),
		activityInformation.getDescription(), activityInformation.getAcquisitionClassification(), activityInformation
			.getValueWithoutVat(), activityInformation.getMoney(), InputStreamUtil
			.consumeInputStream(activityInformation.getInputStream()), activityInformation.getDisplayName(),
		activityInformation.getFilename());
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
	return new RegisterWorkingCapitalAcquisitionActivityInformation(process, this);
    }
    //
    // @Override
    // public boolean isDefaultInputInterfaceUsed() {
    // return false;
    // }

}
