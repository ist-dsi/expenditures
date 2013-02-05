package module.mission.domain;

public class ConferenceItem extends ConferenceItem_Base {

    public ConferenceItem() {
        super();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new ConferenceItem();
    }

}
