/*
 * @(#)AquisitionsPendingProcessCounter.java
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
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcessYear;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.standard.StandardProcedureProcess;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class AquisitionsPendingProcessCounter extends ProcessCounter {

    public AquisitionsPendingProcessCounter() {
        super(AcquisitionProcess.class);
    }

    @Override
    public Class getProcessClassForForwarding() {
        return StandardProcedureProcess.class;
    }

    @Override
    public int getCount() {
        final User user = Authenticate.getUser();
        try {
            final PaymentProcessYear year = currentOrLastYear();
            if (year != null) {
                return (int) year.getPaymentProcessSet().stream()
                        .filter(p -> p instanceof AcquisitionProcess && shouldCountProcess(p, user))
                        .count();
            }
        } catch (final Throwable t) {
            t.printStackTrace();
        }
        return 0;
    }

    private static PaymentProcessYear currentOrLastYear() {
        return ExpenditureTrackingSystem.getInstance().getPaymentProcessYearsSet().stream()
            .max(PaymentProcessYear.COMPARATOR).orElse(null);
    }

    @Override
    public String pathToProcessFrontPage(final Class clazz) {
        return "/search.do?method=searchJump&hasAvailableAndAccessibleActivityForUser=true&searchProcessValue=ACQUISITIONS";
    }

}
