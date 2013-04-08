/*
 * @(#)UnreadCommentsWidget.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import module.dashBoard.domain.DashBoardWidget;
import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureWidgetOptions;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "process.title.processesWithUnreadComments")
/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class UnreadCommentsWidget extends WidgetController {

    @Override
    @Service
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

        List<PaymentProcess> processesWithUnreadComments = new ArrayList<PaymentProcess>();
        processesWithUnreadComments.addAll(loggedPerson.getProcessesWhereUserWasInvolvedWithUnreadComments(PaymentProcess.class));
        Collections.sort(processesWithUnreadComments, new ReverseComparator(new BeanComparator("acquisitionProcessId")));
        int processCount = processesWithUnreadComments.size();
        processesWithUnreadComments = processesWithUnreadComments.subList(0, Math.min(options.getMaxListSize(), processCount));

        request.setAttribute("widgetOptions-" + widget.getExternalId(), options);
        request.setAttribute("processesWithUnreadComments", processesWithUnreadComments);
    }

    @Override
    public void doEditOptions(WidgetRequest request) {
        DashBoardWidget widget = request.getWidget();
        request.setAttribute("edit-widgetOptions-" + widget.getExternalId(), getOrCreateOptions(widget));
    }

    @Override
    public String getWidgetDescription() {
        return BundleUtil
                .getStringFromResourceBundle("resources/ExpenditureResources", "widget.description.UnreadCommentsWidget");
    }
}
