package pt.ist.expenditureTrackingSystem.domain.requests;

import java.util.Comparator;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class RequestForProposalProcessState extends RequestForProposalProcessState_Base {

    public static final Comparator<RequestForProposalProcessState> COMPARATOR_BY_WHEN = new Comparator<RequestForProposalProcessState>() {
	public int compare(RequestForProposalProcessState o1, RequestForProposalProcessState o2) {
	    return o1.getWhenDateTime().compareTo(o2.getWhenDateTime());
	}
    };

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
	setRequestForProposalProcess(requestForProposalProcess);
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
