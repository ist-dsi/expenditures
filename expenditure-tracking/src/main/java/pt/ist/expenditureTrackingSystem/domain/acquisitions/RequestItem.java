/*
 * @(#)RequestItem.java
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Diogo Figueiredo
 * @author João Antunes
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public abstract class RequestItem extends RequestItem_Base {

    public RequestItem() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public abstract Money getValue();

    public abstract Money getValueWithoutVat();

    public abstract Money getRealValue();

    public abstract BigDecimal getVatValue();

    public Money getTotalAssigned() {
        Money sum = Money.ZERO;
        for (UnitItem unitItem : getUnitItems()) {
            sum = sum.add(unitItem.getShareValue());
        }
        return sum;
    }

    public Money getTotalRealAssigned() {
        Money sum = Money.ZERO;
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getRealShareValue() != null) {
                sum = sum.add(unitItem.getRealShareValue());
            }
        }
        return sum;
    }

    protected void delete() {
        setCPVReference(null);
        setMaterial(null);
        setExpenditureTrackingSystem(null);
        for (; !getUnitItems().isEmpty(); getUnitItems().iterator().next().delete()) {
            ;
        }
        getInvoicesFilesSet().stream().forEach(i -> getInvoicesFilesSet().remove(i));

        deleteDomainObject();
    }

    public UnitItem getUnitItemFor(Unit unit) {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit() == unit) {
                return unitItem;
            }
        }
        return null;
    }

    public void createUnitItem(Financer financer, Money shareValue) {
        new UnitItem(financer, this, shareValue, Boolean.FALSE, Boolean.FALSE);
    }

    public abstract void createUnitItem(Unit unit, Money shareValue);

    public boolean hasBeenApprovedBy(final Person person) {
        for (final UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit().isResponsible(person) && !unitItem.isApproved()) {
                return false;
            }
        }
        return !getUnitItemsSet().isEmpty();
    }

    public void approve(final Person person) {
        modifySubmittedForFundsAllocationStateFor(person, Boolean.TRUE);
    }

    public void unapprove(final Person person) {
        modifySubmittedForFundsAllocationStateFor(person, Boolean.FALSE);
    }

    public void unapprove() {
        for (UnitItem unitItem : getUnitItems()) {
            unitItem.setSubmitedForFundsAllocation(Boolean.FALSE);
        }
    }

    private void modifySubmittedForFundsAllocationStateFor(final Person person, final Boolean value) {
        for (final UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit().isResponsible(person)) {
                unitItem.setSubmitedForFundsAllocation(value);
            }
        }
    }

    public boolean isPartiallyApproved() {
        if (getUnitItems().size() == 0) {
            return false;
        }

        Boolean value = null;
        for (final UnitItem unitItem : getUnitItems()) {
            Boolean approved = unitItem.isApproved();
            if (value == null) {
                value = approved;
            }
            if (value != approved) {
                return true;
            }
        }
        return false;
    }

    public boolean isApproved() {
        if (getUnitItems().size() == 0) {
            return false;
        }
        for (final UnitItem unitItem : getUnitItems()) {
            if (!unitItem.isApproved()) {
                return false;
            }
        }
        return true;
    }

    public boolean isPartiallyAuthorized() {
        if (getUnitItems().size() == 0) {
            return false;
        }

        Boolean value = null;
        for (final UnitItem unitItem : getUnitItems()) {
            Boolean authorized = unitItem.getItemAuthorized();
            if (value == null) {
                value = authorized;
            }
            if (value != authorized) {
                return true;
            }
        }
        return false;
    }

    public boolean isResponsible(final Person person) {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit().isResponsible(person)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBeenAuthorizedBy(final Person person) {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit().isResponsible(person) && !unitItem.getItemAuthorized()) {
                return false;
            }
        }
        return !getUnitItems().isEmpty();
    }

    public void authorizeBy(Person person) {
        modifyAuthorizationStateFor(person, Boolean.TRUE);
    }

    public void unathorizeBy(Person person) {
        modifyAuthorizationStateFor(person, Boolean.FALSE);
    }
    
    public void unathorize() {
        modifyAuthorizationStateFor(Boolean.FALSE);
    }
    
    private void modifyAuthorizationStateFor(Person person, Boolean value) {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit().isResponsible(person)) {
                unitItem.setItemAuthorized(value);
            }
        }
    }

    private void modifyAuthorizationStateFor(Boolean value) {
        for (UnitItem unitItem : getUnitItems()) {
            unitItem.setItemAuthorized(value);
        }
    }

    public boolean isAuthorized() {
        for (UnitItem unitItem : getUnitItems()) {
            if (!unitItem.getItemAuthorized()) {
                return false;
            }
        }
        return true;
    }

    public List<UnitItem> getSortedUnitItems() {
        List<UnitItem> unitItems = new ArrayList<UnitItem>(getUnitItems());
        Collections.sort(unitItems, new Comparator<UnitItem>() {

            @Override
            public int compare(UnitItem unitItem1, UnitItem unitItem2) {
                return unitItem1.getUnit().getPresentationName().compareTo(unitItem2.getUnit().getPresentationName());
            }

        });

        return unitItems;
    }

    public void clearRealShareValues() {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getRealShareValue() != null) {
                unitItem.setRealShareValue(null);
            }
        }
    }

    public abstract boolean isFilledWithRealValues();

    public boolean isValueFullyAttributedToUnits() {
        Money totalValue = Money.ZERO;
        for (UnitItem unitItem : getUnitItems()) {
            totalValue = totalValue.add(unitItem.getShareValue());
        }

        return totalValue.equals(getValue());
    }

    public boolean isRealValueFullyAttributedToUnits() {
        Money realValue = getRealValue();
        if (realValue == null) {
            return false;
        }
        Money totalValue = Money.ZERO;
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getRealShareValue() != null) {
                totalValue = totalValue.add(unitItem.getRealShareValue());
            }
        }

        return totalValue.equals(realValue);
    }

    public abstract Money getTotalAmountForCPV(final int year);

    public void confirmInvoiceBy(Person person) {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit().isResponsible(person)) {
                unitItem.getConfirmedInvoices().clear();
                for (PaymentProcessInvoice invoice : getInvoicesFiles()) {
                    unitItem.addConfirmedInvoices(invoice);
                }
            }
        }
    }

    public void unconfirmInvoiceBy(Person person) {
        for (UnitItem unitItem : getUnitItems()) {
            if (unitItem.getUnit().isResponsible(person)) {
                unitItem.getConfirmedInvoices().clear();
            }
        }
    }

    public void unconfirmInvoiceForAll(final AcquisitionInvoice acquisitionInvoice) {
        for (UnitItem unitItem : getUnitItems()) {
            if (acquisitionInvoice == null) {
                unitItem.getConfirmedInvoices().clear();
            } else {
                unitItem.getConfirmedInvoices().remove(acquisitionInvoice);
            }
        }
    }

    public boolean hasAtLeastOneInvoiceConfirmation() {
        return !getConfirmedInvoices().isEmpty();
    }

    public boolean isWithInvoicesPartiallyConfirmed() {
        return hasAtLeastOneInvoiceConfirmation() && !isConfirmForAllInvoices();
    }

    public <T extends PaymentProcessInvoice> Collection<T> getConfirmedInvoices() {
        return getConfirmedInvoices(null);
    }

    public <T extends PaymentProcessInvoice> Collection<T> getConfirmedInvoices(Person person) {
        List<T> invoices = new ArrayList<T>();
        for (UnitItem unitItem : getUnitItems()) {
            if (person == null || unitItem.getFinancer().getUnit().isResponsible(person)) {
                invoices.addAll((Collection<T>) unitItem.getConfirmedInvoices());
            }
        }
        return invoices;
    }

    public <T extends PaymentProcessInvoice> List<T> getUnconfirmedInvoices(Person person) {
        Set<T> unconfirmedInvoices = new HashSet<T>();

        for (PaymentProcessInvoice invoice : getInvoicesFiles()) {
            for (UnitItem unitItem : getUnitItems()) {
                if (person == null || unitItem.getFinancer().getUnit().isResponsible(person)) {
                    if (!unitItem.getConfirmedInvoices().contains(invoice)) {
                        unconfirmedInvoices.add((T) invoice);
                    }
                }
            }
        }

        return new ArrayList<T>(unconfirmedInvoices);
    }

    public boolean isConfirmedForAllInvoices(Person person) {
        Collection<PaymentProcessInvoice> allInvoices = getInvoicesFiles();

        if (allInvoices.isEmpty()) {
            return false;
        }

        for (UnitItem unitItem : getUnitItems()) {
            if (person == null || unitItem.getFinancer().getUnit().isResponsible(person)) {
                if (!unitItem.getConfirmedInvoices().containsAll(allInvoices)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isConfirmForAllInvoices() {
        return isConfirmedForAllInvoices(null);
    }

    public boolean isCurrentRealValueFullyAttributedToUnits() {
        return getInvoicesFiles().isEmpty() ? true : isRealValueFullyAttributedToUnits();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessInvoice> getInvoicesFiles() {
        return getInvoicesFilesSet();
    }

    @Deprecated
    public java.util.Set<pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem> getUnitItems() {
        return getUnitItemsSet();
    }

    @Deprecated
    public boolean hasAnyInvoicesFiles() {
        return !getInvoicesFilesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAnyUnitItems() {
        return !getUnitItemsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasClassification() {
        return getClassification() != null;
    }

    @Deprecated
    public boolean hasExpenditureTrackingSystem() {
        return getExpenditureTrackingSystem() != null;
    }

    @Deprecated
    public boolean hasCPVReference() {
        return getCPVReference() != null;
    }

    @Deprecated
    public boolean hasRequest() {
        return getRequest() != null;
    }

    public Money getValueForDistribution() {
        return getValue();
    }
}
