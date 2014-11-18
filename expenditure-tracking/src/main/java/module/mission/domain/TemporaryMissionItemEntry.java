package module.mission.domain;

import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

public class TemporaryMissionItemEntry extends TemporaryMissionItemEntry_Base {

    private static int NUMBER_OF_DAYS_TO_DELETE_THRESHOLD = 2;

    public TemporaryMissionItemEntry() {
        super();
        setMissionSystem(MissionSystem.getInstance());
        setUser(Authenticate.getUser());
        setCreated(new DateTime());
    }

    public TemporaryMissionItemEntry(final MissionItem missionItem) {
        this();
        setMissionItem(missionItem);
    }

    public void delete() {
        setMissionItem(null);
        setUser(null);
        setMissionSystem(null);
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

    @Deprecated
    public boolean hasCreated() {
        return getCreated() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

    @Deprecated
    public boolean hasMissionItem() {
        return getMissionItem() != null;
    }

    @Deprecated
    public boolean hasUser() {
        return getUser() != null;
    }

}
