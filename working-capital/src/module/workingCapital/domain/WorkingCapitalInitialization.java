package module.workingCapital.domain;

import java.util.Comparator;

import module.organization.domain.Accountability;
import module.organization.domain.Person;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Money;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
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

    public void approve(final Person person) {
	final WorkingCapital workingCapital = getWorkingCapital();
	//final Money requestedAnualValue = getRequestedAnualValue();
	final Money valueForAuthorization = Money.ZERO;
	final Authorization authorization = workingCapital.findUnitResponsible(person, valueForAuthorization);
	if (authorization == null) {
	    throw new DomainException("person.cannot.approve.expense", person.getName());
	}
	setAprovalByUnitResponsible(new DateTime());
	setResponsibleForUnitApproval(authorization);
    }

    public void unapprove() {
	setAprovalByUnitResponsible(null);
	removeResponsibleForUnitApproval();
    }

    public void verify(final User user) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	final Accountability accountability = workingCapitalSystem.getAccountingAccountability(user);
	if (accountability == null) {
	    throw new DomainException("person.cannot.verify.expense", user.getPerson().getName());
	}
	setVerificationByAccounting(new DateTime());
	setResponsibleForAccountingVerification(accountability);
    }

    public void unverify() {
	setVerificationByAccounting(new DateTime());
	removeResponsibleForAccountingVerification();
	setAuthorizedAnualValue(null);
	setMaxAuthorizedAnualValue(null);
    }

    public void authorize(final User user) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstance();
	final Accountability accountability = workingCapitalSystem.getManagementeAccountability(user);
	if (accountability == null) {
	    throw new DomainException("person.cannot.authorize.expense", user.getPerson().getName());
	}
	setAuthorizationByUnitResponsible(new DateTime());
	setResponsibleForUnitAuthorization(accountability);
    }

    public void unauthorize() {
	setAuthorizationByUnitResponsible(new DateTime());
	removeResponsibleForUnitAuthorization();
    }

    public boolean isPendingAproval(User user) {
	if (!hasResponsibleForUnitApproval()) {
	    //final Money valueForAuthorization = getRequestedAnualValue();
	    final Money valueForAuthorization = Money.ZERO;
	    final Authorization authorization = getWorkingCapital().findUnitResponsible(user.getPerson(), valueForAuthorization);
	    if (authorization != null) {
		return true;
	    }
	}
	return false;
    }

    public boolean isPendingVerification() {
	return hasResponsibleForUnitApproval() && !hasResponsibleForAccountingVerification();
    }

    public boolean isPendingAuthorization() {
	return hasResponsibleForAccountingVerification() && !hasResponsibleForUnitAuthorization();
    }

}
