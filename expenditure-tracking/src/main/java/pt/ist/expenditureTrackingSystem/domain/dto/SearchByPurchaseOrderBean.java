/*
 * @(#)SearchByPurchaseOrderBean.java
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
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;

/**
 * 
 * @author Jorge Goulart
 * 
 */
public class SearchByPurchaseOrderBean implements Serializable {

    String purchaseOrderId;

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String orderId) {
        this.purchaseOrderId = orderId.trim();
    }

    public ArrayList<PaymentProcess> search() {
        ArrayList<PaymentProcess> resultsList = new ArrayList<PaymentProcess>();
        if (StringUtils.isEmpty(purchaseOrderId)) {
            return resultsList;
        }

        Collection<PaymentProcessYear> processesYears = ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet();

        for (PaymentProcessYear year : processesYears) {
            for (PaymentProcess process : year.getPaymentProcessSet()) {
                if (process instanceof AcquisitionProcess) {
                    AcquisitionProcess acquisitionProcess = (AcquisitionProcess) process;
                    if(acquisitionProcess.hasPurchaseOrderDocument()) {
                        final String acquisitionRequestDocumentID = acquisitionProcess.getAcquisitionRequestDocumentID();
                        if (acquisitionRequestDocumentID.equals(purchaseOrderId)) {
                            resultsList.add(process);
                            break;
                        }
                    }
                }
            }
        }

        return resultsList;
    }
}
