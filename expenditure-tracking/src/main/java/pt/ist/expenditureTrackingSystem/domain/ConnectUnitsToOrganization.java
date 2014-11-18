/*
 * @(#)ConnectUnitsToOrganization.java
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
package pt.ist.expenditureTrackingSystem.domain;

import java.util.Collection;

import module.organization.domain.PartyType;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.CronTask;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class ConnectUnitsToOrganization extends CronTask {

    @Override
    public void runTask() throws Exception {
        for (final Unit unit : ExpenditureTrackingSystem.getInstance().getUnitsSet()) {
            connect(unit);
        }
    }

    @Override
    public String getLocalizedName() {
        return getClass().getName();
    }

    private void connect(final Unit unit) {
        if (!unit.hasUnit()) {
            final module.organization.domain.Unit organizationUnit = findUnit(unit);
            if (organizationUnit != null) {
                final PartyType partyType = getPartyType(unit);
                organizationUnit.addPartyTypes(partyType);
                unit.setUnit(organizationUnit);
            }
        }
    }

    private PartyType getPartyType(final Unit unit) {
        if (unit instanceof SubProject) {
            return ExpenditureTrackingSystem.getInstance().getSubProjectPartyType();
        } else if (unit instanceof Project) {
            return ExpenditureTrackingSystem.getInstance().getProjectPartyType();
        } else if (unit instanceof CostCenter) {
            return ExpenditureTrackingSystem.getInstance().getCostCenterPartyType();
        } else {
            return ExpenditureTrackingSystem.getInstance().getUnitPartyType();
        }
    }

    private module.organization.domain.Unit findUnit(final Unit unit) {
        if (unit instanceof SubProject) {
            return null;
        } else if (unit instanceof Project) {
            return null;
        } else if (unit instanceof CostCenter) {
            final module.organization.domain.Unit organizationUnit = findUnit(unit, Bennu.getInstance().getTopUnitsSet());
            if (organizationUnit != null) {
                return organizationUnit;
            }
        } else {
            final module.organization.domain.Unit organizationUnit = findUnit(unit, Bennu.getInstance().getTopUnitsSet());
            if (organizationUnit != null) {
                return organizationUnit;
            }
        }
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to find org unit for: ");
        stringBuilder.append(unit.getClass().getSimpleName());
        stringBuilder.append(" ");
        stringBuilder.append(unit.getPresentationName());
        stringBuilder.append(".");
        taskLog(stringBuilder.toString());
        return null;
    }

    private module.organization.domain.Unit findUnit(final Unit unit, final Collection<module.organization.domain.Unit> units) {
        for (final module.organization.domain.Unit organizationUnit : units) {
            if (match(organizationUnit.getPartyName(), unit.getName())) {
                taskLog("Matched: " + unit.getPresentationName() + " with: " + organizationUnit.getPresentationName());
                return organizationUnit;
            }
            final module.organization.domain.Unit result = findUnit(unit, organizationUnit.getChildUnits());
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private boolean match(final LocalizedString multiLanguageString, final String string) {
        return multiLanguageString.getContent().equalsIgnoreCase(string);
    }

}
