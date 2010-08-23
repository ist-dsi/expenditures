package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class GenericRemovePayingUnit<P extends PaymentProcess> extends
	WorkflowActivity<P, GenericRemovePayingUnitActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	return isUserProcessOwner(process, user) && process.isInGenesis()
		&& process.getRequestor() == user.getExpenditurePerson();
    }

    @Override
    protected void process(GenericRemovePayingUnitActivityInformation<P> activityInformation) {
	activityInformation.getProcess().getRequest().removePayingUnit(activityInformation.getPayingUnit());
    }

    public GenericRemovePayingUnitActivityInformation<P> getActivityInformation(P process) {
	return new GenericRemovePayingUnitActivityInformation<P>(process, this);
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
    public boolean isVisible() {
	return false;
    }

    @Override
    public boolean isConfirmationNeeded(P process) {
	return true;
    }
}
