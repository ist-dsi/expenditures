package module.mission.domain;

import module.organization.domain.Person;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

public class AccommodationItem extends AccommodationItem_Base {

    public AccommodationItem() {
	super();
    }

    @Override
    public String getItemDescription() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
		"label.accomodationItem.description", new String[] { getNumberOfNights().toString() });
    }

    @Override
    public Money getPrevisionaryCosts() {
        return getValue();
    }

    @Override
    public boolean isConsistent() {
	if (getNumberOfNights() == null) {
	    return false;
	}
	final int numberOfNights = getNumberOfNights().intValue();
	final Mission mission = getMissionVersion().getMission();
	final int numberOfMissionNights = mission.calculateNumberOfNights();
	return numberOfMissionNights >= numberOfNights && super.isConsistent();
    }

    public static int calculateNumberOfAccomodatedNights(final Mission mission, final Person person) {
	int result = 0;
	for (final MissionItem missionItem : mission.getMissionItemsSet()) {
	    if (missionItem instanceof AccommodationItem && missionItem.getPeopleSet().contains(person)) {
		final AccommodationItem accommodationItem = (AccommodationItem) missionItem;
		final Integer numberOfNights = accommodationItem.getNumberOfNights();
		if (numberOfNights != null) { 
		    result += numberOfNights.intValue();
		}
	    }
	}
	return result;
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
	return new AccommodationItem();
    }

    @Override
    protected void setNewVersionInformation(final MissionItem missionItem) {
	final AccommodationItem accommodationItem = (AccommodationItem) missionItem;
	accommodationItem.setNumberOfNights(getNumberOfNights());
	accommodationItem.setValue(getValue());
    }

}
