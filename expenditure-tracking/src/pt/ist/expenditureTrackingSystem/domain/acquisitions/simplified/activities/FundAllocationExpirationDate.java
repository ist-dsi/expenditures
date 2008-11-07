package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class FundAllocationExpirationDate extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson());
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
