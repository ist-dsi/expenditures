/*
 * @(#)RefundableInvoiceFile.java
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

import java.math.BigDecimal;
import java.util.Collection;

import org.joda.time.LocalDate;

import module.finance.util.Money;
import module.workflow.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author João Alfaiate
 * 
 */
@ClassNameBundle(bundle = "AcquisitionResources")
public class RefundableInvoiceFile extends RefundableInvoiceFile_Base {

    public RefundableInvoiceFile(String invoiceNumber, LocalDate invoiceDate, Money value, BigDecimal vatValue,
            Money refundableValue, byte[] invoiceFile, String filename, RefundItem item, Supplier supplier) {
        super();
        check(item, supplier, value, vatValue, refundableValue);
        this.setInvoiceNumber(invoiceNumber);
        this.setInvoiceDate(invoiceDate);
        this.setValue(value);
        this.setVatValue(vatValue);
        this.setRefundableValue(refundableValue);
        this.addRequestItems(item);
        this.setSupplier(supplier);
        this.setFilename(filename);
        init(filename, filename, invoiceFile);
        final PaymentProcess process = item.getRequest().getProcess();
        setLocalInvoiceNumber(process.getPaymentProcessYear().nextInvoiceNumber());
    }

    @Override
    public void delete() {
        for (RequestItem item : getRequestItems()) {
            item.clearRealShareValues();
        }
        setProcess(null);
        getRequestItems().clear();
        setSupplier(null);

        super.delete();
    }

    private void check(RequestItem item, Supplier supplier, Money value, BigDecimal vatValue, Money refundableValue) {
        RefundProcess process = item.getRequest().getProcess();
        if (!process.getShouldSkipSupplierFundAllocation() && isFundAllocationAllowed(supplier, item.getCPVReference(), value)) {
            throw new DomainException(Bundle.ACQUISITION, "acquisitionRequestItem.message.exception.fundAllocationNotAllowed");
        }
        Money realValue = item.getRealValue();
        Money estimatedValue = item.getValue();

        if ((realValue != null && realValue.add(refundableValue).isGreaterThan(estimatedValue)) || realValue == null
                && refundableValue.isGreaterThan(estimatedValue.round())) {
            throw new DomainException(Bundle.ACQUISITION, "refundItem.message.info.realValueLessThanRefundableValue");
        }

        if (new Money(value.addPercentage(vatValue).getRoundedValue()).isLessThan(refundableValue)) {
            throw new DomainException(Bundle.ACQUISITION, "refundItem.message.info.refundableValueCannotBeBiggerThanInvoiceValue");
        }
    }

    private boolean isFundAllocationAllowed(final Supplier supplier, final CPVReference cpvReference, final Money value) {
        return ExpenditureTrackingSystem.getInstance().checkSupplierLimitsByCPV() ? !supplier.isFundAllocationAllowed(
                cpvReference.getCode(), value) : !supplier.isFundAllocationAllowed(value);
    }

    public void editValues(Money value, BigDecimal vatValue, Money refundableValue) {
        check(getRefundItem(), getSupplier(), value, vatValue, refundableValue);
        this.setValue(value);
        this.setVatValue(vatValue);
        this.setRefundableValue(refundableValue);
    }

    public void resetValues() {
        this.setValue(Money.ZERO);
        this.setVatValue(BigDecimal.ZERO);
        this.setRefundableValue(Money.ZERO);
    }

    public Money getValueWithVat() {
        return getValue().addPercentage(getVatValue());
    }

    public RefundItem getRefundItem() {
        Collection<RequestItem> items = getRequestItems();
        if (items.size() > 1) {
            throw new DomainException(Bundle.EXPENDITURE,
                    "acquisitionRequestItem.message.exception.thereShouldBeOnlyOneRefundItemAssociated");
        }
        return items != null ? (RefundItem) items.iterator().next() : null;
    }

    public boolean isInAllocationPeriod() {
        return getRefundItem().isInAllocationPeriod();
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

    @Deprecated
    public boolean hasVatValue() {
        return getVatValue() != null;
    }

    @Deprecated
    public boolean hasRefundableValue() {
        return getRefundableValue() != null;
    }

    @Deprecated
    public boolean hasSupplier() {
        return getSupplier() != null;
    }

}
