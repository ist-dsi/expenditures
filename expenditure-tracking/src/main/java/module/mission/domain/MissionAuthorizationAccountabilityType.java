package module.mission.domain;

import module.organization.domain.AccountabilityType;
import pt.ist.fenixWebFramework.services.Service;

public class MissionAuthorizationAccountabilityType extends MissionAuthorizationAccountabilityType_Base {
    
    public MissionAuthorizationAccountabilityType(final AccountabilityType accountabilityType) {
	super();
	setMissionSystem(MissionSystem.getInstance());
	setAccountabilityType(accountabilityType);
    }

    public static MissionAuthorizationAccountabilityType find(final AccountabilityType accountabilityType) {
	for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : MissionSystem.getInstance().getMissionAuthorizationAccountabilityTypesSet()) {
	    if (missionAuthorizationAccountabilityType.getAccountabilityType() == accountabilityType) {
		return missionAuthorizationAccountabilityType;
	    }
	}
	return null;
    }

    public String getAccountabilityTypesAsString() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final AccountabilityType accountabilityType : getAccountabilityTypesSet()) {
	    if (stringBuilder.length() > 0) {
		stringBuilder.append(", ");
	    }
	    stringBuilder.append(accountabilityType.getName().getContent());
	}
	return stringBuilder.toString();
    }

    @Service
    public void delete() {
	removeAccountabilityType();
	getAccountabilityTypesSet().clear();
	removeMissionSystem();
	deleteDomainObject();
    }

}
