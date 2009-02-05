package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class FundAllocationExpirationDate extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson);
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return  super.isAvailable(process) && process.getAcquisitionProcessState().isPendingFundAllocation();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	LocalDate now = new LocalDate();
	process.setFundAllocationExpirationDate(now.plusDays(90));
	process.allocateFundsToSupplier();
    }

}
