/*
 * @(#)WorkingCapitalInitialization.java
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
package module.workingCapital.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import module.finance.util.Money;
import module.organization.domain.Accountability;
import module.organization.domain.Person;
import module.workingCapital.util.Bundle;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalInitialization extends WorkingCapitalInitialization_Base {

    public static enum WorkingCapitalInitializationState {
        ACTIVE, CANCELED, REJECTED;
    }

    public static final Comparator<WorkingCapitalInitialization> COMPARATOR_BY_REQUEST_CREATION =
            new Comparator<WorkingCapitalInitialization>() {

                @Override
                public int compare(final WorkingCapitalInitialization o1, final WorkingCapitalInitialization o2) {
                    final int c = o1.getRequestCreation().compareTo(o2.getRequestCreation());
                    return c == 0 ? o2.hashCode() - o1.hashCode() : c;
                }

            };

    public WorkingCapitalInitialization() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstanceForCurrentHost());
        final Person person = Authenticate.getUser().getPerson();
        if (person == null) {
            throw new DomainException(Bundle.WORKING_CAPITAL, "message.working.capital.requestor.cannot.be.null");
        }
        setRequestor(person);
        setRequestCreation(new DateTime());
    }

    public WorkingCapitalInitialization(final Integer year, final Unit unit, final Person person,
            final Money requestedAnualValue, final String fiscalId, final String internationalBankAccountNumber) {
        this();
        if (hasAnotherOpenWorkingCapital(unit)) {
            throw new DomainException(Bundle.WORKING_CAPITAL, "message.open.working.capital.exists.for.unit");
        }
        if (isRequestorOfAnotherOpenWorkingCapitalFromPreviousYears()) {
            throw new DomainException(Bundle.WORKING_CAPITAL, "message.requestor.of.open.working.capital.from.previous.years");
        }
        if (isMovementResponsibleOfAnotherOpenWorkingCapitalFromPreviousYears(person)) {
            throw new DomainException(Bundle.WORKING_CAPITAL,
                    "message.movement.responsible.of.open.working.capital.from.previous.years", person.getUser().getProfile()
                            .getFullName());
        }
        pt.ist.expenditureTrackingSystem.domain.organization.Person responsible =
                getDirectUnitResponsibleOfAnotherOpenWorkingCapitalFromPreviousYears(unit);
        if (responsible != null) {
            throw new DomainException(Bundle.WORKING_CAPITAL,
                    "message.unit.responsible.of.open.working.capital.from.previous.years", responsible.getUser().getProfile()
                            .getFullName());
        }

        WorkingCapital workingCapital = WorkingCapital.find(year, unit);
        if (workingCapital == null) {
            workingCapital = new WorkingCapital(year, unit, person);
        }
        setWorkingCapital(workingCapital);
        setRequestedAnualValue(requestedAnualValue);
        setFiscalId(fiscalId);
        setInternationalBankAccountNumber(internationalBankAccountNumber);
    }

    private boolean hasAnotherOpenWorkingCapital(Unit unit) {
        for (WorkingCapital workingCapital : unit.getWorkingCapitalsSet()) {
            WorkingCapitalInitialization initialization = workingCapital.getWorkingCapitalInitialization();
            if (initialization == null) {
                continue;
            }
            if (initialization == this) {
                continue;
            }

            if (initialization.isOpen()) {
                return true;
            }
        }
        return false;
    }

    private boolean isRequestorOfAnotherOpenWorkingCapitalFromPreviousYears() {
        for (WorkingCapitalInitialization initialization : Authenticate.getUser().getPerson()
                .getRequestedWorkingCapitalInitializationsSet()) {
            if (initialization == this) {
                continue;
            }
            if (initialization.getWorkingCapital().getWorkingCapitalYear().getYear()
                    .equals(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                continue;
            }
            if (initialization.isOpen()) {
                return true;
            }
        }
        return false;
    }

    private boolean isMovementResponsibleOfAnotherOpenWorkingCapitalFromPreviousYears(Person person) {
        for (WorkingCapital workingCapital : person.getMovementResponsibleWorkingCapitalsSet()) {
            if (workingCapital == getWorkingCapital()) {
                continue;
            }
            WorkingCapitalInitialization initialization = workingCapital.getWorkingCapitalInitialization();
            if (initialization == null) {
                continue;
            }
            if (initialization.getWorkingCapital().getWorkingCapitalYear().getYear()
                    .equals(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                continue;
            }
            if (initialization.isOpen()) {
                return true;
            }
        }
        return false;
    }

    private pt.ist.expenditureTrackingSystem.domain.organization.Person getDirectUnitResponsibleOfAnotherOpenWorkingCapitalFromPreviousYears(
            Unit unit) {
        for (pt.ist.expenditureTrackingSystem.domain.organization.Person responsible : getUnitResponsibles(unit)) {
            if (isDirectUnitResponsibleOfAnotherOpenWorkingCapitalFromPreviousYears(responsible)) {
                return responsible;
            }
        }
        return null;
    }

    private static Collection<pt.ist.expenditureTrackingSystem.domain.organization.Person> getUnitResponsibles(Unit unit) {
        Set<pt.ist.expenditureTrackingSystem.domain.organization.Person> responsibles =
                new HashSet<pt.ist.expenditureTrackingSystem.domain.organization.Person>();
        for (Authorization authorization : unit.getAuthorizationsSet()) {
            if (authorization.isValid()) {
                responsibles.add(authorization.getPerson());
            }
        }
        return responsibles;
    }

    private boolean isDirectUnitResponsibleOfAnotherOpenWorkingCapitalFromPreviousYears(
            pt.ist.expenditureTrackingSystem.domain.organization.Person responsible) {
        for (Unit unit : getResponsibleUnits(responsible)) {
            if (hasAnotherOpenWorkingCapitalFromPreviousYears(unit)) {
                return true;
            }
        }
        return false;
    }

    private static Collection<Unit> getResponsibleUnits(pt.ist.expenditureTrackingSystem.domain.organization.Person responsible) {
        Set<Unit> responsibleUnits = new HashSet<Unit>();
        for (Authorization authorization : responsible.getAuthorizationsSet()) {
            if (authorization.isValid()) {
                responsibleUnits.add(authorization.getUnit());
            }
        }
        return responsibleUnits;
    }

    private boolean hasAnotherOpenWorkingCapitalFromPreviousYears(Unit unit) {
        for (WorkingCapital workingCapital : unit.getWorkingCapitalsSet()) {

            WorkingCapitalInitialization initialization = workingCapital.getWorkingCapitalInitialization();
            if (initialization == null) {
                continue;
            }
            if (initialization.getWorkingCapital().getWorkingCapitalYear().getYear()
                    .equals(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                continue;
            }

            if (initialization == this) {
                continue;
            }
            if (initialization.isOpen()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOpen() {
        return !isCanceledOrRejected() && !getWorkingCapital().isRefunded();
    }

    public void approve(final Person person) {
        final WorkingCapital workingCapital = getWorkingCapital();
        final Money valueForAuthorization = Money.ZERO;
        final Authorization authorization = workingCapital.findUnitResponsible(person, valueForAuthorization);
        if (authorization == null) {
            throw new DomainException("person.cannot.approve.expense", person.getUser().getProfile().getFullName());
        }
        setAprovalByUnitResponsible(new DateTime());
        setResponsibleForUnitApproval(authorization);
    }

    public void unapprove() {
        setAprovalByUnitResponsible(null);
        setResponsibleForUnitApproval(null);
    }

    public void verify(final User user, final Money authorizedAnualValue, final Money maxAuthorizedAnualValue,
            final String fundAllocationId) {
        setAuthorizedAnualValue(authorizedAnualValue);
        setMaxAuthorizedAnualValue(maxAuthorizedAnualValue);

        if (!isAccountingResponsible(user)) {
            throw new DomainException("person.cannot.verify.expense", user.getPerson().getName());
        }
        setVerificationByAccounting(new DateTime());
        setResponsibleForAccountingVerification(user.getPerson());
        allocateFunds(fundAllocationId);
    }

    public void allocateFunds(final String fundAllocationId) {
        final String value = fundAllocationId != null && !fundAllocationId.isEmpty() ? fundAllocationId : null;
        setFundAllocationId(value);
    }

    private boolean isAccountingResponsible(final User user) {
        return getWorkingCapital().isAccountingResponsible(user);
    }

    public void unverify() {
        setVerificationByAccounting(new DateTime());
        setResponsibleForAccountingVerification(null);
        setAuthorizedAnualValue(null);
        setMaxAuthorizedAnualValue(null);
        setFundAllocationId(null);
    }

    public void unverifyCentral() {
        setVerificationByCentral(null);
        setResponsibleForCentralVerification(null);
    }

    public void verifyCentral(User user) {
        setVerificationByCentral(new DateTime());
        setResponsibleForCentralVerification(user.getPerson());
    }

    public void authorize(final User user) {
        final WorkingCapitalSystem workingCapitalSystem = WorkingCapitalSystem.getInstanceForCurrentHost();
        final Accountability accountability = workingCapitalSystem.getManagementAccountability(user);
        if (accountability == null) {
            throw new DomainException("person.cannot.authorize.expense", user.getPerson().getName());
        }
        setAuthorizationByUnitResponsible(new DateTime());
        setResponsibleForUnitAuthorization(accountability);
    }

    public void unauthorize() {
        setAuthorizationByUnitResponsible(new DateTime());
        setResponsibleForUnitAuthorization(null);
    }

    public boolean isPendingAproval(User user) {
        if (getAcceptedResponsability() != null && !hasResponsibleForUnitApproval()) {
            //final Money valueForAuthorization = getRequestedAnualValue();
            final Money valueForAuthorization = Money.ZERO;
            final Authorization authorization = getWorkingCapital().findUnitResponsible(user.getPerson(), valueForAuthorization);
            if (authorization != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isPendingDirectAproval(User user) {
        if (getAcceptedResponsability() != null && !hasResponsibleForUnitApproval()) {
            //final Money valueForAuthorization = getRequestedAnualValue();
            final Money valueForAuthorization = Money.ZERO;
            final Authorization authorization =
                    getWorkingCapital().findDirectUnitResponsible(user.getPerson(), valueForAuthorization);
            if (authorization != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isPendingAproval() {
        return !hasResponsibleForUnitApproval();
    }

    public boolean isPendingVerification() {
        return hasResponsibleForUnitApproval() && !hasResponsibleForAccountingVerification();
    }

    public boolean isPendingCentralVerification() {
        return getResponsibleForAccountingVerification() != null && getResponsibleForCentralVerification() == null
                && getFundAllocationId() != null && !getFundAllocationId().isEmpty() && !hasResponsibleForUnitAuthorization();
    }

    public boolean isPendingAuthorization() {
        return getResponsibleForAccountingVerification() != null && getResponsibleForCentralVerification() != null
                && getFundAllocationId() != null && !getFundAllocationId().isEmpty() && !hasResponsibleForUnitAuthorization();
    }

    public boolean isAuthorized() {
        return hasResponsibleForUnitAuthorization();
    }

    public void cancel() {
        setState(WorkingCapitalInitializationState.CANCELED);
    }

    public void reject() {
        setState(WorkingCapitalInitializationState.REJECTED);
    }

    public boolean isCanceledOrRejected() {
        final WorkingCapitalInitializationState state = getState();
        return state == WorkingCapitalInitializationState.CANCELED || state == WorkingCapitalInitializationState.REJECTED;
    }

    @Override
    public AccountingUnit getAccountingUnit() {
        final AccountingUnit accountingUnit = super.getAccountingUnit();
        return accountingUnit == null ? (hasWorkingCapital() ? getWorkingCapital().getUnit().getAccountingUnit() : null) : accountingUnit;
    }

    public boolean isPendingFundAllocation() {
        return !isCanceledOrRejected() && (getFundAllocationId() == null || getFundAllocationId().isEmpty())
                && hasResponsibleForAccountingVerification() && !hasResponsibleForUnitAuthorization();
    }

    public boolean isPendingFundUnAllocation() {
        return (isCanceledOrRejected() && (getFundAllocationId() != null) && (!getFundAllocationId().isEmpty()));
    }

    public void delete() {
        setWorkingCapital(null);
        setWorkingCapitalSystem(null);
        setRequestor(null);
        setAccountingUnit(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasRequestCreation() {
        return getRequestCreation() != null;
    }

    @Deprecated
    public boolean hasRequestedAnualValue() {
        return getRequestedAnualValue() != null;
    }

    @Deprecated
    public boolean hasAuthorizedAnualValue() {
        return getAuthorizedAnualValue() != null;
    }

    @Deprecated
    public boolean hasMaxAuthorizedAnualValue() {
        return getMaxAuthorizedAnualValue() != null;
    }

    @Deprecated
    public boolean hasFiscalId() {
        return getFiscalId() != null;
    }

    @Deprecated
    public boolean hasInternationalBankAccountNumber() {
        return getInternationalBankAccountNumber() != null;
    }

    @Deprecated
    public boolean hasAprovalByUnitResponsible() {
        return getAprovalByUnitResponsible() != null;
    }

    @Deprecated
    public boolean hasAuthorizationByUnitResponsible() {
        return getAuthorizationByUnitResponsible() != null;
    }

    @Deprecated
    public boolean hasVerificationByAccounting() {
        return getVerificationByAccounting() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasLastSubmission() {
        return getLastSubmission() != null;
    }

    @Deprecated
    public boolean hasRefundRequested() {
        return getRefundRequested() != null;
    }

    @Deprecated
    public boolean hasAcceptedResponsability() {
        return getAcceptedResponsability() != null;
    }

    @Deprecated
    public boolean hasFundAllocationId() {
        return getFundAllocationId() != null;
    }

    @Deprecated
    public boolean hasWorkingCapital() {
        return getWorkingCapital() != null;
    }

    @Deprecated
    public boolean hasResponsibleForAccountingVerification() {
        return getResponsibleForAccountingVerification() != null;
    }

    @Deprecated
    public boolean hasWorkingCapitalSystem() {
        return getWorkingCapitalSystem() != null;
    }

    @Deprecated
    public boolean hasRequestor() {
        return getRequestor() != null;
    }

    @Deprecated
    public boolean hasResponsibleForUnitAuthorization() {
        return getResponsibleForUnitAuthorization() != null;
    }

    @Deprecated
    public boolean hasResponsibleForUnitApproval() {
        return getResponsibleForUnitApproval() != null;
    }

    @Deprecated
    public boolean hasAccountingUnit() {
        return getAccountingUnit() != null;
    }

}