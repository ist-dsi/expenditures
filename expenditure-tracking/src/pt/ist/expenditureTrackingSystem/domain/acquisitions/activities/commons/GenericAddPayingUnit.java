package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

public class GenericAddPayingUnit<P extends PaymentProcess> extends
		WorkflowActivity<P, GenericAddPayingUnitActivityInformation<P>> {

	@Override
	public boolean isActive(P process, User user) {
		return isUserProcessOwner(process, user) && process.isInGenesis()
				&& process.getRequestor() == user.getExpenditurePerson();
	}

	@Override
	protected void process(
			GenericAddPayingUnitActivityInformation<P> activityInformation) {
		activityInformation.getProcess().getRequest().addPayingUnit(
				activityInformation.getPayingUnit());
	}

	public GenericAddPayingUnitActivityInformation<P> getActivityInformation(
			P process) {
		return new GenericAddPayingUnitActivityInformation<P>(process, this);
	}

	@Override
	public String getLocalizedName() {
		return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label."
				+ getClass().getName());
	}

	@Override
	public String getUsedBundle() {
		return "resources/AcquisitionResources";
	}

}
