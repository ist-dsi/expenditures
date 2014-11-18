package module.mission.domain;

import module.finance.util.Money;

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

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

}
