package module.mission.domain;

public class PlaneItem extends PlaneItem_Base {

    public PlaneItem() {
        super();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new PlaneItem();
    }

}
