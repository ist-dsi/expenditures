package module.mission.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import module.organization.domain.Unit;
import module.workflow.domain.ProcessCounter;
import module.workflow.domain.WorkflowProcess;
import module.workflow.widgets.ProcessListWidget;
import myorg.domain.MyOrg;
import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.services.Service;

public class MissionSystem extends MissionSystem_Base {

    static boolean migrated = false;

    public static MissionSystem getInstance() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasMissionSystem()) {
	    initialize();
	}

	// The following code is some migration stuff it only has to run once after this code has been deleted.
	if (!migrated) {
	    synchronized (myOrg.getMissionSystem()) {
		if (!migrated) {
		    migrated = true;
		    migrate(myOrg);
		}
	    }
	}

	return myOrg.getMissionSystem();
    }

    @Service
    public synchronized static void initialize() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasMissionSystem()) {
	    new MissionSystem(myOrg);
	}
    }

    @Service
    public static void migrate(final MyOrg myOrg) {
	System.out.println("Migrating mission stuff...");
	// The following code is some migration stuff it only has to run once after this code has been deleted.
	for (final Mission mission : myOrg.getMissionSystem().getMissionsSet()) {
	    mission.migrate();
	}
    }

    private MissionSystem(final MyOrg myOrg) {
	super();
	setMyOrg(myOrg);
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
	final Set<AccountabilityType> modelAccountabilityTypes = hasOrganizationalModel()
		? getOrganizationalModel().getAccountabilityTypesSet() : Collections.EMPTY_SET; 
	final Set<AccountabilityType> accountabilityTypes = new HashSet<AccountabilityType>(modelAccountabilityTypes);
	for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : getMissionAuthorizationAccountabilityTypesSet()) {
	    accountabilityTypes.remove(missionAuthorizationAccountabilityType.getAccountabilityType());
	    accountabilityTypes.removeAll(missionAuthorizationAccountabilityType.getAccountabilityTypesSet());
	}
	return accountabilityTypes;
    }

    public Set<AccountabilityType> getAccountabilityTypesForAuthorization(final AccountabilityType accountabilityType) {
	final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType = MissionAuthorizationAccountabilityType.find(accountabilityType);
	return missionAuthorizationAccountabilityType == null ? null : missionAuthorizationAccountabilityType.getAccountabilityTypesSet();
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

}
