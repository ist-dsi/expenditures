package module.workingCapital.presentationTier.action.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Party;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalYear;

public class WorkingCapitalContext implements Serializable {

    private Integer year;
    private WorkingCapitalYear workingCapitalYear;
    private Party party;

    public WorkingCapitalContext() {
	super();
	setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(final Integer year) {
	this.year = year;
	if (year != null) {
	    for (final WorkingCapitalYear workingCapitalYear : WorkingCapitalSystem.getInstanceForCurrentHost().getWorkingCapitalYearsSet()) {
		if (workingCapitalYear.getYear().intValue() == year.intValue()) {
		    this.workingCapitalYear = workingCapitalYear;
		}
	    }
	}
    }

    public WorkingCapitalYear getWorkingCapitalYear() {
	return workingCapitalYear;
    }

    public void setWorkingCapitalYear(final WorkingCapitalYear year) {
	this.workingCapitalYear = year;
    }

    public Party getParty() {
	return party;
    }

    public void setParty(final Party party) {
	this.party = party;
    }

    public SortedSet<WorkingCapitalProcess> getWorkingCapitalSearchByUnit() {
	final WorkingCapitalYear workingCapitalYear = getWorkingCapitalYear();
	final Party party = getParty();
	return workingCapitalYear == null || party == null ? new TreeSet<WorkingCapitalProcess>() : workingCapitalYear
		.getForParty(party);
    }

}
