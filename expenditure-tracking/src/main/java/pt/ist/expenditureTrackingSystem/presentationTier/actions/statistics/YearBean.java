package pt.ist.expenditureTrackingSystem.presentationTier.actions.statistics;

import java.io.Serializable;

import org.joda.time.LocalDate;

public class YearBean implements Serializable {

	private Integer year = new Integer(new LocalDate().getYear());

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
