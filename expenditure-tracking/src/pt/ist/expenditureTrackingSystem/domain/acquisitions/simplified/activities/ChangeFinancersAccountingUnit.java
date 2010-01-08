package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AbstractChangeFinancersAccountUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class ChangeFinancersAccountingUnit extends AbstractChangeFinancersAccountUnit<RegularAcquisitionProcess> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
	Person person = user.getExpenditurePerson();
	return (process.isAccountingEmployeeForOnePossibleUnit(person) || process
		.isProjectAccountingEmployeeForOnePossibleUnit(person))
		&& isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isInAllocatedToSupplierState()
		&& process.getAcquisitionRequest().hasAnyAccountingUnitFinancerWithNoFundsAllocated(person);
    }

}
