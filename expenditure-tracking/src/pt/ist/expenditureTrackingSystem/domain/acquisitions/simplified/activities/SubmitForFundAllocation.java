package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class SubmitForFundAllocation extends
	WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.isPendingApproval()
		&& process.isResponsibleForUnit(person)
		&& !process.getAcquisitionRequest().hasBeenApprovedBy(person);
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
	activityInformation.getProcess().getAcquisitionRequest().approve(UserView.getCurrentUser().getExpenditurePerson());
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
    public boolean isUserAwarenessNeeded(final RegularAcquisitionProcess process, final User user) {
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
