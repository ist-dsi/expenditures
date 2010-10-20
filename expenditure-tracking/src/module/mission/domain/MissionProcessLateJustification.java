package module.mission.domain;

import java.util.Comparator;

import myorg.applicationTier.Authenticate.UserView;

import org.joda.time.DateTime;

public class MissionProcessLateJustification extends MissionProcessLateJustification_Base {

    public static final Comparator<MissionProcessLateJustification> COMPARATOR_BY_JUSTIFICATION_DATETIME =
	new Comparator<MissionProcessLateJustification>() {

	    @Override
	    public int compare(MissionProcessLateJustification o1, MissionProcessLateJustification o2) {
		final int c = o1.getJustificationDateTime().compareTo(o1.getJustificationDateTime());
		return c == 0 ? o2.hashCode() - o1.hashCode() : c;
	    }};

    public MissionProcessLateJustification() {
        super();
        setMissionSystem(MissionSystem.getInstance());
        setJustificationDateTime(new DateTime());
        setPerson(UserView.getCurrentUser().getPerson());
    }

    public MissionProcessLateJustification(final MissionProcess missionProcess, final String justification) {
	this();
	setMissionProcess(missionProcess);
	setJustification(justification);
    }
    
}
