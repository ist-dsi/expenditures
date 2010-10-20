package module.mission.domain;

import java.util.HashMap;
import java.util.Map;

import myorg.applicationTier.Authenticate.UserView;

import org.joda.time.DateTime;

public class MissionVersion extends MissionVersion_Base {

    MissionVersion(final Mission mission) {
	final MissionVersion previous = mission.getMissionVersion();

	setSinceDateTime(new DateTime());
	setMissionSystem(MissionSystem.getInstance());
	setMission(mission);
	setIsArchived(Boolean.FALSE);

	copyVersionInformation(previous);
    }

    protected void copyVersionInformation(final MissionVersion previous) {
	if (previous != null) {
	    setDaparture(previous.getDaparture());
	    setArrival(previous.getArrival());
	    setChangesAfterArrival(previous.getChangesAfterArrival());
	    setSentForTermination(previous.getSentForTermination());
	    setDescriptionOfChangesAfterArrival(previous.getDescriptionOfChangesAfterArrival());
	    setTerminator(previous.getTerminator());

	    final Map<MissionFinancer, MissionFinancer> missionFinancerMap = new HashMap<MissionFinancer, MissionFinancer>();
	    for (final MissionFinancer previousMissionFinancer : previous.getFinancerSet()) {
		final MissionFinancer missionFinancer = previousMissionFinancer.createNewVersion(this);
		missionFinancerMap.put(previousMissionFinancer, missionFinancer);
	    }

	    final Map<MissionItem, MissionItem> missionItemMap = new HashMap<MissionItem, MissionItem>();
	    for (final MissionItem previousMissionItem : previous.getMissionItemsSet()) {
		final MissionItem missionItem = previousMissionItem.createNewVersion(this);
		missionItemMap.put(previousMissionItem, missionItem);
	    }

	    for (final MissionFinancer previousMissionFinancer : previous.getFinancerSet()) {
		final MissionFinancer missionFinancer = missionFinancerMap.get(previousMissionFinancer);

		for (final MissionItemFinancer previousMissionItemFinancer : previousMissionFinancer.getMissionItemFinancersSet()) {

		    final MissionItem previousMissionItem = previousMissionItemFinancer.getMissionItem();
		    final MissionItem missionItem = missionItemMap.get(previousMissionItem);

		    previousMissionItemFinancer.createNewVersion(missionFinancer, missionItem);
		}
	    }
	}
    }

    public void setDates(final DateTime daparture, final DateTime arrival) {
	setDaparture(daparture);
	setArrival(arrival);
    }

    @Override
    public void setChangesAfterArrival(final Boolean changesAfterArrival) {
        super.setChangesAfterArrival(changesAfterArrival);
        if (changesAfterArrival != null) {
            setTerminator(UserView.getCurrentUser().getPerson());
            setSentForTermination(new DateTime());
        }
    }

    public boolean isTerminatedWithChanges() {
	return getChangesAfterArrival() != null && getChangesAfterArrival().booleanValue();
    }

    public void autoArchive() {
	for (final MissionItem missionItem : getMissionItemsSet()) {
	    missionItem.autoArchive();
	}
    }

    public boolean isTerminated() {
	return getChangesAfterArrival() != null;
    }

    public boolean canArchiveMission() {
	if (hasAnyMissionItems()) {
	    for (final MissionItem missionItem : getMissionItemsSet()) {
		if (missionItem.isAccountantForUnArchivedMissionItemFinancer()) {
		    return true;
		}
	    }
	    return !getIsArchived().booleanValue() && areAllItemsArchived() && isAccountantOfProjectAccountant();
	}
	final Mission mission = getMission();
	return mission.isRequestorOrResponsible();
    }

    private boolean isAccountantOfProjectAccountant() {
	for (final MissionItem missionItem : getMissionItemsSet()) {
	    for (final MissionItemFinancer missionItemFinancer : missionItem.getMissionItemFinancersSet()) {
		final MissionFinancer missionFinancer = missionItemFinancer.getMissionFinancer();
		if (missionFinancer.isCurrentUserAccountant() || missionFinancer.isCurrentUserProjectAccountant()) {
		    return true;
		}
	    }
	}
	return false;
    }

    private boolean areAllItemsArchived() {
	for (final MissionItem missionItem : getMissionItemsSet()) {
	    for (final MissionItemFinancer missionItemFinancer : missionItem.getMissionItemFinancersSet()) {
		if (!missionItemFinancer.isArchived()) {
		    return false;
		}
	    }
	}
	return true;
    }

    public boolean hasNoItemsAndParticipantesAreAuthorized() {
	return !hasAnyMissionItems() && getMission().allParticipantsAreAuthorized();
    }

}
