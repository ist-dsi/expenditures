package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class UnsetSkipSupplierFundAllocation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && (loggedPerson == process.getRequestor() || userHasRole(RoleType.ACQUISITION_CENTRAL));
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process)
		&& (process.getAcquisitionProcessState().isInGenesis() && getLoggedPerson() == process.getRequestor()
			|| (userHasRole(RoleType.ACQUISITION_CENTRAL) &&
				(process.getAcquisitionProcessState().isAuthorized() || process.getAcquisitionProcessState().isAcquisitionProcessed())))
		&& process.getSkipSupplierFundAllocation();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	process.unSkipSupplierFundAllocation();
    }

}
