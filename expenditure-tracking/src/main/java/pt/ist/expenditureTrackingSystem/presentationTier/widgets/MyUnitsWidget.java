/*
 * @(#)MyUnitsWidget.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.util.SortedSet;
import java.util.TreeSet;

import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.DashboardWidget;
import module.dashBoard.widgets.WidgetController;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureWidgetOptions;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.util.ExpenditureTrackingPanelPredicate;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author Luis Cruz
 * 
 */
@DashboardWidget(nameBundle = "resources.ExpenditureResources", nameKey = "label.title.myUnits",
        aditionPredicate = ExpenditureTrackingPanelPredicate.class)
public class MyUnitsWidget extends WidgetController {

    @Override
    @Atomic
    protected ExpenditureWidgetOptions getOrCreateOptions(DashBoardWidget widget) {
        ExpenditureWidgetOptions options = (ExpenditureWidgetOptions) widget.getOptions();
        if (options == null) {
            options = new ExpenditureWidgetOptions(10);
            widget.setOptions(options);
        }
        return options;
    }

    @Override
    public boolean isOptionsModeSupported() {
        return true;
    }

    @Override
    public void doView(WidgetRequest request) {
        DashBoardWidget widget = request.getWidget();
        ExpenditureWidgetOptions options = getOrCreateOptions(widget);
        Person loggedPerson = Person.getLoggedPerson();

        final SortedSet<Unit> units = new TreeSet<Unit>(Unit.COMPARATOR_BY_PRESENTATION_NAME);
        for (final Authorization authorization : loggedPerson.getAuthorizationsSet()) {
            if (authorization.isValid()) {
                final Unit unit = authorization.getUnit();
                if (unit.isActive()) {
                    units.add(unit);
                }
            }
        }

        request.setAttribute("widgetOptions-" + widget.getExternalId(), options);
        request.setAttribute("units", units);
        request.setAttribute("person", loggedPerson);
    }

    @Override
    public void doEditOptions(WidgetRequest request) {
        DashBoardWidget widget = request.getWidget();
        request.setAttribute("edit-widgetOptions-" + widget.getExternalId(), getOrCreateOptions(widget));
    }

    @Override
    public String getWidgetDescription() {
        return BundleUtil.getString("resources/ExpenditureResources", "widget.description.MyUnitsWidget");
    }
}
