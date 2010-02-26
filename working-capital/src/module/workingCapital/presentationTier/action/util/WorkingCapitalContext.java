package module.workingCapital.presentationTier.action.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.SortedSet;
import java.util.TreeSet;

import module.organization.domain.Unit;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalSystem;
import module.workingCapital.domain.WorkingCapitalYear;

public class WorkingCapitalContext implements Serializable {

    private Integer year;
    private WorkingCapitalYear workingCapitalYear;
    private Unit unit;

    public WorkingCapitalContext() {
	super();
	setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
	if (year != null) {
	    for (final WorkingCapitalYear workingCapitalYear : WorkingCapitalSystem.getInstance().getWorkingCapitalYearsSet()) {
		if (workingCapitalYear.getYear().intValue() == year.intValue()) {
		    this.workingCapitalYear = workingCapitalYear;
		}
	    }
	}
    }

    public WorkingCapitalYear getWorkingCapitalYear() {
	return workingCapitalYear;
    }

    public void setWorkingCapitalYear(WorkingCapitalYear year) {
	this.workingCapitalYear = year;
    }

    public Unit getUnit() {
	return unit;
    }

    public void setUnit(Unit unit) {
	this.unit = unit;
    }

    public SortedSet<WorkingCapitalProcess> getWorkingCapitalSearchByUnit() {
	final WorkingCapitalYear workingCapitalYear = getWorkingCapitalYear();
	final Unit unit = getUnit();
	return workingCapitalYear == null || unit == null ? new TreeSet<WorkingCapitalProcess>() : workingCapitalYear
		.getForUnit(unit);
    }

}
