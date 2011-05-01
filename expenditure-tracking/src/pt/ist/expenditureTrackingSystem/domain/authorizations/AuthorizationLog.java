package pt.ist.expenditureTrackingSystem.domain.authorizations;

import java.util.Comparator;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class AuthorizationLog extends AuthorizationLog_Base {

    public static final Comparator<AuthorizationLog> COMPARATOR_BY_WHEN = new Comparator<AuthorizationLog>() {

	@Override
	public int compare(final AuthorizationLog authorizationLog1, AuthorizationLog authorizationLog2) {
	    final DateTime when1 = authorizationLog1.getWhenOperationWasRan();
	    final DateTime when2 = authorizationLog2.getWhenOperationWasRan();
	    final int c = when1.compareTo(when2);
	    return c == 0 ? authorizationLog2.hashCode() - authorizationLog1.hashCode() : c;
	}

    };

    public AuthorizationLog(final AuthorizationOperation authorizationOperation, final Authorization authorization, final String justification) {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setWhenOperationWasRan(new DateTime());
        setWho(Person.getLoggedPerson());
        setAuthorizationOperation(authorizationOperation);

        setAuthorizationType(authorization.getAuthorizationType());
        setCanDelegate(authorization.getCanDelegate());
        setStartDate(authorization.getStartDate());
        setEndDate(authorization.getEndDate());
        setMaxAmount(authorization.getMaxAmount());
        setPerson(authorization.getPerson());
        setUnit(authorization.getUnit());
        setJustification(justification);
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	return getExpenditureTrackingSystem() == ExpenditureTrackingSystem.getInstance();
    }

}
