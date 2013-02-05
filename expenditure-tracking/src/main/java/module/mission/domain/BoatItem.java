package module.mission.domain;

public class BoatItem extends BoatItem_Base {

    public BoatItem() {
        super();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new BoatItem();
    }

}
