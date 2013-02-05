package module.mission.domain;

import pt.ist.bennu.core.domain.util.Money;

public class OtherMissionItem extends OtherMissionItem_Base {

    public OtherMissionItem() {
        super();
    }

    @Override
    public String getItemDescription() {
        return getDescription();
    }

    @Override
    public Money getPrevisionaryCosts() {
        return getValue();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new OtherMissionItem();
    }

    @Override
    protected void setNewVersionInformation(final MissionItem missionItem) {
        final OtherMissionItem otherMissionItem = (OtherMissionItem) missionItem;
        otherMissionItem.setDescription(getDescription());
        otherMissionItem.setValue(getValue());
    }

}
