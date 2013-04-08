/*
 * @(#)RefundProcessPredicate.java
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

import pt.ist.bennu.core.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class RefundProcessPredicate extends SearchPredicate {

    @Override
    public boolean evaluate(PaymentProcess refundProcess, SearchPaymentProcess searchBean) {
        final RefundRequest refundRequest = refundProcess.getRequest();
        final Person taker = searchBean.getTaker();
        return matchesSearchCriteria(refundRequest, searchBean)
                && (refundRequest.getProcess().isAvailableForCurrentUser() || refundProcess.isTakenByCurrentUser() || (taker != null && refundProcess
                        .isTakenByPerson(taker.getUser())));
    }

    private boolean matchesSearchCriteria(final RefundRequest refundRequest, SearchPaymentProcess searchBean) {
        final Person person = refundRequest.getRequester();
        final String identification = refundRequest.getAcquisitionProcessId();
        final RefundProcessStateType type = refundRequest.getProcess().getProcessState().getRefundProcessStateType();
        final Set<AccountingUnit> accountingUnits = refundRequest.getAccountingUnits();
        final String refundeeName = refundRequest.getRefundee().getName();
        final Boolean showOnlyWithUnreadComments = searchBean.getShowOnlyWithUnreadComments();
        final RefundProcess process = refundRequest.getProcess();
        User currentOwner = process.getCurrentOwner();
        final Person taker = currentOwner != null ? currentOwner.getExpenditurePerson() : null;
        final Person accountManager = searchBean.getAccountManager();
        final Boolean showPrioritiesOnly = searchBean.getShowPriorityOnly();

        Person loggedPerson = Person.getLoggedPerson();

        return matchCriteria(searchBean.getProcessId(), identification)
                && matchCriteria(searchBean.getRequestingPerson(), person)
                && matchCriteria(searchBean.getRequestingUnit(), refundRequest.getRequestingUnit())
                && matchCriteria(searchBean.getPayingUnit(), refundRequest.getFinancersSet())
                && matchCriteria(searchBean.getHasAvailableAndAccessibleActivityForUser(), refundRequest)
                && matchCriteria(searchBean.getRefundProcessStateType(), type)
                && (!showPrioritiesOnly || process.isPriorityProcess())
                && matchContainsCriteria(searchBean.getAccountingUnit(), accountingUnits)
                && matchCriteria(searchBean.getRefundeeName(), refundeeName)
                && matchCriteria(searchBean.getTaker(), taker)
                && matchesProjectAccountManager(refundRequest, accountManager)
                && (!showOnlyWithUnreadComments || (!process.getUnreadCommentsForPerson(loggedPerson).isEmpty() && process
                        .hasActivitiesFromUser(loggedPerson)))
                && matchContainsCriteria(searchBean.getCpvReference(), refundRequest.getProcess().getCPVReferences());
    }

    private boolean matchCriteria(RefundProcessStateType refundProcessStateType, RefundProcessStateType type) {
        return refundProcessStateType == null || refundProcessStateType.equals(type);
    }

    private boolean matchCriteria(final Boolean hasAvailableAndAccessibleActivityForUser, final RefundRequest refundRequest) {
        return hasAvailableAndAccessibleActivityForUser == null || !hasAvailableAndAccessibleActivityForUser.booleanValue()
                || refundRequest.getProcess().hasAnyAvailableActivity(true);
    }

    private boolean matchesProjectAccountManager(final RefundRequest refundRequest, final Person accountManager) {
        if (accountManager == null) {
            return true;
        }
        for (final Financer financer : refundRequest.getFinancersSet()) {
            if (financer.isAccountManager(accountManager)) {
                return true;
            }
        }
        return false;
    }

}
