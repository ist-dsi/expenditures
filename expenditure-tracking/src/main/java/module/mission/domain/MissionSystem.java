/*
 * @(#)MissionSystem.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package module.mission.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Party;
import module.organization.domain.Unit;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.domain.VirtualHost;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.services.Service;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class MissionSystem extends MissionSystem_Base {

	public static MissionSystem getInstance() {
		final VirtualHost virtualHostForThread = VirtualHost.getVirtualHostForThread();
		if (!virtualHostForThread.hasMissionSystem()) {
			initialize(virtualHostForThread);
		}

		// The following code is some migration stuff; it only has to run once.
		// TODO: Afterwards, this code can be been deleted.
		if (!isMigrated()) {
			migrate();
		}

		return virtualHostForThread == null ? null : virtualHostForThread.getMissionSystem();
	}

	@Service
	public synchronized static void initialize(VirtualHost virtualHost) {
		if (!virtualHost.hasMissionSystem()) {
			new MissionSystem(virtualHost);
		}
	}

	private static boolean isMigrated() {
		return MyOrg.getInstance().getMissionSystem().hasAnyVirtualHost();
	}

	@Service
	public static void migrate() {
		System.out.println("Migrating mission stuff...");
		MyOrg myOrg = MyOrg.getInstance();
		VirtualHost comprasHost = null;
		VirtualHost dotHost = null;
		for (VirtualHost vHost : myOrg.getVirtualHosts()) {
			if (vHost.getExternalId().equals("395136991233")) {
				comprasHost = vHost;
			} else if (vHost.getExternalId().equals("395136991834")) {
				dotHost = vHost;
			}
		}
		if (comprasHost == null || dotHost == null) {
			throw new RuntimeException("Mission migration error: dot or compras VirtualHosts not found");
		}
		myOrg.getMissionSystem().addVirtualHost(comprasHost);
		myOrg.getMissionSystem().addVirtualHost(dotHost);
	}

	private MissionSystem(final VirtualHost virtualHost) {
		super();
		addVirtualHost(virtualHost);
	}

	public ExpenditureTrackingSystem getExpenditureTrackingSystem() {
		return getVirtualHost().get(0).getExpenditureTrackingSystem();
	}

	public List<pt.ist.expenditureTrackingSystem.domain.organization.Unit> getTopLevelUnitsFromExpenditureSystem() {
		return getExpenditureTrackingSystem().getTopLevelUnits();
	}

	public pt.ist.expenditureTrackingSystem.domain.organization.Unit getFirstTopLevelUnitFromExpenditureSystem() {
		return getExpenditureTrackingSystem().getTopLevelUnits().get(0);
	}

	public Set<AccountabilityType> getAccountabilityTypesThatAuthorize() {
		final Set<AccountabilityType> accountabilityTypes = new HashSet<AccountabilityType>();
		for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : getMissionAuthorizationAccountabilityTypesSet()) {
			accountabilityTypes.addAll(missionAuthorizationAccountabilityType.getAccountabilityTypes());
		}
		return accountabilityTypes;
	}

	public Set<AccountabilityType> getAccountabilityTypesRequireingAuthorization() {
		final Set<AccountabilityType> accountabilityTypes = new HashSet<AccountabilityType>();
		for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : getMissionAuthorizationAccountabilityTypesSet()) {
			accountabilityTypes.add(missionAuthorizationAccountabilityType.getAccountabilityType());
		}
		return accountabilityTypes;
	}

	public Collection<AccountabilityType> getAccountabilityTypesForUnits() {
		final Set<AccountabilityType> modelAccountabilityTypes =
				hasOrganizationalModel() ? getOrganizationalModel().getAccountabilityTypesSet() : Collections.EMPTY_SET;
		final Set<AccountabilityType> accountabilityTypes = new HashSet<AccountabilityType>(modelAccountabilityTypes);
		for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : getMissionAuthorizationAccountabilityTypesSet()) {
			accountabilityTypes.remove(missionAuthorizationAccountabilityType.getAccountabilityType());
			accountabilityTypes.removeAll(missionAuthorizationAccountabilityType.getAccountabilityTypesSet());
		}
		return accountabilityTypes;
	}

	public Set<AccountabilityType> getAccountabilityTypesForAuthorization(final AccountabilityType accountabilityType) {
		final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType =
				MissionAuthorizationAccountabilityType.find(accountabilityType);
		return missionAuthorizationAccountabilityType == null ? null : missionAuthorizationAccountabilityType
				.getAccountabilityTypesSet();
	}

	public Collection<DailyPersonelExpenseTable> getCurrentDailyExpenseTables() {
		final Map<String, DailyPersonelExpenseTable> dailyExpenseTableMap = new HashMap<String, DailyPersonelExpenseTable>();
		for (final DailyPersonelExpenseTable dailyPersonelExpenseTable : getDailyPersonelExpenseTablesSet()) {
			final String aplicableToMissionType = dailyPersonelExpenseTable.getAplicableToMissionType();
			final DailyPersonelExpenseTable existing = dailyExpenseTableMap.get(aplicableToMissionType);
			if (existing == null || existing.getAplicableSince().isBefore(dailyPersonelExpenseTable.getAplicableSince())) {
				dailyExpenseTableMap.put(aplicableToMissionType, dailyPersonelExpenseTable);
			}
		}
		return dailyExpenseTableMap.values();
	}

	@Service
	@Override
	public void setOrganizationalModel(final OrganizationalModel organizationalModel) {
		super.setOrganizationalModel(organizationalModel);
	}

	public boolean isAccountabilityTypesThatAuthorize(final AccountabilityType accountabilityType) {
		for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : getMissionAuthorizationAccountabilityTypesSet()) {
			if (missionAuthorizationAccountabilityType.getAccountabilityTypesSet().contains(accountabilityType)) {
				return true;
			}
		}
		return false;
	}

	@Service
	public void addUnitWithResumedAuthorizations(final Unit unit) {
		addUnitsWithResumedAuthorizations(unit);
	}

	@Service
	public void removeUnitWithResumedAuthorizations(final Unit unit) {
		removeUnitsWithResumedAuthorizations(unit);
	}

	public SortedSet<Unit> getOrderedUnitsWithResumedAuthorizations() {
		final SortedSet<Unit> result = new TreeSet<Unit>(Unit.COMPARATOR_BY_PRESENTATION_NAME);
		result.addAll(getUnitsWithResumedAuthorizationsSet());
		return result;
	}

	public static String getBundle() {
		return "resources.MissionResources";
	}

	public static String getMessage(final String key, String... args) {
		return BundleUtil.getFormattedStringFromResourceBundle(getBundle(), key, args);
	}

	public boolean isManagementCouncilMember(final User user) {
		final OrganizationalModel model = getOrganizationalModel();
		for (final Party party : model.getPartiesSet()) {
			if (party.isUnit() && isManagementCouncilMember(user, model, (Unit) party, true)) {
				return true;
			}
		}
		return false;
	}

	private boolean isManagementCouncilMember(final User user, final OrganizationalModel model, final Unit unit,
			final boolean recurseOverChildren) {
		for (final Accountability accountability : unit.getChildAccountabilitiesSet()) {
			final AccountabilityType accountabilityType = accountability.getAccountabilityType();
			if (model.getAccountabilityTypesSet().contains(accountabilityType)) {
				final Party child = accountability.getChild();
				if ((isAccountabilityTypesThatAuthorize(accountabilityType) && child == user.getPerson())
						|| (recurseOverChildren && child.isUnit() && isManagementCouncilMember(user, model, (Unit) child, false))) {
					return true;
				}
			}
		}
		return false;
	}

	@Service
	@Override
	public void addUsersWhoCanCancelMission(User usersWhoCanCancelMission) {
		super.addUsersWhoCanCancelMission(usersWhoCanCancelMission);
	}

	@Service
	@Override
	public void removeUsersWhoCanCancelMission(User usersWhoCanCancelMission) {
		super.removeUsersWhoCanCancelMission(usersWhoCanCancelMission);
	}

	public static Set<MissionSystem> readAllMissionSystems() {
		Set<MissionSystem> systems = new HashSet<MissionSystem>();
		for (VirtualHost vh : MyOrg.getInstance().getVirtualHosts()) {
			if (vh.hasMissionSystem()) {
				systems.add(vh.getMissionSystem());
			}
		}
		return systems;
	}

	public boolean allowGrantOwnerEquivalence() {
		final Boolean b = getAllowGrantOwnerEquivalence();
		return b != null && b.booleanValue();
	}

	@Service
	public void toggleAllowGrantOwnerEquivalence() {
		final Boolean b = getAllowGrantOwnerEquivalence();
		setAllowGrantOwnerEquivalence(b == null ? Boolean.TRUE : Boolean.valueOf(!b.booleanValue()));
	}

}
