package module.mission.domain;

import org.joda.time.DateTime;

import pt.ist.bennu.core.applicationTier.Authenticate.UserView;
import pt.ist.bennu.core.domain.VirtualHost;

public class TemporaryMissionItemEntry extends TemporaryMissionItemEntry_Base {

    private static int NUMBER_OF_DAYS_TO_DELETE_THRESHOLD = 2;

    public TemporaryMissionItemEntry() {
        super();
        setMissionSystem(MissionSystem.getInstance());
        setUser(UserView.getCurrentUser());
        setCreated(new DateTime());
    }

    public TemporaryMissionItemEntry(final MissionItem missionItem) {
        this();
        setMissionItem(missionItem);
    }

    public void delete() {
        removeMissionItem();
        removeUser();
        removeMissionSystem();
        deleteDomainObject();
    }

    public boolean gc() {
        final MissionItem missionItem = getMissionItem();
        if (missionItem == null) {
            delete();
            return true;
        } else if (missionItem.hasMissionVersion()) {
            throw new Error("This should never happen!!!");
        } else if (hasPastThresholdToDelete()) {
            missionItem.delete();
            return true;
        }
        return false;
    }

    private boolean hasPastThresholdToDelete() {
        return getCreated().isBefore(new DateTime().minusDays(NUMBER_OF_DAYS_TO_DELETE_THRESHOLD));
    }

    @Override
    public boolean isConnectedToCurrentHost() {
        return getMissionSystem() == VirtualHost.getVirtualHostForThread().getMissionSystem();
    }

}
