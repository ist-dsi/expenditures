/*
 * @(#)PendingSimplifiedWidget.java
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
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.DashboardWidget;
import module.dashBoard.widgets.WidgetController;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.util.ExpenditureTrackingPanelPredicate;
import pt.ist.expenditureTrackingSystem.util.ProcessMapGenerator;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
@DashboardWidget(nameBundle = "resources.ExpenditureResources", nameKey = "title.widget.multipleSupplierConsultation",
        aditionPredicate = ExpenditureTrackingPanelPredicate.class)
public class PendingConsultationWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
        Person loggedPerson = Person.getLoggedPerson();
        Map<MultipleSupplierConsultationProcessState, MultiCounter<MultipleSupplierConsultationProcessState>> simplifiedMap =
                ProcessMapGenerator.generateConsultationMap(loggedPerson);
        List<Counter<MultipleSupplierConsultationProcessState>> counters =
                new ArrayList<Counter<MultipleSupplierConsultationProcessState>>();

        for (MultiCounter<MultipleSupplierConsultationProcessState> multiCounter : simplifiedMap.values()) {
            Counter<MultipleSupplierConsultationProcessState> defaultCounter =
                    ProcessMapGenerator.getDefaultCounter(multiCounter);
            if (defaultCounter != null) {
                counters.add(defaultCounter);
            }
        }

        Collections.sort(counters, new BeanComparator("countableObject"));
        request.setAttribute("consultationCounters", counters);
        request.setAttribute("person", loggedPerson);
    }

    @Override
    public String getWidgetDescription() {
        return BundleUtil.getString("resources/ExpenditureResources", "widget.description.PendingConsultationWidget");
    }
}
