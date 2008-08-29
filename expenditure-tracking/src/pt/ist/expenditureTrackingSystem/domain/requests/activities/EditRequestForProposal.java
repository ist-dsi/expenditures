package pt.ist.expenditureTrackingSystem.domain.requests.activities;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRequestForProposalProcessBean;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcessStateType;

public class EditRequestForProposal extends GenericRequestForProposalProcessActivity {

    @Override
    protected boolean isAccessible(RequestForProposalProcess process) {
	User user = getUser();
	return user != null && (process.isRequester(user.getPerson()) || process.isResponsibleForUnit(user.getPerson()));
    }

    @Override
    protected boolean isAvailable(RequestForProposalProcess process) {
	return process.isProcessInState(RequestForProposalProcessStateType.APPROVED) && process.hasNotExpired();
    }

    @Override
    protected void process(RequestForProposalProcess process, Object... objects) {
	process.getRequestForProposal().edit((CreateRequestForProposalProcessBean) objects[0], (byte[]) objects[1]);
    }

}
