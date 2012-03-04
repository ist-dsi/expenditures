/*
 * @(#)EditRefundInvoiceBean.java
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class EditRefundInvoiceBean extends RefundInvoiceBean implements Serializable {

    private RefundableInvoiceFile invoice;

    public EditRefundInvoiceBean(RefundableInvoiceFile invoice) {
	setInvoice(invoice);
	setInvoiceDate(invoice.getInvoiceDate());
	setInvoiceNumber(invoice.getInvoiceNumber());
	setSupplier(invoice.getSupplier());
	setRefundableValue(invoice.getRefundableValue());
	setValue(invoice.getValue());
	setVatValue(invoice.getVatValue());
    }

    public RefundableInvoiceFile getInvoice() {
	return invoice;
    }

    public void setInvoice(RefundableInvoiceFile invoice) {
	this.invoice = invoice;
    }

}
