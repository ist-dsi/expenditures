package module.mission.domain;


public class VehiclItemJustification extends VehiclItemJustification_Base {
    
    public VehiclItemJustification(final VehiclItem vehiclItem) {
	super();
	setMissionSystem(MissionSystem.getInstance());
	setVehiclItem(vehiclItem);
    }

    public void delete() {
	removeVehiclItem();
	removeMissionSystem();
	deleteDomainObject();
    }
    
}
