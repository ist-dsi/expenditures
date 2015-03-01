package module.mission.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import module.mission.domain.activity.AuthoriseParticipantActivity;
import module.mission.domain.activity.AuthoriseParticipantActivityInformation;
import pt.ist.fenixframework.Atomic;

public class AuthorizeDislocationService {

    @Atomic
    public static Collection<PersonMissionAuthorization> authorizeDislocation(final Set<PersonMissionAuthorization> personMissionAuthorizations) {
        final Collection<PersonMissionAuthorization> notActive = new HashSet<>();
        for (final PersonMissionAuthorization personMissionAuthorization : personMissionAuthorizations) {
            final Mission mission = personMissionAuthorization.getAssociatedMission();
            final MissionProcess missionProcess = mission.getMissionProcess();
            final AuthoriseParticipantActivity activity =
                    (AuthoriseParticipantActivity) missionProcess.getActivity(AuthoriseParticipantActivity.class);
            if (activity.isActive(missionProcess)) {
                final AuthoriseParticipantActivityInformation activityInformation =
                        (AuthoriseParticipantActivityInformation) activity.getActivityInformation(missionProcess);
                activityInformation.setPersonMissionAuthorization(personMissionAuthorization);
                activity.execute(activityInformation);
            } else {
                notActive.add(personMissionAuthorization);
            }
        }
        return notActive;
    }

}
