/*
 * @(#)AcquisitionItemClassification.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

/**
 * 
 * @author João Neves
 * 
 */
public enum AcquisitionItemClassification implements IPresentableEnum {

    GOODS,

    SERVICES;

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", I18N.getLocale());
        return resourceBundle.getString(this.getClass().getSimpleName() + "." + name());
    }

    public static AcquisitionItemClassification fromString(final String value) {
        for (final AcquisitionItemClassification classification : values()) {
            if (classification.name().equalsIgnoreCase(value) || classification.getLocalizedName().equalsIgnoreCase(value)) {
                return classification;
            }
        }
        return null;
    }

}
