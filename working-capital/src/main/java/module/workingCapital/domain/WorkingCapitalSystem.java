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

import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import module.workflow.widgets.ProcessListWidget;
import module.workingCapital.domain.util.WorkingCapitalPendingProcessCounter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import pt.ist.bennu.core.domain.ModuleInitializer;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * 
 * @author Diogo Figueiredo
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class WorkingCapitalSystem extends WorkingCapitalSystem_Base implements ModuleInitializer {

    public static class VirtualHostMyOrgRelationListener extends RelationAdapter<VirtualHost, MyOrg> {

        @Override
        public void beforeRemove(VirtualHost vh, MyOrg myorg) {
            vh.setWorkingCapitalSystem(null);
            super.beforeRemove(vh, myorg);
        }
    }

    static {
        VirtualHost.getRelationMyOrgVirtualHost().addListener(new VirtualHostMyOrgRelationListener());

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
        final VirtualHost virtualHostForThread = VirtualHost.getVirtualHostForThread();
        return (virtualHostForThread == null) ? MyOrg.getInstance().getWorkingCapitalSystem() : virtualHostForThread
                .getWorkingCapitalSystem();
    }

    private WorkingCapitalSystem(final VirtualHost virtualHost) {
        super();
        //setMyOrg(Myorg.getInstance());
        virtualHost.setWorkingCapitalSystem(this);
    }

    @Atomic
    public void resetAcquisitionValueLimit() {
        setAcquisitionValueLimit(null);
    }

    @Atomic
    public static void createSystem(final VirtualHost virtualHost) {
        if (virtualHost.getWorkingCapitalSystem() == null || virtualHost.getWorkingCapitalSystem().getVirtualHosts().size() > 1) {
            new WorkingCapitalSystem(virtualHost);
        }
    }

    public SortedSet<Accountability> getManagementMembers() {
        final SortedSet<Accountability> accountingMembers =
                new TreeSet<Accountability>(Accountability.COMPARATOR_BY_CHILD_PARTY_NAMES);
        if (hasManagementUnit() && hasManagingAccountabilityType()) {
            final Unit accountingUnit = getManagementUnit();
            final AccountabilityType accountabilityType = getManagingAccountabilityType();
            for (final Accountability accountability : accountingUnit.getChildAccountabilitiesSet()) {
                if (accountability.getAccountabilityType() == accountabilityType && accountability.getChild().isPerson()) {
                    accountingMembers.add(accountability);
                }
            }
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
            for (final Accountability accountability : person.getParentAccountabilitiesSet()) {
                if (accountability.isValid() && accountability.isActiveNow()
                        && accountability.getAccountabilityType() == accountabilityType && accountability.getParent() == unit) {
                    return accountability;
                }
            }
        }
        return null;
    }

    @Override
    @Atomic
    public void init(final MyOrg root) {
        final WorkingCapitalSystem workingCapitalSystem = root.getWorkingCapitalSystem();
        if (workingCapitalSystem != null) {
        }
    }

    @Atomic
    public void setForVirtualHost(final VirtualHost virtualHost) {
        virtualHost.setWorkingCapitalSystem(this);
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
    public java.util.Set<pt.ist.bennu.core.domain.VirtualHost> getVirtualHosts() {
        return getVirtualHostsSet();
    }

    @Deprecated
    public boolean hasAnyVirtualHosts() {
        return !getVirtualHostsSet().isEmpty();
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
    public boolean hasMyOrg() {
        return getMyOrg() != null;
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
