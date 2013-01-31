package module.mission.domain;

public class BusItem extends BusItem_Base {

	public BusItem() {
		super();
	}

	@Override
	protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
		return new BusItem();
	}

}
