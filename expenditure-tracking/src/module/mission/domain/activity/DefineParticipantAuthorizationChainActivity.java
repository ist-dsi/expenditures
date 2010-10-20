package module.mission.domain.activity;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.domain.PersonMissionAuthorization;
import module.mission.domain.util.ParticipantAuthorizationChain.AuthorizationChain;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class DefineParticipantAuthorizationChainActivity extends MissionProcessActivity<MissionProcess, DefineParticipantAuthorizationChainActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user) && missionProcess.isUnderConstruction() && missionProcess.isRequestor(user);
    }

    @Override
    protected void process(final DefineParticipantAuthorizationChainActivityInformation defineParticipantAuthorizationChainActivityInformation) {
	final MissionProcess missionProcess = defineParticipantAuthorizationChainActivityInformation.getProcess();
	final Mission mission = missionProcess.getMission();
	final Person person = defineParticipantAuthorizationChainActivityInformation.getPerson();
	final AuthorizationChain authorizationChain = defineParticipantAuthorizationChainActivityInformation.getAuthorizationChain();

	for (final PersonMissionAuthorization personMissionAuthorization : mission.getPersonMissionAuthorizationsSet()) {
	    if (personMissionAuthorization.getSubject() == person) {
		personMissionAuthorization.delete();
	    }
	}

	new PersonMissionAuthorization(mission, person, authorizationChain);
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
	return new DefineParticipantAuthorizationChainActivityInformation(process, this);
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
