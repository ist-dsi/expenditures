package module.mission.domain;

import module.organization.domain.AccountabilityType;
import pt.ist.fenixframework.Atomic;

public class MissionAuthorizationAccountabilityType extends MissionAuthorizationAccountabilityType_Base {

    public MissionAuthorizationAccountabilityType(final AccountabilityType accountabilityType) {
        super();
        setMissionSystem(MissionSystem.getInstance());
        setAccountabilityType(accountabilityType);
    }

    public static MissionAuthorizationAccountabilityType find(final AccountabilityType accountabilityType) {
        for (final MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType : MissionSystem.getInstance()
                .getMissionAuthorizationAccountabilityTypesSet()) {
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

    @Atomic
    public void delete() {
        setAccountabilityType(null);
        getAccountabilityTypesSet().clear();
        setMissionSystem(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<module.organization.domain.AccountabilityType> getAccountabilityTypes() {
        return getAccountabilityTypesSet();
    }

    @Deprecated
    public boolean hasAnyAccountabilityTypes() {
        return !getAccountabilityTypesSet().isEmpty();
    }

    @Deprecated
    public boolean hasAccountabilityType() {
        return getAccountabilityType() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

}
