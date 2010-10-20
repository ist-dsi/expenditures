package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

import org.joda.time.DateTime;

public class UpdateMissionDetailsActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private String location;
    private DateTime daparture;
    private DateTime arrival;
    private String objective;

    public UpdateMissionDetailsActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
	final Mission mission = missionProcess.getMission();
	mission.fill(this);
    }

    @Override
    public boolean hasAllneededInfo() {
	return getLocation() != null && getDaparture() != null && getArrival() != null && getObjective() != null
		&& isForwardedFromInput();
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public DateTime getDaparture() {
	return daparture;
    }

    public void setDaparture(DateTime daparture) {
	this.daparture = daparture;
    }

    public DateTime getArrival() {
	return arrival;
    }

    public void setArrival(DateTime arrival) {
	this.arrival = arrival;
    }

    public String getObjective() {
	return objective;
    }

    public void setObjective(String objective) {
	this.objective = objective;
    }

}
