/*
 * @(#)UnitAutoCompleteProvider.java
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

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Luis Cruz
 * @author Susana Fernandes
 * 
 */
public class UnitAutoCompleteProvider implements AutoCompleteProvider<Unit> {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String trimmedValue = value.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");

        return ExpenditureTrackingSystem.getInstance().getUnits().stream()

                .filter(u -> u instanceof CostCenter || u instanceof Project || u instanceof SubProject)

                .filter(u -> hasMatch(input, StringNormalizer.normalize(u.getPresentationName())))

                .filter(u -> !hasSubProjects(u))

                .sorted(Unit.COMPARATOR_BY_PRESENTATION_NAME)

                .collect(Collectors.toList());
    }

    private boolean hasMatch(final String[] input, final String unitNameParts) {
        for (final String namePart : input) {
            if (unitNameParts.indexOf(namePart) == -1) {
                return false;
            }
        }
        return true;
    }

    private boolean hasSubProjects(final Unit unit) {
        if (unit instanceof Project) {
            final Project project = (Project) unit;
            if (project.getSubUnitsSet().size() > 0) {
                return true;
            }
        }
        return false;
    }

}
