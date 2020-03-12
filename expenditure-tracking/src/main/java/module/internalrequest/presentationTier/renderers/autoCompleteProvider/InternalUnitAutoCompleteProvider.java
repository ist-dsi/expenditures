/*
 * @(#)ActiveUnitAutoCompleteProvider.java
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
package module.internalrequest.presentationTier.renderers.autoCompleteProvider;

import module.internalrequest.domain.InternalRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider.ActiveUnitAutoCompleteProvider;

import java.util.SortedSet;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author Susana Fernandes
 * 
 */
public class InternalUnitAutoCompleteProvider extends ActiveUnitAutoCompleteProvider {
    @Override
    protected void addUnit(SortedSet<Unit> units, Unit unit) {
        if (InternalRequest.isUnitInternal(unit)) {
            super.addUnit(units, unit);
        }
    }
}
