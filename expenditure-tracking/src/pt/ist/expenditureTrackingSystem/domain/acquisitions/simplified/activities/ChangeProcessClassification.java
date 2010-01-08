package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ChangeProcessClassification extends
	WorkflowActivity<SimplifiedProcedureProcess, ChangeProcessClassificationActivityInformation> {

    @Override
    public boolean isActive(SimplifiedProcedureProcess process, User user) {
	Person loggedPerson = user.getExpenditurePerson();
	return loggedPerson == process.getRequestor() && process.getAcquisitionProcessState().isInGenesis()
		&& process instanceof SimplifiedProcedureProcess;
    }

    @Override
    protected void process(ChangeProcessClassificationActivityInformation activityInformation) {
	((SimplifiedProcedureProcess) activityInformation.getProcess()).setProcessClassification(activityInformation
		.getClassification());
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
	return new ChangeProcessClassificationActivityInformation(process, this);
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
