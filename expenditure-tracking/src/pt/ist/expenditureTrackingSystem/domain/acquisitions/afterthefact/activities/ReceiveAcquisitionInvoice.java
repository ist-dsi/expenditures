package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.activities;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;

public class ReceiveAcquisitionInvoice extends AbstractActivity<AfterTheFactAcquisitionProcess> {

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
    protected void process(AfterTheFactAcquisitionProcess process, Object... objects) {
	final AcquisitionAfterTheFact acquisitionAfterTheFact = process.getAcquisitionAfterTheFact();

	final String filename = (String) objects[0];
	final byte[] bytes = (byte[]) objects[1];
	final String invoiceNumber = (String) objects[2];
	final LocalDate invoiceDate = (LocalDate) objects[3];

	acquisitionAfterTheFact.receiveInvoice(filename, bytes, invoiceNumber, invoiceDate);
    }

}
