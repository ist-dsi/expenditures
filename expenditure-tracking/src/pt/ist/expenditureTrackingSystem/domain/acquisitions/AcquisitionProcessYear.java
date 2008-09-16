package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class AcquisitionProcessYear extends AcquisitionProcessYear_Base {

    private AcquisitionProcessYear() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setCounter(Integer.valueOf(0));
    }

    private AcquisitionProcessYear(final Integer year) {
	this();
	setYear(year);
    }

    public Integer nextAcquisitionProcessYearNumber() {
	return getAndUpdateNextCountNumber();
    }

    private Integer getAndUpdateNextCountNumber() {
	setCounter(getCounter().intValue() + 1);
	return getCounter();
    }

    public static AcquisitionProcessYear getAcquisitionProcessYearByYear(final Integer year) {
	AcquisitionProcessYear acquisitionProcessYear = getAcquisitionProcess(year);
	return acquisitionProcessYear != null ? acquisitionProcessYear : new AcquisitionProcessYear(year);
    }

    private static AcquisitionProcessYear getAcquisitionProcess(final Integer year) {
	for (AcquisitionProcessYear acquisitionProcessYear : ExpenditureTrackingSystem.getInstance()
		.getAcquisitionProcessYearsSet()) {
	    if (acquisitionProcessYear.getYear().equals(year)) {
		return acquisitionProcessYear;
	    }
	}
	return null;
    }

}
