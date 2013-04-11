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
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import module.workflow.domain.ProcessDocumentMetaDataResolver;
import module.workflow.domain.ProcessFile;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

@ClassNameBundle(bundle = "resources/AcquisitionResources")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author João Alfaiate
 * 
 */
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
        this.setContent(invoiceFile);
        init(filename, filename, invoiceFile);
    }

    @Override
    public void delete() {
        //let's also remove the fileNode and document, if they aren't used elsewhere
        moveToTrash();

        for (RequestItem item : getRequestItems()) {
            item.clearRealShareValues();
        }
        removeProcess();
        getRequestItems().clear();
        removeSupplier();

        super.delete();
    }

    private void check(RequestItem item, Supplier supplier, Money value, BigDecimal vatValue, Money refundableValue) {
        RefundProcess process = item.getRequest().getProcess();
        if (!process.getShouldSkipSupplierFundAllocation() && isFundAllocationAllowed(supplier, item.getCPVReference(), value)) {
            throw new DomainException("acquisitionRequestItem.message.exception.fundAllocationNotAllowed",
                    DomainException.getResourceFor("resources/AcquisitionResources"));
        }
        Money realValue = item.getRealValue();
        Money estimatedValue = item.getValue();

        if ((realValue != null && realValue.add(refundableValue).isGreaterThan(estimatedValue)) || realValue == null
                && refundableValue.isGreaterThan(estimatedValue.round())) {
            throw new DomainException("refundItem.message.info.realValueLessThanRefundableValue",
                    DomainException.getResourceFor("resources/AcquisitionResources"));
        }

        if (new Money(value.addPercentage(vatValue).getRoundedValue()).isLessThan(refundableValue)) {
            throw new DomainException("refundItem.message.info.refundableValueCannotBeBiggerThanInvoiceValue",
                    DomainException.getResourceFor("resources/AcquisitionResources"));
        }
    }

    private boolean isFundAllocationAllowed(final Supplier supplier, final CPVReference cpvReference, final Money value) {
        return ExpenditureTrackingSystem.getInstance().checkSupplierLimitsByCPV() ? !supplier.isFundAllocationAllowed(
                cpvReference.getCode(), value) : !supplier.isFundAllocationAllowed(value);
    }

    public static class RefundableInvoiceFileMetadataResolver extends InvoiceMetadaResolver {

        public final static String VALUE = "Valor (€)";
        public final static String VAT_VALUE = "Taxa do IVA (%)";
        public final static String REFUNDABLE_VALUE = "Valor a reembolsar (€)";
        public final static String SUPPLIER = "Fornecedor";

        @Override
        public Map<String, String> getMetadataKeysAndValuesMap(ProcessFile processFile) {
            RefundableInvoiceFile refundableInvoiceFile = (RefundableInvoiceFile) processFile;
            Map<String, String> metadataKeysAndValuesMap = super.getMetadataKeysAndValuesMap(processFile);

            Money value = refundableInvoiceFile.getValue();

            if (value != null) {
                metadataKeysAndValuesMap.put(VALUE, value.exportAsString());
            }

            BigDecimal vatValue = refundableInvoiceFile.getVatValue();
            if (vatValue != null) {
                metadataKeysAndValuesMap.put(VAT_VALUE, vatValue.toPlainString());
            }

            Money refundableValue = refundableInvoiceFile.getRefundableValue();
            if (refundableValue != null) {
                metadataKeysAndValuesMap.put(REFUNDABLE_VALUE, refundableValue.exportAsString());
            }

            Supplier supplier = refundableInvoiceFile.getSupplier();
            if (supplier != null) {
                metadataKeysAndValuesMap.put(SUPPLIER, supplier.getPresentationName());
            }

            return metadataKeysAndValuesMap;
        }

    }

    @Override
    public ProcessDocumentMetaDataResolver<ProcessFile> getMetaDataResolver() {
        return new RefundableInvoiceFileMetadataResolver();
    }

    public void editValues(Money value, BigDecimal vatValue, Money refundableValue) {
        check(getRefundItem(), getSupplier(), value, vatValue, refundableValue);
        this.setValue(value);
        this.setVatValue(vatValue);
        this.setRefundableValue(refundableValue);
        getMetaDataResolver().fillMetaDataBasedOnDocument(this);
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
            throw new DomainException("acquisitionRequestItem.message.exception.thereShouldBeOnlyOneRefundItemAssociated");
        }
        return items != null ? (RefundItem) items.iterator().next() : null;
    }

    public boolean isInAllocationPeriod() {
        final RefundProcess refundProcess = getRefundItem().getRequest().getProcess();
        final Integer year = refundProcess.getYear().intValue();
        final int i = Calendar.getInstance().get(Calendar.YEAR);
        return year == i || year == i - 1 || year == i - 2;
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        final RefundItem refundItem = getRefundItem();
        if (refundItem != null) {
            final RequestWithPayment request = refundItem.getRequest();
            if (request != null) {
                final PaymentProcess process = request.getProcess();
                return process != null && process.isConnectedToCurrentHost();
            }
        }
        return false;
    }

}
