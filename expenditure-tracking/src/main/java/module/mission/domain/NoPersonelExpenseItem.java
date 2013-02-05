package module.mission.domain;

public class NoPersonelExpenseItem extends NoPersonelExpenseItem_Base {

    public NoPersonelExpenseItem() {
        super();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new NoPersonelExpenseItem();
    }

}
