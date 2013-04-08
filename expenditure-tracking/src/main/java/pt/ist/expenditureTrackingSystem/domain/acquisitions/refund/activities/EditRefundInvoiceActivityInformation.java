/*
 * @(#)EditRefundInvoiceActivityInformation.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.math.BigDecimal;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class EditRefundInvoiceActivityInformation extends ActivityInformation<RefundProcess> {

    RefundableInvoiceFile invoice;
    Money value;
    BigDecimal vatValue;
    Money refundableValue;

    public EditRefundInvoiceActivityInformation(RefundProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public RefundableInvoiceFile getInvoice() {
        return invoice;
    }

    public void setInvoice(RefundableInvoiceFile invoice) {
        this.invoice = invoice;
        setValue(invoice.getValue());
        setVatValue(invoice.getVatValue());
        setRefundableValue(invoice.getRefundableValue());
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public BigDecimal getVatValue() {
        return vatValue;
    }

    public void setVatValue(BigDecimal vatValue) {
        this.vatValue = vatValue;
    }

    public Money getRefundableValue() {
        return refundableValue;
    }

    public void setRefundableValue(Money refundableValue) {
        this.refundableValue = refundableValue;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && getValue() != null && getVatValue() != null && getRefundableValue() != null;
    }
}
