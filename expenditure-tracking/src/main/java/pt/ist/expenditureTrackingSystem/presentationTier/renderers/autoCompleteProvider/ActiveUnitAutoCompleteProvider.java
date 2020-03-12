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
package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;
import org.joda.time.LocalDate;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * @author Susana Fernandes
 * 
 */
public class ActiveUnitAutoCompleteProvider implements AutoCompleteProvider<Unit> {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final SortedSet<Unit> units = new TreeSet<>(Unit.COMPARATOR_BY_PRESENTATION_NAME);

        final String trimmedValue = value.trim();
        final String[] input = StringNormalizer.normalize(trimmedValue).split(" ");

        for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
            if (unit instanceof SubProject) {
                final String unitName = StringNormalizer.normalize(unit.getName());
                final String unitAcronym = StringNormalizer.normalize(unit.getUnit().getAcronym());
                if (hasMatch(input, unitName) || hasMatch(input, unitAcronym)) {
                    addUnit(units, unit);
                }
            } else if (unit instanceof CostCenter) {
                final CostCenter costCenter = (CostCenter) unit;
                final String unitCode = costCenter.getCostCenter();
                if (!StringUtils.isEmpty(unitCode) && trimmedValue.equalsIgnoreCase(unitCode)) {
                    addAllSubUnits(units, unit);
                }
            }
        }

        return units;
    }

    protected void addUnit(SortedSet<Unit> units, Unit unit) {
        if (unit instanceof SubProject && isActive(unit)) {
            units.add(unit);
        }
    }

    private boolean isActive(final Unit unit) {
        final module.organization.domain.Unit orgUnit = unit.getUnit();
        return orgUnit != null
                && orgUnit.hasActiveAncestry(ExpenditureTrackingSystem.getInstance().getOrganizationalAccountabilityType(),
                        new LocalDate());
    }

    private void addAllSubUnits(final SortedSet<Unit> units, final Unit unit) {
        for (final Unit subUnit : unit.getSubUnitsSet()) {
            addUnit(units, subUnit);
        }
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
