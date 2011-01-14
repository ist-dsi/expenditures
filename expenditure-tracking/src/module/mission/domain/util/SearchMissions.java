/**
 * 
 */
package module.mission.domain.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import module.mission.domain.ForeignMission;
import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.MissionYear;
import module.mission.domain.NationalMission;
import module.organization.domain.Party;
import module.organization.domain.Person;
import myorg.domain.util.Search;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * @author Shezad Anavarali Date: Aug 19, 2009
 * 
 */
public class SearchMissions extends Search<Mission> {

    private static class MissionProcessMissionSet implements Set<Mission> {

	private final Set<MissionProcess> missionProcesses;

	private MissionProcessMissionSet(final Set<MissionProcess> missionProcesses) {
	    this.missionProcesses = missionProcesses;
	}

	@Override
	public Iterator<Mission> iterator() {
	    return new Iterator<Mission>() {

		private final Iterator<MissionProcess> iterator = missionProcesses.iterator();

		@Override
		public boolean hasNext() {
		    return iterator.hasNext();
		}

		@Override
		public Mission next() {
		    return iterator.next().getMission();
		}

		@Override
		public void remove() {
		    throw new IllegalAccessError();
		}
	    };
	}

	@Override
	public boolean add(final Mission e) {
	    throw new IllegalAccessError();
	}

	@Override
	public boolean addAll(Collection<? extends Mission> c) {
	    throw new IllegalAccessError();
	}

	@Override
	public void clear() {
	    throw new IllegalAccessError();
	}

	@Override
	public boolean contains(Object o) {
	    throw new Error("not.implemented");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
	    throw new Error("not.implemented");
	}

	@Override
	public boolean isEmpty() {
	    return missionProcesses.isEmpty();
	}

	@Override
	public boolean remove(Object o) {
	    throw new IllegalAccessError();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
	    throw new IllegalAccessError();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
	    throw new IllegalAccessError();
	}

	@Override
	public int size() {
	    return missionProcesses.size();
	}

	@Override
	public Object[] toArray() {
	    throw new Error("not.implemented");
	}

	@Override
	public <T> T[] toArray(T[] a) {
	    throw new Error("not.implemented");
	}
	
    }

    private String processNumber = Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) + '/';
    private Party missionResponsible;
    private Unit payingUnit;
    private Boolean national = Boolean.TRUE;
    private Boolean foreign = Boolean.TRUE;
    private LocalDate date;
    private Person requestingPerson;
    private Person participant;
    private Boolean filterCanceledProcesses = Boolean.TRUE;

    @Override
    public Set<Mission> search() {
	final Set<Mission> missions = limitSearchSet();
	return new SearchResult(missions);
    }

    private Set<Mission> limitSearchSet() {
	if (missionResponsible != null) {
	    missionResponsible.getMissionsFromResponsibleSet();
	}
	if (requestingPerson != null) {
	    return requestingPerson.getRequestedMissionsSet();
	}
	if (participant != null) {
	    return participant.getMissionsSet();
	}
	if (processNumber != null && !processNumber.isEmpty()) {
	    final int i = processNumber.indexOf('/');
	    if (i > 0) {
		final String year = processNumber.substring(0, i);
		final MissionYear missionYear = MissionYear.findMissionYear(Integer.parseInt(year));
		if (missionYear != null) {
		    return new MissionProcessMissionSet(missionYear.getMissionProcessSet());
		}
	    }
	}
	return MissionSystem.getInstance().getMissionsSet();
    }

    protected class SearchResult extends SearchResultSet<Mission> {

	public SearchResult(Collection<? extends Mission> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(Mission mission) {
	    return matchProcessNumberCriteria(mission)
	    	    && matchRequestingUnitCriteria(mission.getMissionResponsible())
	    	    && matchPayingUnitCriteria(mission.getMissionVersion().getFinancer())
	    	    && matchMissionTypeCriteria(mission)
		    && matchDateCriteria(mission)
		    && matchRequestingPersonCriteria(mission.getRequestingPerson())
		    && matchParticipantCriteria(mission)
		    && matchCanceledCriteria(mission)
		    && mission.getMissionProcess().isAccessibleToCurrentUser();
	}

	private boolean matchCanceledCriteria(final Mission mission) {
	    return filterCanceledProcesses == null || !filterCanceledProcesses.booleanValue()
	    		|| !mission.getMissionProcess().isProcessCanceled();
	}

	private boolean matchProcessNumberCriteria(final Mission mission) {
	    return processNumber == null
	    		|| processNumber.isEmpty()
	    		|| mission.getMissionProcess().getProcessIdentification().indexOf(processNumber) >= 0;
	}

	private boolean matchParticipantCriteria(final Mission mission) {
	    return participant == null || mission.getParticipantesSet().contains(participant);
	}

	private boolean matchRequestingPersonCriteria(Person rp) {
	    return requestingPerson == null || requestingPerson == rp;
	}

	private boolean matchDateCriteria(Mission mission) {
	    return date == null
		    || (mission.getDaparture().isBefore(date.plusDays(1).toDateTimeAtStartOfDay()) && mission.getArrival()
			    .isAfter(date.toDateTimeAtStartOfDay()));
	}

	private boolean matchMissionTypeCriteria(Mission mission) {
	    return (national != null && national.booleanValue() && mission instanceof NationalMission)
	    		|| (foreign != null && foreign.booleanValue() && mission instanceof ForeignMission);
	}

	private boolean matchPayingUnitCriteria(List<MissionFinancer> financers) {
	    if (payingUnit == null) {
		return true;
	    }

	    for (final MissionFinancer missionFinancer : financers) {
		if (missionFinancer.getUnit() == payingUnit) {
		    return true;
		}
	    }

	    return false;
	}

	private boolean matchRequestingUnitCriteria(final Party mr) {
	    return missionResponsible == null || missionResponsible == mr;
	}

    }

    public Unit getPayingUnit() {
	return payingUnit;
    }

    public void setPayingUnit(Unit payingUnit) {
	this.payingUnit = payingUnit;
    }

    public Boolean getForeign() {
	return foreign;
    }

    public void setForeign(Boolean foreign) {
	this.foreign = foreign;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public Person getRequestingPerson() {
	return requestingPerson;
    }

    public void setRequestingPerson(Person requestingPerson) {
	this.requestingPerson = requestingPerson;
    }

    public Person getParticipant() {
	return participant;
    }

    public void setParticipant(Person participant) {
	this.participant = participant;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public Boolean getNational() {
        return national;
    }

    public void setNational(Boolean national) {
        this.national = national;
    }

    public Boolean getFilterCanceledProcesses() {
        return filterCanceledProcesses;
    }

    public void setFilterCanceledProcesses(Boolean filterCanceledProcesses) {
        this.filterCanceledProcesses = filterCanceledProcesses;
    }

    public Party getMissionResponsible() {
        return missionResponsible;
    }

    public void setMissionResponsible(Party missionResponsible) {
        this.missionResponsible = missionResponsible;
    }

}
