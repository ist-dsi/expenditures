package module.organizationIst.domain.task;

import java.util.HashSet;
import java.util.Set;

import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organizationIst.domain.IstAccountabilityType;
import myorg.domain.User;
import myorg.domain.scheduler.TransactionalThread;
import net.sourceforge.fenixedu.domain.RemotePerson;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class ImportEmployeeInfo extends TransactionalThread {

    private final String username;
    private Person person;

    public ImportEmployeeInfo(final String username) {
	if (username != null && !username.isEmpty()) {
	    this.username = username;;
	    start();
	} else {
	    throw new Error();
	}
    }

    protected AccountabilityType findAccountabilityType(final String employeeRoleDescription) {
	if ("TEACHER".equals(employeeRoleDescription)) {
	    return IstAccountabilityType.TEACHING_PERSONNEL.readAccountabilityType();
	}
	if ("RESEARCHER".equals(employeeRoleDescription)) {
	    return IstAccountabilityType.RESEARCH_PERSONNEL.readAccountabilityType();
	}
	if ("EMPLOYEE".equals(employeeRoleDescription)) {
	    return IstAccountabilityType.PERSONNEL.readAccountabilityType();
	}
	if ("GRANT_OWNER".equals(employeeRoleDescription)) {
	    return IstAccountabilityType.GRANT_OWNER_PERSONNEL.readAccountabilityType();
	}
	if ("EXTERNAL_RESEARCH_PERSONNEL".equals(employeeRoleDescription)) {
	    return IstAccountabilityType.EXTERNAL_RESEARCH_PERSONNEL.readAccountabilityType();
	}
	return null;
    }

    protected void updateInformation(final LocalDate now, final AccountabilityType accountabilityType, final module.organization.domain.Unit unit) {
	for (final Accountability accountability : person.getParentAccountabilitiesSet()) {
	    if (accountability.getAccountabilityType() == accountabilityType && accountability.isActive(now)) {
		if (accountability.getParent() == unit) {
		    return;
		} else {
		    accountability.editDates(accountability.getBeginDate(), now.minusDays(1));
		}
	    }
	}
	unit.addChild(person, accountabilityType, now, null);
    }

    protected void closeAllRelations() {
	final LocalDate now = new LocalDate();
	final Set<AccountabilityType> accountabilityTypes = new HashSet<AccountabilityType>();
	accountabilityTypes.add(IstAccountabilityType.TEACHING_PERSONNEL.readAccountabilityType());
	accountabilityTypes.add(IstAccountabilityType.RESEARCH_PERSONNEL.readAccountabilityType());
	accountabilityTypes.add(IstAccountabilityType.PERSONNEL.readAccountabilityType());
	accountabilityTypes.add(IstAccountabilityType.GRANT_OWNER_PERSONNEL.readAccountabilityType());
	accountabilityTypes.add(IstAccountabilityType.EXTERNAL_RESEARCH_PERSONNEL.readAccountabilityType());
	for (final Accountability accountability : person.getParentAccountabilitiesSet()) {
	    if (accountabilityTypes.contains(accountability.getAccountabilityType()) && accountability.isActive(now)) {
		accountability.editDates(accountability.getBeginDate(), now.minusDays(1));
	    }
	}
    }

    @Override
    public void transactionalRun() {
	final User user = User.findByUsername(username);
	person = user.getPerson();
	final RemotePerson remotePerson = person.getRemotePerson();
	final String workingPlaceCostCenter = remotePerson.getWorkingPlaceForAnyRoleType();
	final String employeeRoleDescription = remotePerson.getEmployeeRoleDescription();

	final AccountabilityType accountabilityType = findAccountabilityType(employeeRoleDescription);
	final Unit unit = CostCenter.findUnitByCostCenter(workingPlaceCostCenter);

	if (accountabilityType == null || unit == null) {
	    closeAllRelations();
	} else {
	    updateInformation(new LocalDate(), accountabilityType, unit.getUnit());
	}
    }

}
