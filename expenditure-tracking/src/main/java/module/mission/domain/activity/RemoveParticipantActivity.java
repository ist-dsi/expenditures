package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.domain.PersonMissionAuthorization;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import pt.ist.bennu.core.domain.User;
import pt.ist.bennu.core.util.BundleUtil;

public class RemoveParticipantActivity extends MissionProcessActivity<MissionProcess, ParticipantActivityInformation> {

    @Override
    public String getLocalizedName() {
        return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
        return super.isActive(missionProcess, user) && missionProcess.isUnderConstruction() && missionProcess.isRequestor(user)
                && missionProcess.getMission().getMissionVersionsCount() == 1
                && !missionProcess.getMission().getParticipantesSet().isEmpty()
                && (!missionProcess.hasAnyMissionItems() || missionProcess.getMission().getParticipantesCount() > 1);
    }

    @Override
    protected void process(final ParticipantActivityInformation participantActivityInformation) {
        final MissionProcess missionProcess = participantActivityInformation.getProcess();
        final Mission mission = missionProcess.getMission();
        final Person person = participantActivityInformation.getPerson();
        for (final PersonMissionAuthorization personMissionAuthorization : mission.getPersonMissionAuthorizationsSet()) {
            if (personMissionAuthorization.getSubject() == person) {
                personMissionAuthorization.delete();
            }
        }
        mission.removeParticipantes(person);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
        return new ParticipantActivityInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}
