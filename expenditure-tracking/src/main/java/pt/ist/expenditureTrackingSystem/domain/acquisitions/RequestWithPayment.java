/*
 * @(#)RequestWithPayment.java
 *
 * Copyright 2009 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Shezad Anavarali
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
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

    public Money getTotalValueWithoutVat() {
        Money money = Money.ZERO;
        for (RequestItem item : getRequestItems()) {
            money = money.add(item.getValueWithoutVat());
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
        return !getFinancersSet().isEmpty();
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
                projectFinancer.resetEffectiveFundAllocation();
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
            financer.resetEffectiveFundAllocationId();
        }
    }

    public boolean hasAllFundAllocationId() {
        for (Financer financer : getFinancersWithFundsAllocated()) {
            if (financer.getFundAllocationId() == null) {
                return false;
            }
        }
        return !getFinancersWithFundsAllocated().isEmpty();
    }

    public boolean hasAllEffectiveFundAllocationId() {
        for (Financer financer : getFinancersWithFundsAllocated()) {
            if (financer.getEffectiveFundAllocationId() == null) {
                return false;
            }
        }
        return !getFinancersWithFundsAllocated().isEmpty();
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
            if (/* financer.getAmountAllocated().isPositive() && */financer.hasFundAllocationId()) {
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
            if (financer.isProjectFinancer()) {
                final ProjectFinancer projectFinancer = (ProjectFinancer) financer;
                if (projectFinancer.isProjectAccountingEmployeeForOnePossibleUnit(person)
                        && projectFinancer.getAmountAllocated().isPositive()
                        && projectFinancer.getProjectFundAllocationId() == null) {
                    return true;
                }
            } else {
                if (financer.isAccountingEmployeeForOnePossibleUnit(person) && financer.getAmountAllocated().isPositive()
                        && financer.getFundAllocationId() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Financer> getAccountingUnitFinancerWithNoFundsAllocated(final Person person) {
        Set<Financer> res = new HashSet<Financer>();
        for (Financer financer : getFinancersSet()) {
            if (financer.isProjectFinancer()) {
                final ProjectFinancer projectFinancer = (ProjectFinancer) financer;
                if (projectFinancer.isProjectAccountingEmployeeForOnePossibleUnit(person)
                        && projectFinancer.getAmountAllocated().isPositive()
                        && projectFinancer.getProjectFundAllocationId() == null) {
                    res.add(financer);
                }
            } else {
                if (financer.isAccountingEmployeeForOnePossibleUnit(person) && financer.getAmountAllocated().isPositive()
                        && financer.getFundAllocationId() == null) {
                    res.add(financer);
                }
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
            if (requestItem.isResponsible(person) && !requestItem.hasBeenApprovedBy(person)) {
                return false;
            }
        }
        return !getRequestItemsSet().isEmpty();
    }

    public void approve(final Person person) {
        for (final RequestItem requestItem : getRequestItemsSet()) {
            requestItem.approve(person);
        }
        if (isSubmittedForFundsAllocationByAllResponsibles()) {
            getProcess().submitForFundAllocation();
        }
    }

    public boolean isSubmittedForFundsAllocationByAllResponsibles() {
        if (getRequestItems().size() == 0) {
            return false;
        }
        for (final RequestItem requestItem : getRequestItemsSet()) {
            if (!requestItem.isApproved()) {
                return false;
            }
        }
        return true;
    }

    public void unapprove(final Person person) {
        for (final RequestItem requestItem : getRequestItemsSet()) {
            requestItem.unapprove(person);
        }
    }

    public void unSubmitForFundsAllocation() {
        for (final RequestItem requestItem : getRequestItemsSet()) {
            requestItem.unapprove();
        }
    }

    public boolean hasBeenAuthorizedBy(final Person person) {
        for (final RequestItem requestItem : getRequestItemsSet()) {
            if (requestItem.isResponsible(person) && !requestItem.hasBeenAuthorizedBy(person)) {
                return false;
            }
        }
        return !getRequestItemsSet().isEmpty();
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
            if (!requestItem.hasBeenAuthorizedBy(person)) {
                requestItem.authorizeBy(person);
            }
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
        for (final Financer financer : getFinancersWithFundsInitiallyAllocated()) {
            if (!financer.hasAllocatedFundsPermanentlyForAllProjectFinancers()) {
                return false;
            }
        }
        return !getFinancersSet().isEmpty();
    }

    public boolean hasAllocatedFundsPermanentlyForAnyProjectFinancer() {
        for (final Financer financer : getFinancersWithFundsInitiallyAllocated()) {
            if (financer.hasAllocatedFundsPermanentlyForAnyProjectFinancers()) {
                return true;
            }
        }
        return getFinancersSet().isEmpty();
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

    public Set<Financer> getFinancersWithFundsInitiallyAllocated() {
        Set<Financer> res = new HashSet<Financer>();
        for (Financer financer : getFinancers()) {
            if (financer.getInitialAmountAllocated().isPositive()) {
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

    public boolean hasAnyProjectFinancers() {
        for (Financer financer : getFinancers()) {
            if (financer.isProjectFinancer()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAllInvoicesAllocated() {
        for (Financer financer : getFinancers()) {
            if (!financer.hasAllInvoicesAllocated()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAllInvoicesAllocatedInProject() {
        for (Financer financer : getFinancers()) {
            if (!financer.hasAllInvoicesAllocatedInProject()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasAnyInvoiceAllocated() {
        for (Financer financer : getFinancers()) {
            if (financer.hasAllInvoicesAllocated()) {
                return true;
            }
        }
        return false;

    }

    public boolean isConfirmedForAllInvoices() {
        Set<PaymentProcessInvoice> allInvoices = getInvoices();
        Set<PaymentProcessInvoice> confirmedInvoices = new HashSet<PaymentProcessInvoice>();
        for (RequestItem item : getRequestItems()) {
            confirmedInvoices.addAll(item.getConfirmedInvoices());
        }

        return !confirmedInvoices.isEmpty() && confirmedInvoices.containsAll(allInvoices);
    }

    public Set<PaymentProcessInvoice> getConfirmedInvoices() {
        Set<PaymentProcessInvoice> confirmedInvoices = new HashSet<PaymentProcessInvoice>();
        for (RequestItem item : getRequestItems()) {
            confirmedInvoices.addAll(item.getConfirmedInvoices());
        }
        return confirmedInvoices;
    }

    public Set<PaymentProcessInvoice> getInvoices() {
        Set<PaymentProcessInvoice> allInvoices = new HashSet<PaymentProcessInvoice>();
        for (RequestItem item : getRequestItems()) {
            allInvoices.addAll(item.getInvoicesFiles());
        }
        return allInvoices;
    }

    public boolean isPartiallyApproved() {
        for (RequestItem item : getRequestItems()) {
            if (item.isPartiallyApproved()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPartiallyAuthorized() {
        for (RequestItem item : getRequestItems()) {
            if (item.isPartiallyAuthorized()) {
                return true;
            }
        }
        return false;
    }

    public boolean isWithInvoicesPartiallyConfirmed() {
        for (RequestItem item : getRequestItems()) {
            if (item.isWithInvoicesPartiallyConfirmed()) {
                return true;
            }
        }
        return false;
    }

    public boolean isProjectAccountingEmployeeForOnePossibleUnit(Person person) {
        for (final Financer financer : getFinancersSet()) {
            if (financer.isProjectFinancer()) {
                if (financer.isProjectAccountingEmployeeForOnePossibleUnit(person)) {
                    return true;
                }
            }
        }
        return false;
    }

    public abstract SortedSet<? extends RequestItem> getOrderedRequestItemsSet();

    public boolean areAllFundsPermanentlyAllocated() {
        for (final Financer financer : getFinancersSet()) {
//	    if (financer.getRealShareValue().isPositive()) {
            if (!financer.areAllFundsPermanentlyAllocated()) {
                return false;
            }
//	    }
        }
        return true;
    }

    public boolean hasProposalDocument() {
        return true;
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer> getFinancers() {
        return getFinancersSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem> getRequestItems() {
        return getRequestItemsSet();
    }

    @Deprecated
    public boolean hasAnyFinancers() {
        return !getFinancersSet().isEmpty();
    }

    @Deprecated
    public boolean hasRequester() {
        return getRequester() != null;
    }

    @Deprecated
    public boolean hasRequestingUnit() {
        return getRequestingUnit() != null;
    }

}
