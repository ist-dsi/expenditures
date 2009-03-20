package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixframework.pstm.Transaction;

public abstract class AcquisitionProcess extends AcquisitionProcess_Base {

    public AcquisitionProcess() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	super.setSkipSupplierFundAllocation(Boolean.FALSE);
    }

    public void delete() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	acquisitionRequest.delete();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public boolean isAvailableForCurrentUser() {
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && isAvailableForPerson(loggedPerson);
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

    public boolean isPastInvoiceReceived() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return getLastAcquisitionProcessState().isPastInvoiceReceived() && acquisitionRequest.isInvoiceReceived();
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
	final Person loggedPerson = getLoggedPerson();
	return loggedPerson != null && loggedPerson.hasRoleType(roleType);
    }

    public boolean isAllowedToViewSupplierExpenditures() {
	return userHasRole(RoleType.ACQUISITION_CENTRAL) || userHasRole(RoleType.ACQUISITION_CENTRAL_MANAGER)
		|| userHasRole(RoleType.MANAGER);
    }

    public boolean checkRealValues() {
	return getAcquisitionRequest().checkRealValues();
    }

    public Integer getYear() {
	return getPaymentProcessYear().getYear();
    }

    public String getAcquisitionProcessId() {
	return getYear() + "/" + getAcquisitionProcessNumber();
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

    @Override
    public boolean isInAuthorizedState() {
	return getAcquisitionProcessState().isAuthorized();
    }

    @Override
    public boolean isInvoiceConfirmed() {
	return getAcquisitionProcessState().isInvoiceConfirmed();
    }

    @Override
    public boolean isAllocatedPermanently() {
	return getAcquisitionProcessState().isAllocatedPermanently();
    }

    @Override
    public Collection<Supplier> getSuppliers() {
	return getRequest().getSuppliers();

    }

}
