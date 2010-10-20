package module.mission.domain;

import java.util.Collection;
import java.util.Set;

import module.mission.domain.util.ParticipantAuthorizationChain.AuthorizationChain;
import module.organization.domain.AccountabilityType;
import module.organization.domain.Person;
import module.organization.domain.Unit;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

import org.joda.time.DateTime;

public class PersonMissionAuthorization extends PersonMissionAuthorization_Base {
    
    public PersonMissionAuthorization() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    public PersonMissionAuthorization(final Person subject, AuthorizationChain authorizationChain) {
	this();
	setSubject(subject);
	final Unit unit = authorizationChain.getUnit();
	setUnit(unit);
	if (authorizationChain.getNext() != null) {
	    setNext(new PersonMissionAuthorization(subject, authorizationChain.getNext()));
	}
    }

    public PersonMissionAuthorization(final Mission mission, final Person subject, final AuthorizationChain authorizationChain) {
	this(subject, authorizationChain);
	setMission(mission);
    }

    public void delete() {
	if (hasNext()) {
	    getNext().delete();
	}
	removePrevious();
	removeSubject();
	removeUnit();
	removeMission();
	removeAuthority();
	removeMissionSystem();
	deleteDomainObject();
    }

    public boolean canAuthoriseParticipantActivity() {
	final User user = UserView.getCurrentUser();
	return user != null && canAuthoriseParticipantActivity(user.getPerson());
    }

    public boolean canAuthoriseParticipantActivity(final Person person) {
	if (person == getSubject()) {
	    return false;
	}
	final MissionSystem instance = MissionSystem.getInstance();
	final Set<AccountabilityType> accountabilityTypes = instance.getAccountabilityTypesThatAuthorize();
	//final AccountabilityType accountabilityType = IstAccountabilityType.PERSONNEL_RESPONSIBLE_MISSIONS.readAccountabilityType();
	return (!hasAuthority() && getUnit().hasChildAccountabilityIncludingAncestry(accountabilityTypes, person))
		|| (hasNext() && getNext().canAuthoriseParticipantActivity(person));
    }

    public boolean canUnAuthoriseParticipantActivity() {
	final User user = UserView.getCurrentUser();
	return user != null && canUnAuthoriseParticipantActivity(user.getPerson());
    }

    public boolean canUnAuthoriseParticipantActivity(final Person person) {
	if (person == getSubject()) {
	    return false;
	}
	final MissionSystem instance = MissionSystem.getInstance();
	final Set<AccountabilityType> accountabilityTypes = instance.getAccountabilityTypesThatAuthorize();
	//final AccountabilityType accountabilityType = IstAccountabilityType.PERSONNEL_RESPONSIBLE_MISSIONS.readAccountabilityType();
	return hasAuthority() && canUnAuthorise(person, accountabilityTypes) && ((!hasNext()) || (!getNext().hasAuthority()));
    }

    private boolean canUnAuthorise(final Person person, final Collection<AccountabilityType> accountabilityTypes) {
	return getUnit().hasChildAccountabilityIncludingAncestry(accountabilityTypes, person)
		|| (hasNext() && getNext().canUnAuthorise(person, accountabilityTypes));
    }

    public boolean canUnAuthoriseSomeParticipantActivity(final Person person) {
	return canUnAuthoriseParticipantActivity(person) || (hasNext() && getNext().canUnAuthoriseSomeParticipantActivity(person));
    }

    @Override
    public void setAuthority(final Person authority) {
        super.setAuthority(authority);
        final DateTime authorizationDateTime = authority == null ? null : new DateTime();
        setAuthorizationDateTime(authorizationDateTime);
    }

    public boolean hasAnyAuthorization() {
	return (getAuthorizationDateTime() != null && hasAuthority()) || (hasNext() && getNext().hasAnyAuthorization());
    }

    public boolean isAuthorized() {
	return (!hasNext() && hasAuthority()) || (hasNext() && getNext().isAuthorized());
    }

    public int getChainSize() {
	return hasNext() ? getNext().getChainSize() + 1 : 1;
    }

    public Mission getAssociatedMission() {
	final Mission mission = getMission();
	return mission != null || !hasPrevious() ? mission : getPrevious().getAssociatedMission();
    }

    public void clearAuthorities() {
	if (hasNext()) {
	    getNext().clearAuthorities();
	}
	removeAuthority();
    }

}
