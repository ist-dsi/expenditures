package module.mission.domain.util;

import java.io.Serializable;

import module.mission.domain.DailyPersonelExpenseTable;

import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class DailyPersonelExpenseTableBean implements Serializable {

	private LocalDate aplicableSince;
	private Class aplicableToMissionType;

	public DailyPersonelExpenseTableBean() {
	}

	public LocalDate getAplicableSince() {
		return aplicableSince;
	}

	public void setAplicableSince(LocalDate aplicableSince) {
		this.aplicableSince = aplicableSince;
	}

	public Class getAplicableToMissionType() {
		return aplicableToMissionType;
	}

	public void setAplicableToMissionType(Class aplicableToMissionType) {
		this.aplicableToMissionType = aplicableToMissionType;
	}

	@Service
	public DailyPersonelExpenseTable createDailyPersonelExpenseTable() {
		return new DailyPersonelExpenseTable(aplicableSince, aplicableToMissionType);
	}

}
