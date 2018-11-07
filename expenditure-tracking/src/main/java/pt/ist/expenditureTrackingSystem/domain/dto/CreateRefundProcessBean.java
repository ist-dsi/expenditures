/*
 * @(#)CreateRefundProcessBean.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
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
    private boolean rapid;

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

    public boolean isRapid() {
        return rapid;
    }

    public void setRapid(boolean b) {
        this.rapid = b;
    }

}
