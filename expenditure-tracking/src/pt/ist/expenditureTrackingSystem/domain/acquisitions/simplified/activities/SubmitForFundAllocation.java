package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SubmitForFundAllocation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(AcquisitionProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson())
		&& !process.getAcquisitionRequest().hasBeenSubmittedForFundsAllocationBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(AcquisitionProcess process) {
	return process.isPendingApproval();
    }

    @Override
    protected void process(AcquisitionProcess process, Object... objects) {
	Person person = (Person) objects[0];
	process.getAcquisitionRequest().submittedForFundsAllocation(person);
	if (process.getAcquisitionRequest().isSubmittedForFundsAllocationByAllResponsibles()) {
	    new AcquisitionProcessState(process, AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION);
	    new FundAllocationExpirationDate().execute(process, new Object[] {});
	}
    }

}
