package module.workingCapital.domain;

public class WorkingCapitalYear extends WorkingCapitalYear_Base {
    
    public WorkingCapitalYear() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public WorkingCapitalYear(final Integer year) {
        this();
        setYear(year);
    }

    public static WorkingCapitalYear findOrCreate(final Integer year) {
	for (final WorkingCapitalYear workingCapitalYear : WorkingCapitalSystem.getInstance().getWorkingCapitalYearsSet()) {
	    if (workingCapitalYear.getYear().intValue() == year.intValue()) {
		return workingCapitalYear;
	    }
	}
	return new WorkingCapitalYear(year);
    }
    
}
