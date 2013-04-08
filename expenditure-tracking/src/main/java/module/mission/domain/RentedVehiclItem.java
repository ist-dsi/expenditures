package module.mission.domain;

public class RentedVehiclItem extends RentedVehiclItem_Base {

    public RentedVehiclItem() {
        super();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new RentedVehiclItem();
    }

}
