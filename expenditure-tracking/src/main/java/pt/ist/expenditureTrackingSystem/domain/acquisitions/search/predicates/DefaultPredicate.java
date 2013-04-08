/*
 * @(#)DefaultPredicate.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.search.predicates;

import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class DefaultPredicate extends SearchPredicate {

    @Override
    public boolean evaluate(PaymentProcess process, SearchPaymentProcess searchBean) {
        RequestWithPayment request = process.getRequest();
        return request != null && matchesSearchCriteria(request, searchBean)
                && (process.isAccessibleToCurrentUser() || process.isTakenByCurrentUser());
    }

    private boolean matchesSearchCriteria(RequestWithPayment request, SearchPaymentProcess searchBean) {
        final Person person = request.getRequester();
        final Set<AccountingUnit> accountingUnits = request.getAccountingUnits();
        final Unit requestingUnit = request.getRequestingUnit();

        return matchCriteria(searchBean.getRequestingPerson(), person)
                && matchContainsCriteria(searchBean.getAccountingUnit(), accountingUnits)
                && matchCriteria(searchBean.getRequestingUnit(), requestingUnit)
                && matchCriteria(searchBean.getPayingUnit(), request.getFinancersSet())
                && matchCriteria(searchBean.getProcessId(), request.getProcess().getAcquisitionProcessId())
                && matchContainsCriteria(searchBean.getCpvReference(), request.getProcess().getCPVReferences());
    }

}
