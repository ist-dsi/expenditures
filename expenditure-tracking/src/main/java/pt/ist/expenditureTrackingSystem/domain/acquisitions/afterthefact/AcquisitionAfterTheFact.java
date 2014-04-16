/*
 * @(#)AcquisitionAfterTheFact.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import java.math.BigDecimal;
import java.util.Calendar;

import org.joda.time.LocalDate;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionItemClassification;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

/**
 * 
 * @author Shezad Anavarali
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AcquisitionAfterTheFact extends AcquisitionAfterTheFact_Base {

    public AcquisitionAfterTheFact(final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess) {
        super();
        setAfterTheFactAcquisitionProcess(afterTheFactAcquisitionProcess);
    }

    public void edit(AfterTheFactAcquisitionType type, Money value, BigDecimal vatValue, Supplier supplier, String description) {
        setDeletedState(Boolean.FALSE);
        setAfterTheFactAcquisitionType(type);
        setSupplier(supplier);
        setValue(value);
        setVatValue(vatValue);
        setDescription(description);
    }

    public void edit(AfterTheFactAcquisitionType type, Money value, BigDecimal vatValue, Supplier supplier, String description,
            CPVReference cpvReference, AcquisitionItemClassification classification) {
        edit(type, value, vatValue, supplier, description);
        setClassification(classification);
        setCpvReference(cpvReference);
    }

    public void delete() {
        setDeletedState(Boolean.TRUE);
    }

    public String getAcquisitionProcessId() {
        return getAfterTheFactAcquisitionProcess().getAcquisitionProcessId();
    }

    @Override
    public void setSupplier(final Supplier supplier) {
        if (supplier != getSupplier()) {
            super.setSupplier(supplier);
            setValue(getValue());
        }
    }

    @Override
    public void setValue(final Money value) {
        if (getValue() == null || value.isGreaterThan(getValue())) {
            super.setValue(Money.ZERO);
            if (getSupplier() != null && !getSupplier().isFundAllocationAllowed(Money.ZERO)) {
                throw new DomainException("acquisitionProcess.message.exception.SupplierDoesNotAlloweAmount",
                        DomainException.getResourceFor("resources/AcquisitionResources"));
            }
        }
        super.setValue(value);
    }

    public boolean isAppiableForYear(final int year) {
        final LocalDate localDate = getAfterTheFactAcquisitionProcess().getInvoice().getInvoiceDate();
        return localDate != null && localDate.getYear() == year;
    }

    public AfterTheFactInvoice receiveInvoice(String filename, byte[] bytes, String invoiceNumber, LocalDate invoiceDate) {
        return getAfterTheFactAcquisitionProcess().receiveInvoice(filename, bytes, invoiceNumber, invoiceDate);
    }

    public String getInvoiceNumber() {
        return getAfterTheFactAcquisitionProcess().getInvoiceNumber();
    }

    public LocalDate getInvoiceDate() {
        return getAfterTheFactAcquisitionProcess().getInvoiceDate();
    }

    public boolean isInAllocationPeriod() {
        final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess = getAfterTheFactAcquisitionProcess();
        final Integer year = afterTheFactAcquisitionProcess.getYear().intValue();
        final int i = Calendar.getInstance().get(Calendar.YEAR);
        return year == i || year == i - 1 || year == i - 2;
    }

    @Deprecated
    public boolean hasAfterTheFactAcquisitionType() {
        return getAfterTheFactAcquisitionType() != null;
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
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasDeletedState() {
        return getDeletedState() != null;
    }

    @Deprecated
    public boolean hasSupplier() {
        return getSupplier() != null;
    }

    @Deprecated
    public boolean hasAfterTheFactAcquisitionProcess() {
        return getAfterTheFactAcquisitionProcess() != null;
    }

}
