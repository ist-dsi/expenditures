package module.workingCapital.domain;

import module.organization.domain.Person;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class WorkingCapitalInitialization extends WorkingCapitalInitialization_Base {
    
    public WorkingCapitalInitialization() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
        setRequestor(UserView.getCurrentUser().getPerson());
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
