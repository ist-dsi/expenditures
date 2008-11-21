package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SubmitForFundAllocation extends GenericAcquisitionProcessActivity {

    @Override
    protected boolean isAccessible(RegularAcquisitionProcess process) {
	User user = getUser();
	return user != null && process.isResponsibleForUnit(user.getPerson())
		&& !process.getAcquisitionRequest().hasBeenSubmittedForFundsAllocationBy(user.getPerson());
    }

    @Override
    protected boolean isAvailable(RegularAcquisitionProcess process) {
	return super.isAvailable(process) && process.isPendingApproval();
    }

    @Override
    protected void process(RegularAcquisitionProcess process, Object... objects) {
	Person person = (Person) objects[0];
	process.getAcquisitionRequest().submittedForFundsAllocation(person);
	if (process.getAcquisitionRequest().isSubmittedForFundsAllocationByAllResponsibles()) {
	    if (!process.getSkipSupplierFundAllocation()) {
		process.submitForFundAllocation();
		new FundAllocationExpirationDate().execute(process, new Object[] {});
	    }
	    else {
		process.skipFundAllocation();
	    }
	}
    }

}
