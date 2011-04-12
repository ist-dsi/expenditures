package module.mission.domain.util;

import java.io.Serializable;

import module.geography.domain.Country;
import module.mission.domain.ForeignMissionProcess;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import module.mission.domain.NationalMissionProcess;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import pt.ist.fenixWebFramework.services.Service;

public class MissionProcessCreationBean implements Serializable {

    private Country country ; //= MissionSystem.getInstance().getCountry();
    private String location;
    private DateTime daparture;
    private DateTime arrival;
    private String objective;
    private Boolean isCurrentUserAParticipant = Boolean.FALSE;
    private Boolean grantOwnerEquivalence = Boolean.FALSE;

    public MissionProcessCreationBean(final Boolean grantOwnerEquivalence) {
	this.grantOwnerEquivalence = grantOwnerEquivalence;

	final LocalDate today = new LocalDate().plusDays(16);
	final LocalTime defaultStart = new LocalTime(9, 0, 0);
	final LocalTime defaultEnd = new LocalTime(18, 0, 0);
	daparture = today.toDateTime(defaultStart);
	arrival = today.toDateTime(defaultEnd);
    }

    public Country getCountry() {
	return country;
    }

    public void setCountry(Country country) {
	this.country = country;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public DateTime getDaparture() {
	return daparture;
    }

    public void setDaparture(DateTime daparture) {
	this.daparture = daparture;
    }

    public DateTime getArrival() {
	return arrival;
    }

    public void setArrival(DateTime arrival) {
	this.arrival = arrival;
    }

    public String getObjective() {
	return objective;
    }

    public void setObjective(String objective) {
	this.objective = objective;
    }

    public Boolean getIsCurrentUserAParticipant() {
	return isCurrentUserAParticipant;
    }

    public void setIsCurrentUserAParticipant(Boolean isCurrentUserAParticipant) {
	this.isCurrentUserAParticipant = isCurrentUserAParticipant;
    }

    @Service
    public MissionProcess createNewMissionProcess() {
	return MissionSystem.getInstance().getCountry() == country ? new NationalMissionProcess(location,
		daparture, arrival, objective, isCurrentUserAParticipant, grantOwnerEquivalence)
		: new ForeignMissionProcess(country, location, daparture, arrival, objective,
			isCurrentUserAParticipant, grantOwnerEquivalence);
    }

    public String getCurrentUserName() {
	final User currentUser = UserView.getCurrentUser();
	return currentUser == null ? "" : currentUser.getPerson().getFirstAndLastName();
    }

}
