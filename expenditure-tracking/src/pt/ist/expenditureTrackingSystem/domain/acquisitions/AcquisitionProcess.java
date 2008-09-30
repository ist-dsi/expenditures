package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericLog;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.ProcessComment;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public abstract class AcquisitionProcess extends AcquisitionProcess_Base {

    public AcquisitionProcess() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	AcquisitionProcessYear acquisitionProcessYear = AcquisitionProcessYear.getAcquisitionProcessYearByYear(new LocalDate()
		.getYear());
	setAcquisitionProcessYear(acquisitionProcessYear);
	setAcquisitionProcessNumber(acquisitionProcessYear.nextAcquisitionProcessYearNumber());
    }

    public void delete() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.delete();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public List<OperationLog> getOperationLogsInState(AcquisitionProcessStateType state) {
	List<OperationLog> logs = new ArrayList<OperationLog>();
	for (OperationLog log : getOperationLogs()) {
	    if (log.getState() == state) {
		logs.add(log);
	    }
	}
	return logs;
    }

    public List<OperationLog> getOperationLogs() {
	List<OperationLog> logs = new ArrayList<OperationLog>();
	for (GenericLog log : super.getExecutionLogs()) {
	    logs.add((OperationLog) log);
	}
	return logs;
    }

    public boolean isAvailableForCurrentUser() {
	User user = UserView.getUser();
	return user != null && isAvailableForPerson(user.getPerson());
    }

    public List<Unit> getPayingUnits() {
	List<Unit> res = new ArrayList<Unit>();
	for (Financer financer : getAcquisitionRequest().getFinancers()) {
	    res.add(financer.getUnit());
	}
	return res;
    }

    @Override
    public abstract <T extends GenericProcess> AbstractActivity<T> getActivityByName(String activityName);

    public boolean isAvailableForPerson(Person person) {
	return person.hasRoleType(RoleType.ACQUISITION_CENTRAL)
		|| getRequestor() == person || getRequestingUnit().isResponsible(person)
		|| isResponsibleForAtLeastOnePayingUnit(person) || isAccountingEmployee(person);
    }

    public boolean isAccountingEmployee(final Person person) {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.isAccountingEmployee(person);
    }

    public boolean isAccountingEmployee() {
	final User user = UserView.getUser();
	return user != null && isAccountingEmployee(user.getPerson());
    }

    public boolean isRealValueEqualOrLessThanFundAllocation() {
	Money allocatedMoney = this.getAcquisitionRequest().getTotalItemValueWithAdditionalCostsAndVat();
	Money realMoney = this.getAcquisitionRequest().getRealTotalValueWithAdditionalCostsAndVat();
	return realMoney.isLessThanOrEqual(allocatedMoney);
    }

    public boolean isResponsibleForAtLeastOnePayingUnit(Person person) {
	for (Unit unit : getPayingUnits()) {
	    if (unit.isResponsible(person)) {
		return true;
	    }
	}
	return false;
    }

    public Person getRequestor() {
	return getAcquisitionRequest().getRequester();
    }

    public boolean isActive() {
	return getLastAcquisitionProcessStateType().isActive();
    }

    public boolean isProcessInState(AcquisitionProcessStateType state) {
	return getLastAcquisitionProcessStateType().equals(state);
    }

    public AcquisitionProcessState getAcquisitionProcessState() {
	return getLastAcquisitionProcessState();
    }

    protected AcquisitionProcessState getLastAcquisitionProcessState() {
	return (AcquisitionProcessState) Collections.max(getProcessStates(), ProcessState.COMPARATOR_BY_WHEN);
    }

    public AcquisitionProcessStateType getAcquisitionProcessStateType() {
	return getLastAcquisitionProcessStateType();
    }

    protected AcquisitionProcessStateType getLastAcquisitionProcessStateType() {
	return getLastAcquisitionProcessState().getAcquisitionProcessStateType();
    }

    public boolean isPendingApproval() {
	return isProcessInState(AcquisitionProcessStateType.SUBMITTED_FOR_APPROVAL);
    }

    public boolean isApproved() {
	return isProcessInState(AcquisitionProcessStateType.APPROVED);
    }

    public boolean isAllocatedToSupplier() {
	return getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED_TO_SERVICE_PROVIDER) >= 0;
    }

    public boolean isAllocatedToUnit() {
	return getAcquisitionProcessStateType().compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED) >= 0;
    }

    public boolean isPayed() {
	return isProcessInState(AcquisitionProcessStateType.ACQUISITION_PAYED);
    }

    public boolean isAllocatedToUnit(Unit unit) {
	return isAllocatedToUnit() && getPayingUnits().contains(unit);
    }

    public boolean isAcquisitionProcessed() {
	return isProcessInState(AcquisitionProcessStateType.ACQUISITION_PROCESSED);
    }

    public boolean isInvoiceReceived() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return isProcessInState(AcquisitionProcessStateType.INVOICE_RECEIVED) && acquisitionRequest.isInvoiceReceived();
    }

    public Unit getUnit() {
	return getRequestingUnit();
    }

    public Money getAmountAllocatedToUnit(Unit unit) {
	return getAcquisitionRequest().getAmountAllocatedToUnit(unit);
    }

    public Set<Financer> getFinancersWithFundsAllocated() {
	return getAcquisitionRequest().getFinancersWithFundsAllocated();
    }

    public Set<Financer> getProjectFinancersWithFundsAllocated() {
	return getAcquisitionRequest().getProjectFinancersWithFundsAllocated();
    }

    public Money getAcquisitionRequestValueLimit() {
	return null;
    }

    public Unit getRequestingUnit() {
	return getAcquisitionRequest().getRequestingUnit();
    }

    public boolean isResponsibleForUnit(Person person) {
	Set<Authorization> validAuthorizations = person.getValidAuthorizations();
	for (Unit unit : getPayingUnits()) {
	    for (Authorization authorization : validAuthorizations) {
		if (unit.isSubUnit(authorization.getUnit())) {
		    return true;
		}
	    }
	}

	return false;
    }

    public boolean isResponsibleForUnit() {
	User user = UserView.getUser();
	if (user == null) {
	    return false;
	}
	return isResponsibleForUnit(user.getPerson());
    }

    public boolean isAllowedToViewCostCenterExpenditures() {
	try {
	    return getUnit() != null && isResponsibleForUnit() || userHasRole(RoleType.ACCOUNTING_MANAGER) || isAccountingEmployee();
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new Error(e);
	}
    }

    protected boolean userHasRole(final RoleType roleType) {
	final User user = UserView.getUser();
	return user != null && user.getPerson().hasRoleType(roleType);
    }

    public boolean isAllowedToViewSupplierExpenditures() {
	return userHasRole(RoleType.ACQUISITION_CENTRAL);
    }

    public boolean checkRealValues() {
	return getAcquisitionRequest().checkRealValues();
    }

    public boolean isResponsibleForUnit(Person person, Money amount) {
	Set<Authorization> validAuthorizations = person.getValidAuthorizations();
	for (Unit unit : getPayingUnits()) {
	    for (Authorization authorization : validAuthorizations) {
		if (authorization.getMaxAmount().isGreaterThanOrEqual(amount) && unit.isSubUnit(authorization.getUnit())) {
		    return true;
		}
	    }
	}
	return false;
    }

    public String getAcquisitionProcessId() {
	return getAcquisitionProcessYear().getYear() + "/" + getAcquisitionProcessNumber();
    }

    public boolean isProjectAccountingEmployee(final Person person) {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.isProjectAccountingEmployee(person);
    }

    public boolean isProjectAccountingEmployee() {
	final User user = UserView.getUser();
	return user != null && isProjectAccountingEmployee(user.getPerson());
    }

    public boolean hasAllocatedFundsForAllProjectFinancers() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.hasAllocatedFundsForAllProjectFinancers();
    }

    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    @Service
    public void createComment(Person person, String comment) {
	new ProcessComment(this,person,comment);
    }
}
