package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UnApprove<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	Person person = user.getExpenditurePerson();
	return process.isResponsibleForUnit(person) && isUserProcessOwner(process, user)
		&& (process.isPendingApproval() || process.isInApprovedState()) && process.getRequest().hasBeenApprovedBy(person);
    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
	final Person loggedPerson = Person.getLoggedPerson();
	final PaymentProcess process = activityInformation.getProcess();
	process.getRequest().unapprove(loggedPerson);
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

    @Override
    public boolean isUserAwarenessNeeded(P process, User user) {
	return false;
    }

}
