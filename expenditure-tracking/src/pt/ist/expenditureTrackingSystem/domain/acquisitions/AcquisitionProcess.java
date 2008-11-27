package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.File;
import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericFile;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.ProcessComment;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
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
	super.setSkipSupplierFundAllocation(Boolean.FALSE);
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
	return person.hasRoleType(RoleType.ACQUISITION_CENTRAL) || person.hasRoleType(RoleType.ACQUISITION_CENTRAL_MANAGER)
		|| person.hasRoleType(RoleType.ACCOUNTING_MANAGER) || person.hasRoleType(RoleType.PROJECT_ACCOUNTING_MANAGER)
		|| person.hasRoleType(RoleType.TREASURY) || getRequestor() == person || getRequestingUnit().isResponsible(person)
		|| isResponsibleForAtLeastOnePayingUnit(person) || isAccountingEmployee(person)
		|| isProjectAccountingEmployee(person);
    }

    public boolean isAccountingEmployee(final Person person) {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.isAccountingEmployee(person);
    }

    public boolean isAccountingEmployee() {
	final User user = UserView.getUser();
	return user != null && isAccountingEmployee(user.getPerson());
    }

    public boolean isAccountingEmployeeForOnePossibleUnit() {
	final User user = UserView.getUser();
	return user != null && isAccountingEmployeeForOnePossibleUnit(user.getPerson());
    }

    public boolean isAccountingEmployeeForOnePossibleUnit(final Person person) {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.isAccountingEmployeeForOnePossibleUnit(person);
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

    public boolean isPendingApproval() {
	return getLastAcquisitionProcessState().isPendingApproval();
    }

    public boolean isApproved() {
	return getLastAcquisitionProcessState().isApproved();
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

    public Set<Financer> getFinancersWithFundsAllocated() {
	return getAcquisitionRequest().getFinancersWithFundsAllocated();
    }

    public Set<Financer> getFinancersWithFundsAllocated(Person person) {
	return getAcquisitionRequest().getFinancersWithFundsAllocated(person);
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated() {
	return getAcquisitionRequest().getProjectFinancersWithFundsAllocated();
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated(final Person person) {
	return getAcquisitionRequest().getProjectFinancersWithFundsAllocated(person);
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

    public boolean hasAnyAllocatedFunds() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.hasAnyAllocatedFunds();
    }

    public boolean hasAllocatedFundsForAllProjectFinancers(final Person person) {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.hasAllocatedFundsForAllProjectFinancers(person);
    }

    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.hasAllocatedFundsPermanentlyForAllProjectFinancers();
    }

    
    @Service
    public void createComment(Person person, String comment) {
	new ProcessComment(this, person, comment);
    }

    public boolean isProcessFlowCharAvailable() {
	return false;
    }

    public List<AcquisitionProcessStateType> getAvailableStates() {
	return Collections.emptyList();
    }

    @Override
    public void setCurrentOwner(Person currentOwner) {
	throw new DomainException("error.message.illegal.method.useTakeInstead");
    }

    @Override
    public void removeCurrentOwner() {
	throw new DomainException("error.message.illegal.method.useReleaseInstead");
    }

    @Service
    public void systemProcessRelease() {
	super.setCurrentOwner(null);
    }

    @Service
    public void takeProcess() {
	final Person currentOwner = getCurrentOwner();
	if (currentOwner != null) {
	    throw new DomainException("error.message.illegal.method.useStealInstead");
	}
	final User user = UserView.getUser();
	super.setCurrentOwner(user.getPerson());
    }

    @Service
    public void releaseProcess() {
	final User user = UserView.getUser();
	final Person person = getCurrentOwner();
	if (user != null && person != null && user.getPerson() != person) {
	    throw new DomainException("error.message.illegal.state.unableToReleaseATicketNotOwnerByUser");
	}
	super.setCurrentOwner(null);
    }

    @Service
    public void stealProcess() {
	final User user = UserView.getUser();
	super.setCurrentOwner(user.getPerson());
    }

    public boolean isUserCurrentOwner() {
	final User user = UserView.getUser();
	return user != null && user.getPerson() == getCurrentOwner();
    }

    @Service
    public void addFile(String displayName, String filename, byte[] consumeInputStream) {
	GenericFile file = new GenericFile(displayName, filename, consumeInputStream);
	addFiles(file);
    }
}
