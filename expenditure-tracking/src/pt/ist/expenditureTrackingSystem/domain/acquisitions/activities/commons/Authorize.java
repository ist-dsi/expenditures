package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class Authorize<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user) && process.isInAllocatedToUnitState()
		&& process.isResponsibleForUnit(person, process.getRequest().getTotalValue())
		&& !process.getRequest().hasBeenAuthorizedBy(person);
    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
	activityInformation.getProcess().authorizeBy(Person.getLoggedPerson());
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
