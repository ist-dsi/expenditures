package module.workingCapital.domain.activity;

import org.joda.time.DateTime;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class RequestCapitalRestitutionActivity extends WorkflowActivity<WorkingCapitalProcess, ActivityInformation<WorkingCapitalProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	return workingCapitalSystem.isAccountingMember(user) && workingCapital.canRequestCapitalRefund();
    }

    @Override
    protected void process(final ActivityInformation<WorkingCapitalProcess> activityInformation) {
	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
	final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	workingCapitalInitialization.setRefundRequested(new DateTime());
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new ActivityInformation<WorkingCapitalProcess>(process, this);
    }

}
