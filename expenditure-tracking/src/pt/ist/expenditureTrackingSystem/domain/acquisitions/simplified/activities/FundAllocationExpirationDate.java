package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class FundAllocationExpirationDate extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson());
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.getAcquisitionProcessState().isPendingFundAllocation();
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	LocalDate now = new LocalDate();
	process.setFundAllocationExpirationDate(now.plusDays(90));
	new AcquisitionProcessState(process, AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER);

    }

}
