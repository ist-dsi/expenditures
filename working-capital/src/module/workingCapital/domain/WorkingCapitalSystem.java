package module.workingCapital.domain;

import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import myorg.domain.ModuleInitializer;
import myorg.domain.MyOrg;
import myorg.domain.User;
import pt.ist.fenixWebFramework.services.Service;

public class WorkingCapitalSystem extends WorkingCapitalSystem_Base implements ModuleInitializer {

    public static WorkingCapitalSystem getInstance() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasWorkingCapitalSystem()) {
	    initialize();
	}
	return myOrg.getWorkingCapitalSystem();
    }

    @Service
    public synchronized static void initialize() {
	final MyOrg myOrg = MyOrg.getInstance();
	if (!myOrg.hasWorkingCapitalSystem()) {
	    new WorkingCapitalSystem(myOrg);
	}
    }

    private WorkingCapitalSystem(final MyOrg myOrg) {
	super();
	setMyOrg(myOrg);
    }

    public SortedSet<Accountability> getManagementeMembers() {
	final SortedSet<Accountability> accountingMembers = new TreeSet<Accountability>(Accountability.COMPARATOR_BY_CHILD_PARTY_NAMES);
	if (hasManagementUnit() && hasManagementAccountabilityType()) {
	    final Unit accountingUnit = getManagementUnit();
	    final AccountabilityType accountabilityType = getManagementAccountabilityType();
	    for (final Accountability accountability : accountingUnit.getChildAccountabilitiesSet()) {
		if (accountability.getAccountabilityType() == accountabilityType && accountability.getChild().isPerson()) {
		    accountingMembers.add(accountability);
		}
	    }
	}
	return accountingMembers;
    }

    public boolean isManagementeMember(final User user) {
	return getManagementeAccountability(user) != null;
    }

    public Accountability getManagementeAccountability(final User user) {
	if (hasManagementUnit() && hasManagementAccountabilityType()) {
	    final Unit managementeUnit = getManagementUnit();
	    final AccountabilityType accountabilityType = getManagementAccountabilityType();
	    return findAccountability(user, accountabilityType, managementeUnit);
	}
	return null;
    }

    private Accountability findAccountability(final User user, final AccountabilityType accountabilityType, final Unit unit) {
	for (final Accountability accountability : unit.getChildAccountabilitiesSet()) {
	    if (accountability.getAccountabilityType() == accountabilityType && accountability.getChild().isPerson()) {
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
	WorkingCapitalYear.findOrCreate(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

}
