package module.mission.domain;

public class TrainItem extends TrainItem_Base {

	public TrainItem() {
		super();
	}

	@Override
	protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
		return new TrainItem();
	}

}
