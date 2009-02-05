package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class DeleteAfterTheFactAcquisitionProcess extends AbstractActivity<AfterTheFactAcquisitionProcess> {

    @Override
    protected boolean isAccessible(final AfterTheFactAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && (loggedPerson.hasRoleType(RoleType.ACQUISITION_CENTRAL)
		|| loggedPerson.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER));
    }

    @Override
    protected boolean isAvailable(final AfterTheFactAcquisitionProcess process) {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = process.getAcquisitionAfterTheFact();
	return !acquisitionAfterTheFact.getDeletedState().booleanValue();
    }

    @Override
    protected void process(final AfterTheFactAcquisitionProcess process, final Object... objects) {
	process.delete();
    }

}
