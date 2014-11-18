package module.mission.domain;

import java.util.Comparator;

import org.joda.time.DateTime;

public class MissionChangeDescription extends MissionChangeDescription_Base {

    public static final Comparator<MissionChangeDescription> COMPARATOR_BY_WHEN = new Comparator<MissionChangeDescription>() {

        @Override
        public int compare(MissionChangeDescription o1, MissionChangeDescription o2) {
            final DateTime d1 = o1.getRevertInstant();
            final DateTime d2 = o2.getRevertInstant();
            final int d = d2.compareTo(d1);
            return d == 0 ? o2.getExternalId().compareTo(o1.getExternalId()) : d;
        }

    };

    public MissionChangeDescription(final Mission mission, final String description) {
        setMissionSystem(MissionSystem.getInstance());
        setRevertInstant(new DateTime());
        setMission(mission);
        setDescription(description);
    }

    @Deprecated
    public boolean hasRevertInstant() {
        return getRevertInstant() != null;
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasMission() {
        return getMission() != null;
    }

    @Deprecated
    public boolean hasMissionSystem() {
        return getMissionSystem() != null;
    }

}
