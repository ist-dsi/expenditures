package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AbstractChangeFinancersAccountUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ChangeFinancersAccountingUnit extends AbstractChangeFinancersAccountUnit<RefundProcess> {

    @Override
    public boolean isActive(RefundProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.isPendingFundAllocation()
		&& (process.isAccountingEmployeeForOnePossibleUnit(person) || process
			.isProjectAccountingEmployeeForOnePossibleUnit(person))
		&& process.getRequest().hasAnyAccountingUnitFinancerWithNoFundsAllocated(person);
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
