package module.mission.domain.activity;

import module.geography.domain.Country;
import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class UpdateForeignMissionDetailsActivityInformation extends UpdateMissionDetailsActivityInformation {

    private Country country;

    public UpdateForeignMissionDetailsActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
	final Mission mission = missionProcess.getMission();
	mission.fill(this);
    }

    @Override
    public boolean hasAllneededInfo() {
	return getCountry() != null && super.hasAllneededInfo();
    }

    public Country getCountry() {
	return country;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

}
