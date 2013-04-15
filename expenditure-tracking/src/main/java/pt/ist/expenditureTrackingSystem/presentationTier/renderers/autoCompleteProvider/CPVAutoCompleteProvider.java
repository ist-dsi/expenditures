/*
 * @(#)CPVAutoCompleteProvider.java
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

import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class CPVAutoCompleteProvider implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        List<CPVReference> result = new ArrayList<CPVReference>();

        String[] values = value.toLowerCase().split(" ");
        for (final CPVReference cpvCode : MyOrg.getInstance().getCPVReferencesSet()) {
            if (cpvCode.getCode().startsWith(value) || match(cpvCode.getDescription().toLowerCase(), values)) {
                result.add(cpvCode);
            }
            if (result.size() >= maxCount) {
                break;
            }
        }
        return result;
    }

    private boolean match(String description, String[] inputParts) {

        for (final String namePart : inputParts) {
            if (description.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

}
