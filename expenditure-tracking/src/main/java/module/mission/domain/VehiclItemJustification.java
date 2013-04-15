package module.mission.domain;

import pt.ist.bennu.core.domain.VirtualHost;

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

    @Override
    public boolean isConnectedToCurrentHost() {
        return getMissionSystem() == VirtualHost.getVirtualHostForThread().getMissionSystem();
    }

    @Deprecated
    public boolean hasMotive() {
        return getMotive() != null;
    }

    @Deprecated
    public boolean hasJustification() {
        return getJustification() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

    @Deprecated
    public boolean hasVehiclItem() {
        return getVehiclItem() != null;
    }

}
