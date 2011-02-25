package module.organizationIst.domain.task;

import module.organization.domain.Accountability;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organizationIst.domain.IstAccountabilityType;
import myorg.domain.User;
import myorg.domain.scheduler.WriteCustomTask;
import net.sourceforge.fenixedu.domain.RemotePerson;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class CallImportEmployeesAndResponsiblesAux extends WriteCustomTask {

    @Override
    protected void doService() {
	final User user = User.findByUsername("ist24439");
	final Person somePerson = user.getPerson();
	final RemotePerson someRemotePerson = somePerson.getRemotePerson();

//	final String allTeacherInformation = someRemotePerson.readAllTeacherInformation();
//	updateInformation(IstAccountabilityType.TEACHING_PERSONNEL, allTeacherInformation);
//	final String allResearcherInformation = someRemotePerson.readAllResearcherInformation();
//	updateInformation(IstAccountabilityType.RESEARCH_PERSONNEL, allResearcherInformation);
//	final String allEmployeeInformation = someRemotePerson.readAllEmployeeInformation();
//	updateInformation(IstAccountabilityType.PERSONNEL, allEmployeeInformation);
//	final String allGrantOwnerInformation = someRemotePerson.readAllGrantOwnerInformation();
//	updateInformation(IstAccountabilityType.GRANT_OWNER_PERSONNEL, allGrantOwnerInformation);

  	final String allExternalResearcherInformation = someRemotePerson.readAllExternalResearcherInformation();
	System.out.println("########################################");
	System.out.println(allExternalResearcherInformation);
	System.out.println("-----------------------------------------");
	updateInformation(IstAccountabilityType.EXTERNAL_RESEARCH_PERSONNEL, allExternalResearcherInformation + '|');

    }

    private static void updateInformation(final IstAccountabilityType istAccountabilityType, final String allInformation) {
	final AccountabilityType accountabilityType = istAccountabilityType.readAccountabilityType();
	final LocalDate now = new LocalDate();
	for (int i = 0; i < allInformation.length(); ) {
	    final int sep1 = allInformation.indexOf(':', i);
	    final int sep2 = allInformation.indexOf(':', sep1 + 1);
	    final int sep3 = allInformation.indexOf('|', sep2 + 1);

	    if (sep1 > i && sep2 > sep1 && sep3 > sep2) {
		final String username = allInformation.substring(i, sep1);
		final String costCenterCode = allInformation.substring(sep2 + 1, sep3);

		System.out.println("   " + username);
		System.out.println("   " + costCenterCode);
		System.out.println();
		
		updateInformation(now, accountabilityType, username, costCenterCode);
		i = sep3 + 1;
	    } else {
		i++;
	    }
	}
    }

    private static void updateInformation(final LocalDate now, final AccountabilityType accountabilityType, final String username, final String costCenterCode) {
	final User user = User.findByUsername(username);
	if (user != null) {
	    final Person person = user.getPerson();
	    if (person != null) {
		final Unit unit = CostCenter.findUnitByCostCenter(costCenterCode);
		if (unit != null) {
		    updateInformation(now, accountabilityType, person, unit.getUnit());
		} else {
		    System.out.println("Did not find cost center with code: " + costCenterCode);
		}
	    } else {
		System.out.println("User with username: " + username + " has no person");
	    }
	} else {
	    System.out.println("Did not find user with username: " + username);
	}
    }

    private static void updateInformation(final LocalDate now, final AccountabilityType accountabilityType, final Person person, final module.organization.domain.Unit unit) {
	for (final Accountability accountability : person.getParentAccountabilitiesSet()) {
	    System.out.println("      processing " + accountability.getAccountabilityType().getName().getContent() + " of " + accountability.getBeginDate().toString());
	    if (accountability.getAccountabilityType() == accountabilityType && accountability.isActive(now)) {
		System.out.println("      found mathing type that is active now.");
		if (accountability.getParent() == unit) {
		    System.out.println("      matched unit... nothing to do: " + unit.getPresentationName());
		    return;
		} else {
		    accountability.setEndDate(now.minusDays(1));
		}
	    }
	}
	System.out.println("Creating accountability for: " + person.getPresentationName() + " of type: " + accountabilityType.getName().getContent());
	unit.addChild(person, accountabilityType, now, null);
    }

}
