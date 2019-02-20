/*
 * @(#)WorkingCapitalSystem.java
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

import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.workflow.widgets.ProcessListWidget;
import module.workingCapital.domain.util.WorkingCapitalPendingProcessCounter;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Diogo Figueiredo
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalSystem extends WorkingCapitalSystem_Base {

    static {
        ProcessListWidget.register(new WorkingCapitalPendingProcessCounter());
    }

    @Deprecated
    /**
     * This class is no longer a singleton.
     * Use getInstanceForCurrentHost() instead.
     */
    public static WorkingCapitalSystem getInstance() {
        return getInstanceForCurrentHost();
    }

    public static WorkingCapitalSystem getInstanceForCurrentHost() {
        createSystem();
        return Bennu.getInstance().getWorkingCapitalSystem();
    }

    private WorkingCapitalSystem() {
        super();
        setBennu(Bennu.getInstance());
    }

    @Atomic
    public void resetAcquisitionValueLimit() {
        setAcquisitionValueLimit(null);
    }

    @Atomic
    public static void createSystem() {
        if (Bennu.getInstance().getWorkingCapitalSystem() == null) {
            new WorkingCapitalSystem();
        }
    }

    public SortedSet<Accountability> getManagementMembers() {
        final SortedSet<Accountability> accountingMembers =
                new TreeSet<Accountability>(Accountability.COMPARATOR_BY_CHILD_PARTY_NAMES);
        if (hasManagementUnit() && hasManagingAccountabilityType()) {
            final Unit accountingUnit = getManagementUnit();
            final AccountabilityType accountabilityType = getManagingAccountabilityType();
            accountingUnit.getChildAccountabilityStream()
                    .filter(a -> a.getAccountabilityType() == accountabilityType && a.getChild().isPerson())
                    .forEach(a -> accountingMembers.add(a));
        }
        return accountingMembers;
    }

    public boolean isManagementMember(final User user) {
        return getManagementAccountability(user) != null;
    }

    public Accountability getManagementAccountability(final User user) {
        if (hasManagementUnit() && hasManagingAccountabilityType()) {
            final Unit managementUnit = getManagementUnit();
            final AccountabilityType accountabilityType = getManagingAccountabilityType();
            return findAccountability(user, accountabilityType, managementUnit);
        }
        return null;
    }

    private Accountability findAccountability(final User user, final AccountabilityType accountabilityType, final Unit unit) {
        final Person person = user.getPerson();
        if (person != null) {
            return person.getParentAccountabilityStream().filter(a -> a.isValid() && a.isActiveNow()
                    && a.getAccountabilityType() == accountabilityType && a.getParent() == unit).findAny().orElse(null);
        }
        return null;
    }

    public static boolean isLastMonthForWorkingCapitalTermination() {
        return new DateTime().monthOfYear().get() == DateTimeConstants.DECEMBER;
    }

    @Deprecated
    public boolean hasAcquisitionValueLimit() {
        return getAcquisitionValueLimit() != null;
    }

    @Deprecated
    public java.util.Set<module.workingCapital.domain.WorkingCapitalAcquisition> getWorkingCapitalAcquisitions() {
        return getWorkingCapitalAcquisitionsSet();
    }

    @Deprecated
    public boolean hasAnyWorkingCapitalAcquisitions() {
        return !getWorkingCapitalAcquisitionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<module.workingCapital.domain.WorkingCapitalTransaction> getWorkingCapitalTransactions() {
        return getWorkingCapitalTransactionsSet();
    }

    @Deprecated
    public boolean hasAnyWorkingCapitalTransactions() {
        return !getWorkingCapitalTransactionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<module.workingCapital.domain.WorkingCapitalInitialization> getWorkingCapitalInitializations() {
        return getWorkingCapitalInitializationsSet();
    }

    @Deprecated
    public boolean hasAnyWorkingCapitalInitializations() {
        return !getWorkingCapitalInitializationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasManagingAccountabilityType() {
        return getManagingAccountabilityType() != null;
    }

    @Deprecated
    public java.util.Set<module.workingCapital.domain.WorkingCapitalYear> getWorkingCapitalYears() {
        return getWorkingCapitalYearsSet();
    }

    @Deprecated
    public boolean hasAnyWorkingCapitalYears() {
        return !getWorkingCapitalYearsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<module.workingCapital.domain.WorkingCapital> getWorkingCapitals() {
        return getWorkingCapitalsSet();
    }

    @Deprecated
    public boolean hasAnyWorkingCapitals() {
        return !getWorkingCapitalsSet().isEmpty();
    }

    @Deprecated
    public boolean hasManagementUnit() {
        return getManagementUnit() != null;
    }

    @Deprecated
    public java.util.Set<module.workingCapital.domain.AcquisitionClassification> getAcquisitionClassifications() {
        return getAcquisitionClassificationsSet();
    }

    @Deprecated
    public boolean hasAnyAcquisitionClassifications() {
        return !getAcquisitionClassificationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<module.workingCapital.domain.WorkingCapitalRequest> getWorkingCapitalRequests() {
        return getWorkingCapitalRequestsSet();
    }

    @Deprecated
    public boolean hasAnyWorkingCapitalRequests() {
        return !getWorkingCapitalRequestsSet().isEmpty();
    }

}
