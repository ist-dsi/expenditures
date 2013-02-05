/*
 * @(#)PersonalVehiclItem.java
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
package module.mission.domain;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class PersonalVehiclItem extends PersonalVehiclItem_Base {

    public PersonalVehiclItem() {
        super();
    }

    @Override
    protected MissionItem createNewVersionInstance(final MissionVersion missionVersion) {
        return new PersonalVehiclItem();
    }

    @Override
    protected void setNewVersionInformation(final MissionItem missionItem) {
        final PersonalVehiclItem personalVehiclItem = (PersonalVehiclItem) missionItem;
        super.setNewVersionInformation(personalVehiclItem);
        personalVehiclItem.setKms(getKms());
    }

    @Override
    public boolean isAvailableForEdit() {
        final MissionVersion missionVersion = getMissionVersion();
        final Mission mission = missionVersion.getMission();
        return super.isAvailableForEdit() || mission.isTerminatedWithChanges();
    }

    @Override
    protected boolean canAutoArchive() {
        return false;
    }

}
