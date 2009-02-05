package pt.ist.expenditureTrackingSystem.domain.requests.activities;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;

public class ChooseSupplierProposal extends GenericRequestForProposalProcessActivity {

    @Override
    protected boolean isAccessible(RequestForProposalProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson);
    }

    @Override
    protected boolean isAvailable(RequestForProposalProcess process) {
	return process.canAdvanceToAcquisition();
    }

    @Override
    protected void process(RequestForProposalProcess process, Object... objects) {
    }

}
