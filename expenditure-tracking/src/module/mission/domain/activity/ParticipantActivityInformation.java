package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import module.mission.domain.util.ImportEmployeeInfoAndUpdateStructure;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class ParticipantActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private Person person;

    public ParticipantActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(final Person person) {
	this.person = person;
	if (person != null && person.hasUser()) {
	    new ImportEmployeeInfoAndUpdateStructure(person.getUser().getUsername());
	}
    }

    @Override
    public boolean hasAllneededInfo() {
	return person != null;
    }

}
