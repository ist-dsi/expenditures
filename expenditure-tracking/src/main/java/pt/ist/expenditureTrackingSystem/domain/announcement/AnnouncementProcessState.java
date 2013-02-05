/*
 * @(#)AnnouncementProcessState.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.announcement;

import pt.ist.bennu.core.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcess;
import pt.ist.expenditureTrackingSystem.domain.announcements.AnnouncementProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class AnnouncementProcessState extends AnnouncementProcessState_Base {

    protected AnnouncementProcessState() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public AnnouncementProcessState(final AnnouncementProcess process, final AnnouncementProcessStateType processStateType) {
        this();
        final Person person = getPerson();
        checkArguments(process, processStateType, person);
        super.initFields(process, person);
        setAnnouncementProcessStateType(processStateType);
    }

    public AnnouncementProcessState(final AnnouncementProcess process, final AnnouncementProcessStateType processStateType,
            String justification) {
        this(process, processStateType);
        setJustification(justification);
    }

    protected Person getPerson() {
        return Person.getLoggedPerson();
    }

    private void checkArguments(AnnouncementProcess announcementProcess,
            AnnouncementProcessStateType announcementProcessStateType, Person person) {
        if (announcementProcessStateType == null) {
            throw new DomainException("error.wrong.AnnouncementProcessState.arguments");
        }
        super.checkArguments(announcementProcess, person);
    }

    public String getLocalizedName() {
        return getAnnouncementProcessStateType().getLocalizedName();
    }

    @Override
    public boolean isInFinalStage() {
        return true;
    }

}
