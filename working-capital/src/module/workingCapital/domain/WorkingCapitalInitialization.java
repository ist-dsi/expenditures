package module.workingCapital.domain;

import java.util.Comparator;

import org.joda.time.DateTime;

import module.organization.domain.Person;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class WorkingCapitalInitialization extends WorkingCapitalInitialization_Base {
    
    public static final Comparator<WorkingCapitalInitialization> COMPARATOR_BY_REQUEST_CREATION = new Comparator<WorkingCapitalInitialization>() {

	@Override
	public int compare(final WorkingCapitalInitialization o1, final WorkingCapitalInitialization o2) {
	    final int c = o1.getRequestCreation().compareTo(o2.getRequestCreation());
	    return c == 0 ? o2.hashCode() - o1.hashCode() : c;
	}

    };

    public WorkingCapitalInitialization() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
        setRequestor(UserView.getCurrentUser().getPerson());
        setRequestCreation(new DateTime());
    }

    public WorkingCapitalInitialization(final Integer year, final Unit unit, final Person person,
	    final Money requestedAnualValue, final String fiscalId, final String bankAccountId) {
	this();
	final WorkingCapital workingCapital = new WorkingCapital(year, unit, person);
	setWorkingCapital(workingCapital);
	setRequestedAnualValue(requestedAnualValue);
	setFiscalId(fiscalId);
	setBankAccountId(bankAccountId);
    }
    
}
