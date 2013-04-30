/*
 * @(#)NIFSupplierAutoCompleteProvider.java
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
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class NIFSupplierAutoCompleteProvider implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final List<Supplier> result = new ArrayList<Supplier>();
        final String[] input = value.split(" ");
        StringNormalizer.normalize(input);

        for (final Supplier supplier : MyOrg.getInstance().getSuppliersSet()) {
            if (supplier.getFiscalIdentificationCode().startsWith(value)) {
                addResult(result, supplier);
            } else {
                final String name = StringNormalizer.normalize(supplier.getName());
                if (hasMatch(input, name)) {
                    addResult(result, supplier);
                }
            }
            if (result.size() >= maxCount) {
                break;
            }
        }
        return result;
    }

    protected void addResult(final List<Supplier> result, final Supplier supplier) {
        result.add(supplier);
    }

    private boolean hasMatch(final String[] input, final String unitNameParts) {
        for (final String namePart : input) {
            if (unitNameParts.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

}
