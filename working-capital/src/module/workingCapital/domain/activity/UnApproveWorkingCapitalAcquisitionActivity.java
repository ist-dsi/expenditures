package module.workingCapital.domain.activity;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalTransaction;
import myorg.domain.User;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;

public class UnApproveWorkingCapitalAcquisitionActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalTransactionInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final Person person = user.getPerson();
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	if (!workingCapital.isCanceledOrRejected() && workingCapital.hasAcquisitionPendingSubmission()) {
	    final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	    if (workingCapitalInitialization != null
		    && workingCapitalInitialization.hasResponsibleForUnitApproval()
		    && !workingCapitalInitialization.hasResponsibleForAccountingVerification()) {
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
    protected void process(final WorkingCapitalTransactionInformation activityInformation) {
	final WorkingCapitalTransaction workingCapitalTransaction = activityInformation.getWorkingCapitalTransaction();
	workingCapitalTransaction.unApprove();
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new WorkingCapitalTransactionInformation(process, this);
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    public boolean isUserAwarenessNeeded(final WorkingCapitalProcess process, final User user) {
        return false;
    }

}
