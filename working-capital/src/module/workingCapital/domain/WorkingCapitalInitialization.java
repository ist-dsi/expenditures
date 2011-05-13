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
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class WorkingCapitalInitialization extends WorkingCapitalInitialization_Base {

    public static enum WorkingCapitalInitializationState {
	ACTIVE, CANCELED, REJECTED;
    }

    public static final Comparator<WorkingCapitalInitialization> COMPARATOR_BY_REQUEST_CREATION = new Comparator<WorkingCapitalInitialization>() {

	@Override
	public int compare(final WorkingCapitalInitialization o1, final WorkingCapitalInitialization o2) {
	    final int c = o1.getRequestCreation().compareTo(o2.getRequestCreation());
	    return c == 0 ? o2.hashCode() - o1.hashCode() : c;
	}

    };

    public WorkingCapitalInitialization() {
	super();
	setWorkingCapitalSystem(WorkingCapitalSystem.getInstanceForCurrentHost());
	final Person person = UserView.getCurrentUser().getPerson();
	if (person == null) {
	    throw new DomainException("message.working.capital.requestor.cannot.be.null");
	}
	setRequestor(person);
	setRequestCreation(new DateTime());
    }

    public WorkingCapitalInitialization(final Integer year, final Unit unit, final Person person,
	    final Money requestedAnualValue, final String fiscalId, final String internationalBankAccountNumber) {
	this();
	WorkingCapital workingCapital = WorkingCapital.find(year, unit);
	if (workingCapital == null) {
	    workingCapital = new WorkingCapital(year, unit, person);
	} else {
	    final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
	    if (workingCapitalInitialization != null && !workingCapitalInitialization.isCanceledOrRejected()) {
		throw new DomainException("message.working.capital.exists.for.year.and.unit");
	    }
	}
	setWorkingCapital(workingCapital);
	setRequestedAnualValue(requestedAnualValue);
	setFiscalId(fiscalId);
	setInternationalBankAccountNumber(internationalBankAccountNumber);
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

    public void verify(final User user, final Money authorizedAnualValue, final Money maxAuthorizedAnualValue,
	    final String fundAllocationId) {
	setAuthorizedAnualValue(authorizedAnualValue);
	setMaxAuthorizedAnualValue(maxAuthorizedAnualValue);

	if (!isAccountingResponsible(user)) {
	    throw new DomainException("person.cannot.verify.expense", user.getPerson().getName());
	}
	setVerificationByAccounting(new DateTime());
	setResponsibleForAccountingVerification(user.getPerson());
	allocateFunds(fundAllocationId);
    }

    public void allocateFunds(final String fundAllocationId) {
	final String value = fundAllocationId != null && !fundAllocationId.isEmpty() ? fundAllocationId : null;
	setFundAllocationId(value);
    }

    private boolean isAccountingResponsible(final User user) {
	return getWorkingCapital().isAccountingResponsible(user);
    }

    public void unverify() {
	setVerificationByAccounting(new DateTime());
	removeResponsibleForAccountingVerification();
	setAuthorizedAnualValue(null);
	setMaxAuthorizedAnualValue(null);
	setFundAllocationId(null);
    }

    public void authorize(final User user) {
	final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
	final Accountability accountability = workingCapitalSystem.getManagementAccountability(user);
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
	if (getAcceptedResponsability() != null && !hasResponsibleForUnitApproval()) {
	    //final Money valueForAuthorization = getRequestedAnualValue();
	    final Money valueForAuthorization = Money.ZERO;
	    final Authorization authorization = getWorkingCapital().findUnitResponsible(user.getPerson(), valueForAuthorization);
	    if (authorization != null) {
		return true;
	    }
	}
	return false;
    }

    public boolean isPendingDirectAproval(User user) {
	if (getAcceptedResponsability() != null && !hasResponsibleForUnitApproval()) {
	    //final Money valueForAuthorization = getRequestedAnualValue();
	    final Money valueForAuthorization = Money.ZERO;
	    final Authorization authorization = getWorkingCapital().findDirectUnitResponsible(user.getPerson(),
		    valueForAuthorization);
	    if (authorization != null) {
		return true;
	    }
	}
	return false;
    }

    public boolean isPendingAproval() {
	return !hasResponsibleForUnitApproval();
    }

    public boolean isPendingVerification() {
	return hasResponsibleForUnitApproval() && !hasResponsibleForAccountingVerification();
    }

    public boolean isPendingAuthorization() {
	return hasResponsibleForAccountingVerification() && getFundAllocationId() != null && !getFundAllocationId().isEmpty()
		&& !hasResponsibleForUnitAuthorization();
    }

    public boolean isAuthorized() {
	return hasResponsibleForUnitAuthorization();
    }

    public void cancel() {
	setState(WorkingCapitalInitializationState.CANCELED);
    }

    public void reject() {
	setState(WorkingCapitalInitializationState.REJECTED);
    }

    public boolean isCanceledOrRejected() {
	final WorkingCapitalInitializationState state = getState();
	return state == WorkingCapitalInitializationState.CANCELED || state == WorkingCapitalInitializationState.REJECTED;
    }

    @Override
    public AccountingUnit getAccountingUnit() {
	final AccountingUnit accountingUnit = super.getAccountingUnit();
	return accountingUnit == null ? (hasWorkingCapital() ? getWorkingCapital().getUnit().getAccountingUnit() : null)
		: accountingUnit;
    }

    public boolean isPendingFundAllocation() {
	return !isCanceledOrRejected() && (getFundAllocationId() == null || getFundAllocationId().isEmpty())
		&& hasResponsibleForAccountingVerification() && !hasResponsibleForUnitAuthorization();
    }

    public boolean isPendingFundUnAllocation() {
	return (isCanceledOrRejected() && (getFundAllocationId() != null) && (!getFundAllocationId().isEmpty()));
    }

    public void delete() {
	removeWorkingCapital();
	removeWorkingCapitalSystem();
	removeRequestor();
	removeAccountingUnit();
	deleteDomainObject();
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	return getWorkingCapitalSystem() == WorkingCapitalSystem.getInstanceForCurrentHost();
    }

}
