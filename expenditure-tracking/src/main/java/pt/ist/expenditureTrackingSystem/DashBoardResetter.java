/*
 * @(#)DashBoardResetter.java
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
package pt.ist.expenditureTrackingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pt.ist.bennu.core._development.PropertiesManager;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.expenditureTrackingSystem.domain.DashBoard;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.Strings;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * 
 * @author João Neves
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class DashBoardResetter {

    public static void init() {
	Language.setDefaultLocale(new Locale("pt", "PT"));
	Language.setLocale(Language.getDefaultLocale());

	FenixWebFramework.initialize(PropertiesManager.getFenixFrameworkConfig());
    }

    public static void main(String[] args) {
	init();
	reset();
	System.out.println("Done.");
    }

    @Service
    private static void reset() {
	final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
	for (final Person person : MyOrg.getInstance().getPeopleFromExpenditureTackingSystemSet()) {
	    final DashBoard dashBoard = person.getDashBoard();
	    dashBoard.setExpenditureTrackingSystem(expenditureTrackingSystem);
	    addWidget1("widgetUnreadComments", dashBoard);
	    addWidget3("widgetActivateEmailNotification", dashBoard);
	}
    }

    private static void addWidget1(final String widgetName, final DashBoard dashBoard) {
	if (!hasWidget(widgetName, dashBoard)) {
	    final List<String> column1 = new ArrayList<String>();
	    for (final String string : dashBoard.getColumn1().getUnmodifiableList()) {
		column1.add(string);
	    }
	    column1.add(widgetName);
	    dashBoard.setColumn1(new Strings(column1));
	}
    }

    private static void addWidget2(final String widgetName, final DashBoard dashBoard) {
	if (!hasWidget(widgetName, dashBoard)) {
	    final List<String> column2 = new ArrayList<String>();
	    for (final String string : dashBoard.getColumn2().getUnmodifiableList()) {
		column2.add(string);
	    }
	    column2.add(widgetName);
	    dashBoard.setColumn2(new Strings(column2));
	}
    }

    private static void addWidget3(final String widgetName, final DashBoard dashBoard) {
	if (!hasWidget(widgetName, dashBoard)) {
	    final List<String> column3 = new ArrayList<String>();
	    for (final String string : dashBoard.getColumn3().getUnmodifiableList()) {
		column3.add(string);
	    }
	    column3.add(widgetName);
	    dashBoard.setColumn3(new Strings(column3));
	}
    }

    private static boolean hasWidget(String string, DashBoard dashBoard) {
	return dashBoard.getColumn1().contains(string) || dashBoard.getColumn2().contains(string)
		|| dashBoard.getColumn3().contains(string);
    }

}
