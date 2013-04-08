package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import module.mission.domain.PersonMissionAuthorization;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class AuthoriseParticipantActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private PersonMissionAuthorization personMissionAuthorization;

    public AuthoriseParticipantActivityInformation(final MissionProcess missionProcess,
            final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
        super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
        return super.hasAllneededInfo() && personMissionAuthorization != null;
    }

    public PersonMissionAuthorization getPersonMissionAuthorization() {
        return personMissionAuthorization;
    }

    public void setPersonMissionAuthorization(final PersonMissionAuthorization personMissionAuthorization) {
        this.personMissionAuthorization = personMissionAuthorization;
    }

}
