package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class Approve<P extends PaymentProcess> extends WorkflowActivity<P, ActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	final Person executor = user.getExpenditurePerson();

	return executor != null && process.isPendingApproval() && isUserProcessOwner(process, user)
		&& process.isResponsibleForUnit(executor) && !process.getRequest().hasBeenApprovedBy(executor);

    }

    @Override
    protected void process(ActivityInformation<P> activityInformation) {
	PaymentProcess process = activityInformation.getProcess();
	final Person person = Person.getLoggedPerson();
	process.getRequest().approve(person);
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
    public boolean isUserAwarenessNeeded(final P process, final User user) {
	final Person person = user.getExpenditurePerson();
	for (final RequestItem requestItem : process.getRequest().getRequestItemsSet()) {
	    for (final UnitItem unitItem : requestItem.getUnitItemsSet()) {
		final Unit unit = unitItem.getUnit();
		if (!unitItem.isApproved() && unit.isDirectResponsible(person)) {
		    return true;
		}
	    }
	}
	return false;
    }

}
