package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.HashSet;
import java.util.Set;

import myorg.domain.User;
import myorg.domain.groups.PersistentGroup;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.fenixWebFramework.services.Service;

public class UnitActiveResponsibleGroup extends UnitActiveResponsibleGroup_Base {

    protected UnitActiveResponsibleGroup() {
        super();
        setSystemGroupMyOrg(getMyOrg());
    }

    protected String getNameLable() {
	return "label.persistent.group.unitActiveResponsible.name";
    }

    @Override
    public String getName() {
	return BundleUtil.getStringFromResourceBundle("resources/ExpenditureOrganizationResources", getNameLable());
    }

    protected boolean isExpectedUnitType(final Unit unit) {
	return true;
    }

    @Override
    public boolean isMember(final User user) {
	if (user.hasExpenditurePerson()) {
	    final Person person = user.getExpenditurePerson();
	    for (final Authorization authorization : person.getAuthorizationsSet()) {
		if (authorization.isValid() && isExpectedUnitType(authorization.getUnit())) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public Set<User> getMembers() {
	final Set<User> members = new HashSet<User>();
	for (final Authorization authorization : ExpenditureTrackingSystem.getInstance().getAuthorizationsSet()) {
	    if (authorization.isValid() && isExpectedUnitType(authorization.getUnit())) {
		members.add(authorization.getPerson().getUser());
	    }
	}
	return members;
    }

    @Service
    public static UnitActiveResponsibleGroup getInstance() {
	final UnitActiveResponsibleGroup group = (UnitActiveResponsibleGroup) PersistentGroup.getSystemGroup(UnitActiveResponsibleGroup.class);
	return group == null ? new UnitActiveResponsibleGroup() : group;
    }

}
