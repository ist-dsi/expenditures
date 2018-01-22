/*
 * @(#)Financer.java
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

import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import module.finance.util.Money;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;
import pt.utl.ist.fenix.tools.util.Strings;

/**
 * 
 * @author João Neves
 * @author João Antunes
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class Financer extends Financer_Base {

    protected Financer() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public Financer(final RequestWithPayment acquisitionRequest, final CostCenter costCenter) {
        this();
        if (acquisitionRequest == null || costCenter == null) {
            throw new DomainException(Bundle.EXPENDITURE, "error.financer.wrong.initial.arguments");
        }
        if (acquisitionRequest.hasPayingUnit(costCenter)) {
            throw new DomainException(Bundle.EXPENDITURE, "error.financer.acquisition.request.already.has.paying.unit");
        }

        setFundedRequest(acquisitionRequest);
        setUnit(costCenter);
        setAccountingUnit(costCenter.getAccountingUnit());
    }

    public boolean isProjectFinancer() {
        return false;
    }

    public void delete() {
        if (checkIfCanDelete()) {
            setExpenditureTrackingSystem(null);
            setFundedRequest(null);
            setUnit(null);
            setAccountingUnit(null);
            getAllocatedInvoices().clear();
            deleteDomainObject();
        }
    }

    private boolean checkIfCanDelete() {
        if (hasAnyUnitItems()) {
            throw new DomainException(Bundle.ACQUISITION,
                    "acquisitionProcess.message.exception.cannotRemovePayingUnit.alreadyAssignedToItems");
        }
        return true;
    }

    public Money getAmountAllocated() {
        Money amount = Money.ZERO;
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getRoundedRealShareValue() != null) {
                amount = amount.add(unitItem.getRoundedRealShareValue());
            } else if (unitItem.getRoundedShareValue() != null) {
                amount = amount.add(unitItem.getRoundedShareValue());
            }
        }
        return amount;
    }

    public Money getInitialAmountAllocated() {
        Money amount = Money.ZERO;
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getRoundedShareValue() != null) {
                amount = amount.add(unitItem.getRoundedShareValue());
            }
        }
        return amount;
    }

    public Money getRealShareValue() {
        Money amount = Money.ZERO;
        for (UnitItem unitItem : getUnitItemsSet()) {
            if (unitItem.getRealShareValue() != null) {
                amount = amount.addAndRound(unitItem.getRealShareValue());
            }
        }
        return amount;
    }

    public Money getShareValue() {
        Money amount = Money.ZERO;
        for (UnitItem unitItem : getUnitItemsSet()) {
            amount = amount.addAndRound(unitItem.getShareValue());
        }
        return amount;
    }

    public boolean isRealUnitShareValueLessThanUnitShareValue() {
        return getRealShareValue().isLessThanOrEqual(getShareValue());
    }

    public boolean isAccountingEmployee(final Person person) {
        return hasAccountingUnit() && getAccountingUnit().getPeopleSet().contains(person);
    }

    public boolean isProjectAccountingEmployee(Person person) {
        return false;
    }

    protected String getAllocationIds(final String id, final String key) {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.AcquisitionResources", I18N.getLocale());
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        stringBuilder.append(resourceBundle.getObject(key));
        stringBuilder.append(' ');
        stringBuilder.append(id == null || id.isEmpty() ? "-" : id);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public String getFundAllocationIds() {
        return getAllocationIds(getFundAllocationId(), "financer.label.allocation.id.prefix.giaf");
    }

    public String getEffectiveFundAllocationIds() {
        Strings strings = getEffectiveFundAllocationId();
        if (strings != null && !strings.isEmpty()) {
            StringBuilder buffer = new StringBuilder("");

            for (String allocationId : strings.getUnmodifiableList()) {
                buffer.append(getAllocationIds(allocationId, "financer.label.allocation.id.prefix.giaf"));
                buffer.append(' ');
            }
            return buffer.toString();
        }
        return getAllocationIds(null, "financer.label.allocation.id.prefix.giaf");
    }

    public boolean hasAllocatedFundsForAllProject() {
        return true;
    }

    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
        return true;
    }

    public boolean hasAllocatedFundsPermanentlyForAnyProjectFinancers() {
        return true;
    }

    public void addEffectiveFundAllocationId(String effectiveFundAllocationId) {
        if (StringUtils.isEmpty(effectiveFundAllocationId)) {
            // throw new DomainException(Bundle.EXPENDITURE, "acquisitionProcess.message.exception.effectiveFundAllocationCannotBeNull");
            return;
        }
        Strings strings = getEffectiveFundAllocationId();
        if (strings == null) {
            strings = new Strings(effectiveFundAllocationId);
        }
        if (!strings.contains(effectiveFundAllocationId)) {
            strings = new Strings(strings, effectiveFundAllocationId);
        }
        setEffectiveFundAllocationId(strings);

        allocateInvoices();

    }

    public void addPaymentDiaryNumber(String paymentReference) {
        if (StringUtils.isEmpty(paymentReference)) {
            return;
            // throw new DomainException(Bundle.EXPENDITURE, "acquisitionProcess.message.exception.paymentReferenceCannotBeNull");
        }
        Strings strings = getPaymentDiaryNumber();
        if (strings == null) {
            strings = new Strings(paymentReference);
        }
        if (!strings.contains(paymentReference)) {
            strings = new Strings(strings, paymentReference);
        }
        setPaymentDiaryNumber(strings);
    }

    public void addTransactionNumber(String transactionNumber) {
        if (StringUtils.isEmpty(transactionNumber)) {
            return;
            // throw new DomainException(Bundle.EXPENDITURE, "acquisitionProcess.message.exception.paymentReferenceCannotBeNull");
        }
        Strings strings = getTransactionNumber();
        if (strings == null) {
            strings = new Strings(transactionNumber);
        }
        if (!strings.contains(transactionNumber)) {
            strings = new Strings(strings, transactionNumber);
        }
        setTransactionNumber(strings);
    }

    private void allocateInvoices() {
        getAllocatedInvoices().clear();
        Set<PaymentProcessInvoice> invoices = new HashSet<PaymentProcessInvoice>();
        for (UnitItem unitItem : getUnitItems()) {
            invoices.addAll(unitItem.getConfirmedInvoices());
        }
        getAllocatedInvoices().addAll(invoices);
    }

    public CostCenter getFinancerCostCenter() {
        return getUnit() != null ? getUnit().getCostCenterUnit() : null;
    }

    public Set<AccountingUnit> getCostCenterAccountingUnits() {
        Set<AccountingUnit> res = new HashSet<AccountingUnit>();
        res.addAll(ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet());
        return res;
    }

    public boolean isAccountingEmployeeForOnePossibleUnit(Person person) {
        for (AccountingUnit accountingUnit : getCostCenterAccountingUnits()) {
            if (accountingUnit.getPeopleSet().contains(person)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFundAllocationId() {
        return getFundAllocationId() != null;
    }

    public boolean hasEffectiveFundAllocationId() {
        return getEffectiveFundAllocationId() != null;
    }

    public boolean hasAnyFundsAllocated() {
        return hasAllocatedFundsForAllProject() && hasFundAllocationId();
    }

    public boolean isTreasuryMember(Person person) {
        return getUnit().isTreasuryMember(person);
    }

    public boolean isProjectAccountingEmployeeForOnePossibleUnit(Person person) {
        return false;
    }

    public boolean hasAllInvoicesAllocatedInProject() {
        return true;
    }

    public boolean hasAllInvoicesAllocated() {
        Collection<PaymentProcessInvoice> allocatedInvoices = getAllocatedInvoices();
        for (UnitItem unitItem : getUnitItems()) {
            if (!allocatedInvoices.containsAll(unitItem.getConfirmedInvoices())) {
                return false;
            }
        }
        return true;
    }

    public void resetEffectiveFundAllocationId() {
        setEffectiveFundAllocationId(null);
        setPaymentDiaryNumber(null);
        setTransactionNumber(null);
        getAllocatedInvoices().clear();
    }

    public boolean isApproved() {
        Collection<UnitItem> unitItems = getUnitItems();
        for (UnitItem unitItem : unitItems) {
            if (!unitItem.isApproved()) {
                return false;
            }
        }
        return !unitItems.isEmpty();
    }

    public boolean isAuthorized() {
        Collection<UnitItem> unitItems = getUnitItems();
        for (UnitItem unitItem : unitItems) {
            if (!unitItem.getItemAuthorized()) {
                return false;
            }
        }
        return !unitItems.isEmpty();
    }

    public boolean isWithInvoicesConfirmed() {
        Collection<UnitItem> unitItems = getUnitItems();
        for (UnitItem unitItem : unitItems) {
            if (!unitItem.isWithAllInvoicesConfirmed()) {
                return false;
            }
        }
        return !unitItems.isEmpty();
    }

    public boolean isFundAllocationPresent() {
        return getFundAllocationId() != null;
    }

    public boolean isEffectiveFundAllocationPresent() {
        return getEffectiveFundAllocationId() != null;
        /*	if (getEffectiveFundAllocationId() != null) {
        	    for (final String s : getEffectiveFundAllocationId()) {
        		if (!s.isEmpty()) {
        		    return true;
        		}
        	    }
        	}
        	return false;
        */
    }

    @Override
    public AccountingUnit getAccountingUnit() {
        final AccountingUnit accountingUnit = super.getAccountingUnit();
        return accountingUnit == null && hasUnit() ? getUnit().getAccountingUnit() : accountingUnit;
    }

    public boolean isAccountManager(final Person accountManager) {
        return getUnit().isAccountManager(accountManager);
    }

    public void createFundAllocationRequest(final boolean isFinalFundAllocation) {
    }

    public PaymentProcess getProcess() {
        final RequestWithPayment fundedRequest = getFundedRequest();
        return fundedRequest.getProcess();
    }

    public void cancelFundAllocationRequest(final boolean isFinalFundAllocation) {
        for (final UnitItem unitItem : getUnitItemsSet()) {
            unitItem.cancelFundAllocationRequest(isFinalFundAllocation);
        }
    }

    public boolean areAllFundsPermanentlyAllocated() {
        final Strings fundAllocationId = getEffectiveFundAllocationId();
        if (hasStringValue(fundAllocationId)) {
            return true;
        }
        final Strings paymentDiaryNumber = getPaymentDiaryNumber();
        final Strings transactionNumber = getTransactionNumber();
        if (hasStringValue(paymentDiaryNumber) && hasStringValue(transactionNumber)) {
            return true;
        }
        return false;
    }

    private boolean hasStringValue(final Strings strings) {
        if (strings != null) {
            for (final String s : strings.getUnmodifiableList()) {
                if (s != null && !s.trim().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCommitted() {
        final String commitmentNumber = getCommitmentNumber();
        return commitmentNumber != null && !commitmentNumber.isEmpty();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem> getUnitItems() {
        return getUnitItemsSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessInvoice> getAllocatedInvoices() {
        return getAllocatedInvoicesSet();
    }

    @Deprecated
    public boolean hasAnyUnitItems() {
        return !getUnitItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyAllocatedInvoices() {
        return !getAllocatedInvoicesSet().isEmpty();
    }

    @Deprecated
    public boolean hasPaymentDiaryNumber() {
        return getPaymentDiaryNumber() != null;
    }

    @Deprecated
    public boolean hasTransactionNumber() {
        return getTransactionNumber() != null;
    }

    @Deprecated
    public boolean hasCommitmentNumber() {
        return getCommitmentNumber() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

    @Deprecated
    public boolean hasAccountingUnit() {
        return getAccountingUnit() != null;
    }

    @Deprecated
    public boolean hasFundedRequest() {
        return getFundedRequest() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

}
