package module.mission.domain;

import module.finance.util.Money;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public abstract class TransportationItem extends TransportationItem_Base {

    public TransportationItem() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    @Override
    public String getItemDescription() {
        return BundleUtil.getString("resources/MissionResources", "label.transportationItem.description", getItinerary());
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

    @Deprecated
    public boolean hasItinerary() {
        return getItinerary() != null;
    }

    @Deprecated
    public boolean hasValue() {
        return getValue() != null;
    }

}
