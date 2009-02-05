package pt.ist.expenditureTrackingSystem.domain.requests.activities;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcessState;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcessStateType;

public class ApproveRequestForProposal extends GenericRequestForProposalProcessActivity {

    @Override
    protected boolean isAccessible(RequestForProposalProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isResponsibleForUnit(loggedPerson);
    }

    @Override
    protected boolean isAvailable(RequestForProposalProcess process) {
	return process.isProcessInState(RequestForProposalProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    @Override
    protected void process(RequestForProposalProcess process, Object... objects) {
	new RequestForProposalProcessState(process, RequestForProposalProcessStateType.APPROVED);
	// TODO tratar a questão da visualização com base na data de publicação / data limite
    }

}
