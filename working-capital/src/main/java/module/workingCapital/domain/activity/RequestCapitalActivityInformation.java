/*
 * @(#)RequestCapitalActivityInformation.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Working Capital Module.
 *
 *   The Working Capital Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Working Capital Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Working Capital Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.workingCapital.domain.activity;

import java.math.BigDecimal;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalAcquisitionSubmission;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalTransaction;
import module.workingCapital.domain.util.PaymentMethod;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class RequestCapitalActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private Money requestedValue;
    private PaymentMethod paymentMethod;

    public RequestCapitalActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
            final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
        super(workingCapitalProcess, activity);
        final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();
        final WorkingCapitalInitialization workingCapitalInitialization = workingCapital.getWorkingCapitalInitialization();
        final String internationalBankAccountNumber = workingCapitalInitialization.getInternationalBankAccountNumber();
        paymentMethod =
                internationalBankAccountNumber == null || internationalBankAccountNumber.isEmpty() ? PaymentMethod.CHECK : PaymentMethod.WIRETRANSFER;
        if (workingCapital.hasAnyWorkingCapitalTransactions()) {
            final WorkingCapitalTransaction workingCapitalTransaction = workingCapital.getLastTransaction();
            if (workingCapitalTransaction instanceof WorkingCapitalAcquisitionSubmission) {
                final WorkingCapitalAcquisitionSubmission workingCapitalAcquisitionSubmission =
                        (WorkingCapitalAcquisitionSubmission) workingCapitalTransaction;
                requestedValue = workingCapitalAcquisitionSubmission.getValue();
            } else {
                //requestedValue = workingCapitalTransaction.getAccumulatedValue();
                final Money authorizedAnualValue = workingCapitalInitialization.getAuthorizedAnualValue();
                final Money balance = workingCapitalTransaction.getBalance();
                requestedValue = authorizedAnualValue.subtract(balance);
            }
            if (requestedValue.isGreaterThan(workingCapitalInitialization.getAuthorizedAnualValue())) {
                requestedValue = workingCapitalInitialization.getAuthorizedAnualValue();
            }
        } else {
            final Money maxAnualValue = workingCapitalInitialization.getMaxAuthorizedAnualValue();
            requestedValue = maxAnualValue.divideAndRound(new BigDecimal(6));
            final Money anualValue = workingCapitalInitialization.getAuthorizedAnualValue();
            if (requestedValue.isGreaterThan(anualValue)) {
                requestedValue = anualValue;
            }
        }

        final Money allocateableValue = workingCapital.calculateAllocateableValue();
        if (requestedValue.isGreaterThan(allocateableValue)) {
            requestedValue = allocateableValue;
        }
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Money getRequestedValue() {
        return requestedValue;
    }

    public void setRequestedValue(Money requestedValue) {
        this.requestedValue = requestedValue;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && requestedValue != null && !requestedValue.isZero() && paymentMethod != null;
    }

}
