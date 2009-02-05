package pt.ist.expenditureTrackingSystem.domain.requests.activities;

import pt.ist.expenditureTrackingSystem.domain.dto.CreateRequestForProposalProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcessStateType;

public class EditRequestForProposal extends GenericRequestForProposalProcessActivity {

    @Override
    protected boolean isAccessible(RequestForProposalProcess process) {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && process.isRequester(loggedPerson);
    }

    @Override
    protected boolean isAvailable(RequestForProposalProcess process) {
	return process.isProcessInState(RequestForProposalProcessStateType.IN_GENESIS);
    }

    @Override
    protected void process(RequestForProposalProcess process, Object... objects) {
	process.getRequestForProposal().edit((CreateRequestForProposalProcessBean) objects[0], (byte[]) objects[1]);
    }

}
