/*
 * @(#)UnitNameAutoCompleteProvider.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class UnitNameAutoCompleteProvider implements AutoCompleteProvider<Unit> {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<Unit> units = new ArrayList<Unit>();
        String[] input = StringNormalizer.normalize(value).split(" ");

        for (Unit unit : ExpenditureTrackingSystem.getInstance().getUnits()) {
            String unitName = StringNormalizer.normalize(unit.getName());
            if (hasMatch(input, unitName)) {
                units.add(unit);
            }
            if (units.size() >= maxCount) {
                break;
            }
        }
        return units;
    }

    private boolean hasMatch(String[] input, String unitNameParts) {
        for (final String namePart : input) {
            if (unitNameParts.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

}
