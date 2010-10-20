/**
 * 
 */
package module.mission.domain.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import module.mission.domain.ForeignMission;
import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionSystem;
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
	return new SearchResult(MissionSystem.getInstance().getMissions());
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
		    && matchParticipantCriteria(mission.getParticipantes())
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

	private boolean matchParticipantCriteria(List<Person> participantes) {
	    if (participant == null) {
		return true;
	    }

	    for (final Person person : participantes) {
		if (person == participant) {
		    return true;
		}
	    }

	    return false;
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
