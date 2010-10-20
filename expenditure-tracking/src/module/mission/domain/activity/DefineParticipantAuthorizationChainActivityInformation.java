package module.mission.domain.activity;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import module.mission.domain.MissionProcess;
import module.mission.domain.util.ParticipantAuthorizationChain;
import module.mission.domain.util.ParticipantAuthorizationChain.AuthorizationChain;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class DefineParticipantAuthorizationChainActivityInformation extends ParticipantActivityInformation {

    private AuthorizationChain authorizationChain;

    public DefineParticipantAuthorizationChainActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && false;
    }

    public AuthorizationChain getAuthorizationChain() {
        return authorizationChain;
    }

    public void setAuthorizationChain(AuthorizationChain authorizationChain) {
        this.authorizationChain = authorizationChain;
    }

    public SortedMap<Person, Collection<ParticipantAuthorizationChain>> getPossibleParticipantAuthorizationChains() {
	final Person selected = getPerson();
	final SortedMap<Person, Collection<ParticipantAuthorizationChain>> participantAuthorizationChainss = new TreeMap<Person, Collection<ParticipantAuthorizationChain>>(Person.COMPARATOR_BY_NAME);
	for (final Person person : getProcess().getMission().getParticipantesSet()) {
	    if (selected == null || selected == person) {
		final Collection<ParticipantAuthorizationChain> participantAuthorizationChain = ParticipantAuthorizationChain.getParticipantAuthorizationChains(person);
		participantAuthorizationChainss.put(person, participantAuthorizationChain);
	    }
	}
	return participantAuthorizationChainss;
    }

}
