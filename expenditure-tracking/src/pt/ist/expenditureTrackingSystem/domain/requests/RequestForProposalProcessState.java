package pt.ist.expenditureTrackingSystem.domain.requests;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class RequestForProposalProcessState extends RequestForProposalProcessState_Base {

    protected RequestForProposalProcessState() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public RequestForProposalProcessState(final RequestForProposalProcess requestForProposalProcess,
	    final RequestForProposalProcessStateType requestForProposalProcessStateType) {
	this();
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	checkArguments(requestForProposalProcess, requestForProposalProcessStateType, person);
	setProcess(requestForProposalProcess);
	setRequestForProposalProcessStateType(requestForProposalProcessStateType);
	setWho(person);
	setWhenDateTime(new DateTime());
    }

    private void checkArguments(RequestForProposalProcess requestForProposalProcess,
	    RequestForProposalProcessStateType requestForProposalProcessStateType, Person person) {
	if (requestForProposalProcess == null || requestForProposalProcessStateType == null || person == null) {
	    throw new DomainException("error.wrong.RequestForProposalProcessState.arguments");
	}
    }

    public String getLocalizedName() {
	return getRequestForProposalProcessStateType().getLocalizedName();
    }
}
