/*
 * @(#)RefundRequest.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class RefundRequest extends RefundRequest_Base {

    public RefundRequest(RefundProcess process, Person requestor, String refundeeName, String refundeeFiscalCode,
            Unit requestingUnit) {
        super();
        setProcess(process);
        Refundee refundee = Refundee.getExternalRefundee(refundeeName, refundeeFiscalCode);
        setRefundee(refundee == null ? new Refundee(refundeeName, refundeeFiscalCode) : refundee);
        setRequester(requestor);
        setRequestingUnit(requestingUnit);
    }

    public RefundRequest(RefundProcess process, Person requestor, Person refundeePerson, Unit requestingUnit) {
        super();
        setProcess(process);
        Refundee refundee = refundeePerson.hasRefundee() ? refundeePerson.getRefundee() : new Refundee(refundeePerson);
        setRefundee(refundee);
        setRequester(requestor);
        setRequestingUnit(requestingUnit);
    }

    public void createRefundItem(Money valueEstimation, CPVReference reference, AcquisitionItemClassification classification,
            String description) {
        RefundItem refundItem = new RefundItem(this, valueEstimation, reference, classification, description);
        List<Unit> payingUnits = this.getProcess().getPayingUnits();
        if (payingUnits.size() == 1) {
            refundItem.createUnitItem(payingUnits.get(0), valueEstimation);
        }
    }

    public boolean isEveryItemFullyAttributedToPayingUnits() {
        for (RefundItem item : getRefundItemsSet()) {
            if (!item.isValueFullyAttributedToUnits()) {
                return false;
            }
        }
        return true;
    }

    public boolean isApprovedByAtLeastOneResponsible() {
        for (RefundItem item : getRefundItemsSet()) {
            if (item.hasAtLeastOneResponsibleApproval()) {
                return true;
            }
        }
        return false;
    }

    public Set<RefundItem> getRefundItemsSet() {
        Set<RefundItem> refundItems = new HashSet<RefundItem>();
        for (RequestItem item : getRequestItems()) {
            refundItems.add((RefundItem) item);
        }
        return refundItems;
    }

    public String getAcquisitionProcessId() {
        return getProcess().getAcquisitionProcessId();
    }

    public boolean isPayed() {
        final String reference = getPaymentReference();
        return reference != null && !reference.isEmpty();
    }

    public Set<Supplier> getSuppliers() {
        final Set<Supplier> suppliers = new HashSet<Supplier>();
        for (final RefundItem refundItem : getRefundItemsSet()) {
            refundItem.getSuppliers(suppliers);
        }
        return suppliers;
    }

    @Override
    public SortedSet<RefundItem> getOrderedRequestItemsSet() {
        SortedSet<RefundItem> set = new TreeSet<RefundItem>(RefundItem.COMPARATOR);
        set.addAll(getRefundItemsSet());
        return set;
    }

    public Money getCurrentTotalValue() {
        return hasAnyInvoice() ? getRealTotalValue() : getTotalValue();
    }

    private boolean hasAnyInvoice() {
        for (final RefundItem refundItem : getRefundItemsSet()) {
            for (final PaymentProcessInvoice invoice : refundItem.getInvoicesFiles()) {
                return true;
            }
        }
        return false;
    }

    public boolean allPayingUnitsHaveAccountingUnit() {
        for (final Financer financer : getFinancersSet()) {
            if (financer.getAccountingUnit() == null) {
                return false;
            }
        }
        return true;
    }

    public Set<CPVReference> getCPVReferences() {
        final Set<CPVReference> result = new HashSet<CPVReference>();
        for (final RequestItem requestItem : getRequestItemsSet()) {
            result.add(requestItem.getCPVReference());
        }
        return result;
    }

    public void createFundAllocationRequest(final boolean isFinalFundAllocation) {
        for (final Financer financer : getFinancersSet()) {
            financer.createFundAllocationRequest(isFinalFundAllocation);
        }
    }

    @Override
    public void unapprove(final Person person) {
        super.unapprove(person);
        cancelFundAllocationRequest(false);
    }

    public void cancel() {
        cancelFundAllocationRequest(false);
    }

    public void cancelFundAllocationRequest(final boolean isFinalFundAllocation) {
        for (final Financer financer : getFinancersSet()) {
            financer.cancelFundAllocationRequest(isFinalFundAllocation);
        }
    }

    public AcquisitionItemClassification getGoodsOrServiceClassification() {
        Money goods = Money.ZERO;
        Money services = Money.ZERO;
        for (final RequestItem requestItem : getRequestItemsSet()) {
            final RefundItem refundItem = (RefundItem) requestItem;
            final AcquisitionItemClassification classification = refundItem.getClassification();
            if (classification != null) {
                final Money realValue = refundItem.getRealValue();
                if (realValue != null) {
                    if (classification == AcquisitionItemClassification.GOODS) {
                        goods = goods.add(realValue);
                    } else if (classification == AcquisitionItemClassification.SERVICES) {
                        services = services.add(realValue);
                    } else {
                        throw new Error("unreachable.code");
                    }
                }
            }
        }
        if (goods.isZero() && services.isZero()) {
            return null;
        }
        return goods.isGreaterThan(services) ? AcquisitionItemClassification.GOODS : AcquisitionItemClassification.SERVICES;
    }

    @Deprecated
    public boolean hasPaymentReference() {
        return getPaymentReference() != null;
    }

    @Deprecated
    public boolean hasProcess() {
        return getProcess() != null;
    }

    @Deprecated
    public boolean hasRefundee() {
        return getRefundee() != null;
    }

}
