package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class CreateRefundProcessBean implements Serializable {

    private boolean isForMission = false;
    private MissionProcess missionProcess;
    private Person requestor;
    private Person refundee;
    private Unit requestingUnit;
    private boolean requestUnitPayingUnit = true;
    private boolean externalPerson = false;
    private String refundeeName;
    private String refundeeFiscalCode;
    private boolean underCCP;

    public CreateRefundProcessBean(final Person requestor, boolean underCCP) {
	setRequestor(requestor);
	setUnderCCP(underCCP);
    }

    public Person getRequestor() {
	return requestor;
    }

    public void setRequestor(Person requestor) {
	this.requestor = requestor;
    }

    public Person getRefundee() {
	return refundee;
    }

    public void setRefundee(Person refundee) {
	this.refundee = refundee;
    }

    public Unit getRequestingUnit() {
	return requestingUnit;
    }

    public void setRequestingUnit(Unit requestingUnit) {
	this.requestingUnit = requestingUnit;
    }

    public boolean isRequestUnitPayingUnit() {
	return requestUnitPayingUnit;
    }

    public void setRequestUnitPayingUnit(boolean requestUnitPayingUnit) {
	this.requestUnitPayingUnit = requestUnitPayingUnit;
    }

    public boolean isExternalPerson() {
	return externalPerson;
    }

    public void setExternalPerson(boolean externalPerson) {
	this.externalPerson = externalPerson;
    }

    public String getRefundeeName() {
	return refundeeName;
    }

    public void setRefundeeName(String refundeeName) {
	this.refundeeName = refundeeName;
    }

    public String getRefundeeFiscalCode() {
	return refundeeFiscalCode;
    }

    public void setRefundeeFiscalCode(String refundeeFiscalCode) {
	this.refundeeFiscalCode = refundeeFiscalCode;
    }

    public boolean isForMission() {
	return isForMission;
    }

    public boolean getIsForMission() {
	return isForMission;
    }

    public void setIsForMission(boolean isForMission) {
	this.isForMission = isForMission;
    }

    public void setForMission(boolean isForMission) {
	this.isForMission = isForMission;
    }

    public MissionProcess getMissionProcess() {
	return missionProcess;
    }

    public void setMissionProcess(MissionProcess missionProcess) {
	this.missionProcess = missionProcess;
    }

    public void setUnderCCP(boolean underCCP) {
	this.underCCP = underCCP;
    }

    public boolean isUnderCCP() {
	return underCCP;
    }

}
