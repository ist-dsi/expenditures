package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.UnApprove;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RemoveFundAllocationExpirationDateForResponsible extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {

	return super.isAvailable(process)
		&& ((!process.getSkipSupplierFundAllocation() && process.getFundAllocationExpirationDate() != null) || (process
			.getSkipSupplierFundAllocation() && process.isPendingFundAllocation()))
		&& !process.hasAnyAllocatedFunds();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.removeFundAllocationExpirationDate();
	new UnApprove<RegularAcquisitionProcess>().execute(process, objects);
    }

}
