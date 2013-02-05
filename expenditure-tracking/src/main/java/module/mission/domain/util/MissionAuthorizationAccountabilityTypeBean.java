package module.mission.domain.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import module.mission.domain.MissionAuthorizationAccountabilityType;
import module.organization.domain.AccountabilityType;
import pt.ist.fenixWebFramework.services.Service;

public class MissionAuthorizationAccountabilityTypeBean implements Serializable {

    private AccountabilityType accountabilityType;
    private List<AccountabilityType> accountabilityTypes = new ArrayList<AccountabilityType>();

    public AccountabilityType getAccountabilityType() {
        return accountabilityType;
    }

    public void setAccountabilityType(AccountabilityType accountabilityType) {
        this.accountabilityType = accountabilityType;
    }

    public List<AccountabilityType> getAccountabilityTypes() {
        return accountabilityTypes;
    }

    public void setAccountabilityTypes(List<AccountabilityType> accountabilityTypes) {
        this.accountabilityTypes = accountabilityTypes;
    }

    @Service
    public void createMissionAuthorizationAccountabilityType() {
        if (accountabilityType != null) {
            MissionAuthorizationAccountabilityType missionAuthorizationAccountabilityType =
                    MissionAuthorizationAccountabilityType.find(accountabilityType);
            if (missionAuthorizationAccountabilityType == null) {
                missionAuthorizationAccountabilityType = new MissionAuthorizationAccountabilityType(accountabilityType);
            }
            missionAuthorizationAccountabilityType.getAccountabilityTypesSet().clear();
            missionAuthorizationAccountabilityType.getAccountabilityTypesSet().addAll(accountabilityTypes);
        }
    }

}
