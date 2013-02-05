/*
 * @(#)AfterTheFactPredicate.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.search;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AcquisitionAfterTheFact;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.predicates.SearchPredicate;

/**
 * 
 * @author Paulo Abrantes
 * 
 */
public class AfterTheFactPredicate extends SearchPredicate {

    @Override
    public boolean evaluate(PaymentProcess process, SearchPaymentProcess searchBean) {
        AcquisitionAfterTheFact request = ((AfterTheFactAcquisitionProcess) process).getAcquisitionAfterTheFact();
        return request != null && matchesSearchCriteria(request, searchBean)
                && (process.isAccessibleToCurrentUser() || process.isTakenByCurrentUser());
    }

    private boolean matchesSearchCriteria(AcquisitionAfterTheFact request, SearchPaymentProcess searchBean) {

        return !StringUtils.isEmpty(searchBean.getProcessId())
                && matchCriteria(searchBean.getProcessId(), request.getAfterTheFactAcquisitionProcess().getProcessNumber());

    }
}
