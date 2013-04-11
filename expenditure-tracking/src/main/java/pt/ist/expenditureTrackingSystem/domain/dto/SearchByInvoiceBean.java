/*
 * @(#)SearchByInvoiceBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
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
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessInvoice;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

/**
 * 
 * @author Bruno Santos
 * 
 */
public class SearchByInvoiceBean implements Serializable {

    String invoiceId;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId.trim();
    }

    public ArrayList<PaymentProcess> search() {
        ArrayList<PaymentProcess> resultsList = new ArrayList<PaymentProcess>();
        if (StringUtils.isEmpty(invoiceId)) {
            return resultsList;
        }

        Collection<PaymentProcessYear> processesYears = ExpenditureTrackingSystem.getInstance().getPaymentProcessYears();

        for (PaymentProcessYear year : processesYears) {
            for (GenericProcess process : year.getPaymentProcessSet()) {
                if (process instanceof PaymentProcess) {
                    RequestWithPayment request = ((PaymentProcess) process).getRequest();
                    if (request != null) {
                        for (PaymentProcessInvoice invoice : request.getInvoices()) {
                            if (invoice.getInvoiceNumber().equals(invoiceId)) {
                                resultsList.add((PaymentProcess) process);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return resultsList;
    }
}
