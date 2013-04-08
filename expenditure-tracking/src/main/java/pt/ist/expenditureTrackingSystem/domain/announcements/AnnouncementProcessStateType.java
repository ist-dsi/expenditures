/*
 * @(#)AnnouncementProcessStateType.java
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
package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author Luis Cruz
 * 
 */
public enum AnnouncementProcessStateType {

    IN_GENESIS, SUBMITTED_FOR_APPROVAL, APPROVED, REJECTED, CANCELED, CLOSED;

    private AnnouncementProcessStateType() {
    }

    public String getLocalizedName() {
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
        return resourceBundle.getString(AnnouncementProcessStateType.class.getSimpleName() + "." + name());
    }

}
