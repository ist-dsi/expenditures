/*
 * @(#)TakenProcessesWidget.java
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

import java.util.List;

import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.DashboardWidget;
import module.dashBoard.widgets.WidgetController;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureWidgetOptions;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.ExpenditureTrackingPanelPredicate;
import pt.ist.fenixframework.Atomic;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
@DashboardWidget(nameBundle = "resources.ExpenditureResources", nameKey = "process.title.takenProcesses",
        aditionPredicate = ExpenditureTrackingPanelPredicate.class)
public class TakenProcessesWidget extends WidgetController {

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

        List<PaymentProcess> takenProcesses = loggedPerson.getProcesses(PaymentProcess.class);
        takenProcesses = takenProcesses.subList(0, Math.min(options.getMaxListSize(), takenProcesses.size()));

        request.setAttribute("widgetOptions-" + widget.getExternalId(), options);
        request.setAttribute("takenProcesses", takenProcesses);
        request.setAttribute("person", loggedPerson);
    }

    @Override
    public void doEditOptions(WidgetRequest request) {
        DashBoardWidget widget = request.getWidget();
        request.setAttribute("edit-widgetOptions-" + widget.getExternalId(), getOrCreateOptions(widget));
    }

    @Override
    public String getWidgetDescription() {
        return BundleUtil.getString("resources/ExpenditureResources", "widget.description.TakenProcessesWidget");
    }
}
