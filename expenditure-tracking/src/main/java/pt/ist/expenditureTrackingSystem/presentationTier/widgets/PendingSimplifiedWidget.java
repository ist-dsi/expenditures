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

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;
import pt.ist.bennu.core.util.Counter;
import pt.ist.bennu.core.util.MultiCounter;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.util.ProcessMapGenerator;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "title.widget.simplifiedProcedure")
/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * 
 */
public class PendingSimplifiedWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	Person loggedPerson = Person.getLoggedPerson();
	Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> simplifiedMap = ProcessMapGenerator
		.generateAcquisitionMap(loggedPerson);
	List<Counter<AcquisitionProcessStateType>> counters = new ArrayList<Counter<AcquisitionProcessStateType>>();

	for (MultiCounter<AcquisitionProcessStateType> multiCounter : simplifiedMap.values()) {
	    Counter<AcquisitionProcessStateType> defaultCounter = ProcessMapGenerator.getDefaultCounter(multiCounter);
	    if (defaultCounter != null) {
		counters.add(defaultCounter);
	    }
	}

	Collections.sort(counters, new BeanComparator("countableObject"));
	request.setAttribute("simplifiedCounters", counters);
	request.setAttribute("person", loggedPerson);
    }

    @Override
    public String getWidgetDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/ExpenditureResources", "widget.description.PendingSimplifiedWidget");
    }
}
