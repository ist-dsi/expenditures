package pt.ist.expenditureTrackingSystem.domain.requests;

import myorg.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class RequestForProposalProcessState extends RequestForProposalProcessState_Base {

    protected RequestForProposalProcessState() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public RequestForProposalProcessState(final RequestForProposalProcess process,
	    final RequestForProposalProcessStateType processStateType) {
	this();
	final Person person = getPerson();
	checkArguments(process, processStateType, person);
	super.initFields(process, person);
	setRequestForProposalProcessStateType(processStateType);
    }

    private void checkArguments(RequestForProposalProcess requestForProposalProcess,
	    RequestForProposalProcessStateType requestForProposalProcessStateType, Person person) {
	if (requestForProposalProcessStateType == null) {
	    throw new DomainException("error.wrong.RequestForProposalProcessState.arguments");
	}
	super.checkArguments(requestForProposalProcess, person);
    }

    public String getLocalizedName() {
	return getRequestForProposalProcessStateType().getLocalizedName();
    }
}
