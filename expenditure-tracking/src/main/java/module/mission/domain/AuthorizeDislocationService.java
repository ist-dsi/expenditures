package module.mission.domain;

import java.util.Set;

import module.mission.domain.activity.AuthoriseParticipantActivity;
import module.mission.domain.activity.AuthoriseParticipantActivityInformation;
import pt.ist.fenixWebFramework.services.Service;

public class AuthorizeDislocationService {

	@Service
	public static void authorizeDislocation(final Set<PersonMissionAuthorization> personMissionAuthorizations) {
		for (final PersonMissionAuthorization personMissionAuthorization : personMissionAuthorizations) {
			final Mission mission = personMissionAuthorization.getAssociatedMission();
			final MissionProcess missionProcess = mission.getMissionProcess();
			final AuthoriseParticipantActivity activity =
					(AuthoriseParticipantActivity) missionProcess.getActivity(AuthoriseParticipantActivity.class);
			final AuthoriseParticipantActivityInformation activityInformation =
					(AuthoriseParticipantActivityInformation) activity.getActivityInformation(missionProcess);
			activityInformation.setPersonMissionAuthorization(personMissionAuthorization);
			activity.execute(activityInformation);
		}
	}

}
