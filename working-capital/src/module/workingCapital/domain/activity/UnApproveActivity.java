package module.workingCapital.domain.activity;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;

public class UnApproveActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final Person person = user.getPerson();
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	for (final WorkingCapitalInitialization workingCapitalInitialization : workingCapital.getWorkingCapitalInitializationsSet()) {
	    if (workingCapitalInitialization.hasResponsibleForUnitApproval() && !workingCapitalInitialization.hasResponsibleForAccountingVerification()) {
		//final Money valueForAuthorization = workingCapitalInitialization.getRequestedAnualValue();
		final Money valueForAuthorization = Money.ZERO;
		final Authorization authorization = workingCapital.findUnitResponsible(person, valueForAuthorization);
		if (authorization != null) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    protected void process(final WorkingCapitalInitializationInformation activityInformation) {
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	workingCapitalInitialization.unapprove();
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new WorkingCapitalInitializationInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
