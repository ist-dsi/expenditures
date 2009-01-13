package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.pstm.Transaction;

public abstract class AcquisitionProcess extends AcquisitionProcess_Base {

    public AcquisitionProcess() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	final AcquisitionProcessYear acquisitionProcessYear = getAcquisitionProcessYearForConstruction();
	setAcquisitionProcessYear(acquisitionProcessYear);
	setAcquisitionProcessNumber(acquisitionProcessYear.nextAcquisitionProcessYearNumber());
	super.setSkipSupplierFundAllocation(Boolean.FALSE);
    }

    private AcquisitionProcessYear getAcquisitionProcessYearForConstruction() {
	return AcquisitionProcessYear.getAcquisitionProcessYearByYear(getYearForConstruction());
    }

    protected int getYearForConstruction() {
	return new LocalDate().getYear();
    }

    public void delete() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.delete();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public boolean isAvailableForCurrentUser() {
	User user = UserView.getUser();
	return user != null && isAvailableForPerson(user.getPerson());
    }

    @Override
    public abstract <T extends GenericProcess> AbstractActivity<T> getActivityByName(String activityName);

    public abstract boolean hasAnyAvailableActivitity();

    public boolean isAvailableForPerson(Person person) {
	return person.hasRoleType(RoleType.ACQUISITION_CENTRAL) || person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER)
		|| person.hasRoleType(RoleType.ACCOUNTING_MANAGER) || person.hasRoleType(RoleType.PROJECT_ACCOUNTING_MANAGER)
		|| person.hasRoleType(RoleType.TREASURY) || getRequestor() == person || getRequestingUnit().isResponsible(person)
		|| isResponsibleForAtLeastOnePayingUnit(person) || isAccountingEmployee(person)
		|| isProjectAccountingEmployee(person);
    }

    public boolean isActive() {
	return getLastAcquisitionProcessState().isActive();
    }

    public AcquisitionProcessState getAcquisitionProcessState() {
	return getLastAcquisitionProcessState();
    }

    protected AcquisitionProcessState getLastAcquisitionProcessState() {
	return (AcquisitionProcessState) Collections.max(getProcessStates(), ProcessState.COMPARATOR_BY_WHEN);
    }

    public AcquisitionProcessStateType getAcquisitionProcessStateType() {
	return getLastAcquisitionProcessState().getAcquisitionProcessStateType();
    }

    @Override
    public boolean isPendingApproval() {
	return getLastAcquisitionProcessState().isPendingApproval();
    }

    public boolean isApproved() {
	final AcquisitionProcessStateType acquisitionProcessStateType = getAcquisitionProcessStateType();
	return acquisitionProcessStateType.compareTo(AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION) <= 0
		&& isActive();
    }

    public boolean isAllocatedToSupplier() {
	return getLastAcquisitionProcessState().isAllocatedToSupplier();
    }

    public boolean isAllocatedToUnit() {
	return getLastAcquisitionProcessState().isAllocatedToUnit();
    }

    public boolean isPayed() {
	return getLastAcquisitionProcessState().isPayed();
    }

    public boolean isAllocatedToUnit(Unit unit) {
	return isAllocatedToUnit() && getPayingUnits().contains(unit);
    }

    public boolean isAcquisitionProcessed() {
	return getLastAcquisitionProcessState().isAcquisitionProcessed();
    }

    public boolean isInvoiceReceived() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return getLastAcquisitionProcessState().isInvoiceReceived() && acquisitionRequest.isInvoiceReceived();
    }

    public Unit getUnit() {
	return getRequestingUnit();
    }

    public Money getAmountAllocatedToUnit(Unit unit) {
	return getAcquisitionRequest().getAmountAllocatedToUnit(unit);
    }

    public Money getAcquisitionRequestValueLimit() {
	return null;
    }

    public Unit getRequestingUnit() {
	return getAcquisitionRequest().getRequestingUnit();
    }

    public boolean isAllowedToViewCostCenterExpenditures() {
	try {
	    return (getUnit() != null && isResponsibleForUnit()) || userHasRole(RoleType.ACCOUNTING_MANAGER)
		    || userHasRole(RoleType.PROJECT_ACCOUNTING_MANAGER) || isAccountingEmployee()
		    || isProjectAccountingEmployee() || userHasRole(RoleType.MANAGER);
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
	return userHasRole(RoleType.ACQUISITION_CENTRAL) || userHasRole(RoleType.ACQUISITION_CENTRAL_MANAGER)
		|| userHasRole(RoleType.MANAGER);
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

    public Integer getYear() {
	return getAcquisitionProcessYear().getYear();
    }

    public String getAcquisitionProcessId() {
	return getYear() + "/" + getAcquisitionProcessNumber();
    }

    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    public boolean isProcessFlowCharAvailable() {
	return false;
    }

    public List<AcquisitionProcessStateType> getAvailableStates() {
	return Collections.emptyList();
    }

    public String getAllocationIds() {
	StringBuilder builder = new StringBuilder();
	for (PayingUnitTotalBean bean : getAcquisitionRequest().getTotalAmountsForEachPayingUnit()) {
	    builder.append(bean.getFinancer().getFundAllocationIds());
	}
	return builder.toString();
    }

    public String getEffectiveAllocationIds() {
	StringBuilder builder = new StringBuilder();
	for (PayingUnitTotalBean bean : getAcquisitionRequest().getTotalAmountsForEachPayingUnit()) {
	    builder.append(bean.getFinancer().getEffectiveFundAllocationIds());
	}
	return builder.toString();
    }

    public boolean isTakenByPerson(Person person) {
	return person != null && person == getCurrentOwner();
    }

    public boolean isTakenByCurrentUser() {
	User user = UserView.getUser();
	return user != null && isTakenByPerson(user.getPerson());
    }

    public boolean isSimplifiedProcedureProcess() {
	return false;
    }

    public AcquisitionRequest getRequest() {
	return getAcquisitionRequest();
    }

    @Override
    public boolean isInGenesis() {
	return getAcquisitionProcessState().isInGenesis();
    }

    @Override
    public boolean isInApprovedState() {
	return getAcquisitionProcessState().isInApprovedState();
    }

    @Override
    public boolean isPendingFundAllocation() {
	return getAcquisitionProcessState().isInAllocatedToSupplierState();
    }
}
