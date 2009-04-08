package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import myorg.domain.util.Money;

public abstract class RequestWithPayment extends RequestWithPayment_Base {

    public abstract <T extends PaymentProcess> T getProcess();

    public RequestWithPayment() {
	super();
    }

    public boolean hasAnyRequestItems() {
	return getRequestItems().size() > 0;
    }

    public Money getTotalValue() {
	Money money = Money.ZERO;
	for (RequestItem item : getRequestItems()) {
	    money = money.add(item.getValue());
	}
	return money;
    }

    public Money getRealTotalValue() {
	Money money = Money.ZERO;
	for (RequestItem item : getRequestItems()) {
	    if (item.getRealValue() != null) {
		money = money.add(item.getRealValue());
	    }
	}
	return money;
    }

    public boolean isProjectAccountingEmployee(final Person person) {
	for (final Financer financer : getFinancersSet()) {
	    if (financer.isProjectAccountingEmployee(person)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAllocatedFundsForAllProjectFinancers() {
	for (final Financer financer : getFinancersSet()) {
	    if (!financer.hasAllocatedFundsForAllProject()) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasAllocatedFundsForAllProjectFinancers(Person person) {
	for (final Financer financer : getFinancersSet()) {
	    if (financer.isProjectAccountingEmployee(person) && !financer.hasAllocatedFundsForAllProject()) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasAnyAllocatedFunds() {
	for (Financer financer : getFinancers()) {
	    if (financer.hasAnyFundsAllocated()) {
		return true;
	    }
	}
	return false;
    }

    public boolean isAccountingEmployee(final Person person) {
	for (final Financer financer : getFinancersSet()) {
	    if (financer.isAccountingEmployee(person)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isAccountingEmployeeForOnePossibleUnit(final Person person) {
	for (final Financer financer : getFinancersSet()) {
	    if (!financer.isProjectFinancer()) {
		if (financer.isAccountingEmployeeForOnePossibleUnit(person)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void resetFundAllocationId() {
	for (Financer financer : getFinancersSet()) {
	    financer.setFundAllocationId(null);
	}

    }

    public void resetFundAllocationId(final Person person) {
	for (Financer financer : getFinancersSet()) {
	    if (financer.isAccountingEmployee(person)) {
		financer.setFundAllocationId(null);
	    }
	}
    }

    public void resetProjectFundAllocationId(final Person person) {
	for (Financer financer : getFinancersSet()) {
	    if (financer.isProjectFinancer() && financer.isProjectAccountingEmployee(person)) {
		ProjectFinancer projectFinancer = (ProjectFinancer) financer;
		projectFinancer.setProjectFundAllocationId(null);
	    }
	}
    }

    public void resetPermanentProjectFundAllocationId(final Person person) {
	for (Financer financer : getFinancersSet()) {
	    if (financer.isProjectFinancer() && financer.isProjectAccountingEmployee(person)) {
		ProjectFinancer projectFinancer = (ProjectFinancer) financer;
		projectFinancer.setEffectiveProjectFundAllocationId(null);
	    }
	}
    }

    public void resetProjectFundAllocationId() {
	for (Financer financer : getFinancersSet()) {
	    if (financer.isProjectFinancer()) {
		ProjectFinancer projectFinancer = (ProjectFinancer) financer;
		projectFinancer.setProjectFundAllocationId(null);
	    }
	}
    }

    public void resetEffectiveFundAllocationId() {
	for (Financer financer : getFinancersSet()) {
	    financer.setEffectiveFundAllocationId(null);
	}

    }

    public boolean hasAllFundAllocationId() {
	for (Financer financer : getFinancersWithFundsAllocated()) {
	    if (financer.getFundAllocationId() == null) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasAllEffectiveFundAllocationId() {
	for (Financer financer : getFinancersWithFundsAllocated()) {
	    if (financer.getEffectiveFundAllocationId() == null) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasAllFundAllocationId(Person person) {
	for (Financer financer : getFinancersWithFundsAllocated()) {
	    if (financer.isAccountingEmployee(person) && financer.getFundAllocationId() == null) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasAnyFundAllocationId() {
	for (Financer financer : getFinancers()) {
	    if (financer.getAmountAllocated().isPositive() && financer.hasFundAllocationId()) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyEffectiveFundAllocationId() {
	for (Financer financer : getFinancersWithFundsAllocated()) {
	    if (financer.hasEffectiveFundAllocationId()) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyNonProjectFundAllocationId() {
	for (Financer financer : getFinancersWithFundsAllocated()) {
	    if (financer.getFundAllocationId() != null) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyFundAllocationId(Person person) {
	for (Financer financer : getFinancersWithFundsAllocated()) {
	    if (financer.getFundAllocationId() != null && financer.isAccountingEmployee(person)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasAnyAccountingUnitFinancerWithNoFundsAllocated(final Person person) {
	for (Financer financer : getFinancersSet()) {
	    if (financer.isAccountingEmployeeForOnePossibleUnit(person) && financer.getAmountAllocated().isPositive()
		    && financer.getFundAllocationId() == null) {
		return true;
	    }
	}
	return false;
    }

    public Set<Financer> getAccountingUnitFinancerWithNoFundsAllocated(final Person person) {
	Set<Financer> res = new HashSet<Financer>();
	for (Financer financer : getFinancersSet()) {
	    if (financer.isAccountingEmployeeForOnePossibleUnit(person) && financer.getAmountAllocated().isPositive()
		    && financer.getFundAllocationId() == null) {
		res.add(financer);
	    }
	}
	return res;
    }

    public Set<AccountingUnit> getAccountingUnits() {
	Set<AccountingUnit> units = new HashSet<AccountingUnit>();
	for (Financer financer : getFinancers()) {
	    units.add(financer.getAccountingUnit());
	}
	return units;
    }

    public boolean hasBeenApprovedBy(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    if (requestItem.hasBeenApprovedBy(person)) {
		return true;
	    }
	}
	return false;
    }

    public void submittedForFundsAllocation(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    requestItem.submittedForFundsAllocation(person);
	}
    }

    public boolean isSubmittedForFundsAllocationByAllResponsibles() {
	if (getRequestItemsCount() == 0) {
	    return false;
	}
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    if (!requestItem.isApproved()) {
		return false;
	    }
	}
	return true;
    }

    public void unSubmitForFundsAllocation(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    requestItem.unSubmitForFundsAllocation(person);
	}
    }

    public void unSubmitForFundsAllocation() {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    requestItem.unSubmitForFundsAllocation();
	}
    }

    public boolean hasBeenAuthorizedBy(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    if (requestItem.hasBeenAuthorizedBy(person)) {
		return true;
	    }
	}
	return false;
    }

    public boolean isAuthorizedByAllResponsibles() {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    if (!requestItem.isAuthorized()) {
		return false;
	    }
	}
	return true;
    }

    public void authorizeBy(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    requestItem.authorizeBy(person);
	}
    }

    public void unathorizeBy(final Person person) {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    requestItem.unathorizeBy(person);
	}
    }

    public boolean isRealValueFullyAttributedToUnits() {
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    if (!requestItem.isRealValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    public boolean isEveryItemFullyAttributeInRealValues() {
	for (final RequestItem item : getRequestItemsSet()) {
	    if (!item.isRealValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
	for (final Financer financer : getFinancersSet()) {
	    if (!financer.hasAllocatedFundsPermanentlyForAllProjectFinancers()) {
		return false;
	    }
	}
	return true;
    }

    public boolean isTreasuryMember(final Person person) {
	for (final Financer financer : getFinancersSet()) {
	    if (financer.isTreasuryMember(person)) {
		return true;
	    }
	}
	return false;
    }

    public List<PayingUnitTotalBean> getTotalAmountsForEachPayingUnit() {
	List<PayingUnitTotalBean> beans = new ArrayList<PayingUnitTotalBean>();
	for (Financer financer : getFinancers()) {
	    beans.add(new PayingUnitTotalBean(financer));
	}
	return beans;
    }

    public boolean hasPayingUnit(Unit unit) {
	Financer financer = getFinancer(unit);
	return financer != null;
    }

    public void removePayingUnit(Unit unit) {
	Financer financer = getFinancer(unit);
	if (financer != null) {
	    financer.delete();
	}
    }

    public Financer addPayingUnit(final Unit unit) {
	Financer financer = getFinancer(unit);
	return financer != null ? financer : unit.finance(this);
    }

    public Financer getFinancer(Unit unit) {
	for (Financer financer : getFinancers()) {
	    if (financer.getUnit() == unit) {
		return financer;
	    }
	}
	return null;
    }

    public Set<Unit> getPayingUnits() {
	Set<Unit> res = new HashSet<Unit>();
	for (Financer financer : getFinancers()) {
	    res.add(financer.getUnit());
	}
	return res;
    }

    public Money getAmountAllocatedToUnit(Unit unit) {
	Financer financer = getFinancer(unit);
	return financer == null ? Money.ZERO : financer.getAmountAllocated();
    }

    public Set<Financer> getFinancersWithFundsAllocated() {
	Set<Financer> res = new HashSet<Financer>();
	for (Financer financer : getFinancers()) {
	    if (financer.getAmountAllocated().isPositive()) {
		res.add(financer);
	    }
	}
	return res;
    }

    public Set<Financer> getFinancersWithFundsAllocated(Person person) {
	Set<Financer> res = new HashSet<Financer>();
	for (Financer financer : getFinancers()) {
	    if (financer.isAccountingEmployee(person) && financer.getAmountAllocated().isPositive()) {
		res.add(financer);
	    }
	}
	return res;
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated() {
	Set<ProjectFinancer> res = new HashSet<ProjectFinancer>();
	for (Financer financer : getFinancers()) {
	    if (financer instanceof ProjectFinancer && financer.getAmountAllocated().isPositive()) {
		res.add((ProjectFinancer) financer);
	    }
	}
	return res;
    }

    public Set<ProjectFinancer> getProjectFinancersWithFundsAllocated(Person person) {
	Set<ProjectFinancer> res = new HashSet<ProjectFinancer>();
	for (Financer financer : getFinancers()) {
	    if (financer instanceof ProjectFinancer && financer.isProjectAccountingEmployee(person)
		    && financer.getAmountAllocated().isPositive()) {
		res.add((ProjectFinancer) financer);
	    }
	}
	return res;
    }

}
