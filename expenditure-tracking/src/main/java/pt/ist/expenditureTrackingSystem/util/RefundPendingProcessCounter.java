/*
 * @(#)RefundPendingProcessCounter.java
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
package pt.ist.expenditureTrackingSystem.util;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.ProcessCounter;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class RefundPendingProcessCounter extends ProcessCounter {

    public RefundPendingProcessCounter() {
        super(RefundProcess.class);
    }

    @Override
    public int getCount() {
        final User user = Authenticate.getUser();
        final PaymentProcessYear year = currentOrLastYear();
        return year == null ? 0 : (int) year.getPaymentProcessSet().stream()
                .filter(p -> p instanceof RefundProcess && shouldCountProcess(p, user))
                .count();
    }

    private static PaymentProcessYear currentOrLastYear() {
        return ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet().stream()
            .max(PaymentProcessYear.COMPARATOR).orElse(null);
    }

    @Override
    public String pathToProcessFrontPage(Class clazz) {
        return "/search.do?method=searchJump&hasAvailableAndAccessibleActivityForUser=true&searchProcessValue=REFUND";
    }

}
