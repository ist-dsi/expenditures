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
import pt.ist.bennu.core.domain.ModuleInitializer;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.fenixWebFramework.services.Service;
import dml.runtime.RelationAdapter;

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
			vh.removeWorkingCapitalSystem();
			super.beforeRemove(vh, myorg);
		}
	}

	static {
		VirtualHost.MyOrgVirtualHost.addListener(new VirtualHostMyOrgRelationListener());

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

	@Service
	public void resetAcquisitionValueLimit() {
		setAcquisitionValueLimit(null);
	}

	@Service
	public static void createSystem(final VirtualHost virtualHost) {
		if (!virtualHost.hasWorkingCapitalSystem() || virtualHost.getWorkingCapitalSystem().getVirtualHostsCount() > 1) {
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
		for (final Accountability accountability : unit.getChildAccountabilitiesSet()) {
			if (accountability.isValid() && accountability.isActiveNow()
					&& accountability.getAccountabilityType() == accountabilityType && accountability.getChild().isPerson()) {
				final Person person = (Person) accountability.getChild();
				if (person.getUser() == user) {
					return accountability;
				}
			}
		}
		return null;
	}

	@Override
	@Service
	public void init(final MyOrg root) {
		final WorkingCapitalSystem workingCapitalSystem = root.getWorkingCapitalSystem();
		if (workingCapitalSystem != null) {
		}
	}

	@Service
	public void setForVirtualHost(final VirtualHost virtualHost) {
		virtualHost.setWorkingCapitalSystem(this);
	}

}
