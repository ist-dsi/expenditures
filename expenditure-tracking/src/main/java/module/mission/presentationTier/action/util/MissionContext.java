package module.mission.presentationTier.action.util;

import java.io.Serializable;
import java.util.Calendar;

import module.mission.domain.MissionSystem;
import module.mission.domain.MissionYear;

public class MissionContext implements Serializable {

	private Integer year;
	private MissionYear missionYear;

	public MissionContext() {
		super();
		setYear(Integer.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
		if (year != null) {
			for (final MissionYear missionYear : MissionSystem.getInstance().getMissionYearSet()) {
				if (missionYear.getYear().intValue() == year.intValue()) {
					this.missionYear = missionYear;
				}
			}
		}

		if (missionYear == null) {
			missionYear = MissionYear.getCurrentYear();
		}
	}

	public MissionYear getMissionYear() {
		return missionYear;
	}

	public void setMissionYear(MissionYear missionYear) {
		this.missionYear = missionYear;
	}

}
