package module.mission.domain;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.bennu.core.util.BundleUtil;

public abstract class TransportationItem extends TransportationItem_Base {

    public TransportationItem() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    @Override
    public String getItemDescription() {
        return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
                "label.transportationItem.description", getItinerary());
    }

    @Override
    public Money getPrevisionaryCosts() {
        return getValue();
    }

    @Override
    protected void setNewVersionInformation(final MissionItem missionItem) {
        final TransportationItem transportationItem = (TransportationItem) missionItem;
        transportationItem.setItinerary(getItinerary());
        transportationItem.setValue(getValue());
    }

}
