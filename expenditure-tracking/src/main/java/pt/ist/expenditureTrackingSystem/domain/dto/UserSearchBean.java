/*
 * @(#)UserSearchBean.java
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
package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Pedro Santos
 * 
 */
public class UserSearchBean implements Serializable {

    private SavedSearch selectedSearch;
    private Person user;

    public UserSearchBean(Person person) {
	setUser(person);
	setSelectedSearch(null);
    }

    public SavedSearch getSelectedSearch() {
	return selectedSearch;
    }

    public void setSelectedSearch(SavedSearch selectedSearch) {
	this.selectedSearch = selectedSearch;
    }

    public Person getUser() {
	return user;
    }

    public void setUser(Person user) {
	this.user = user;
    }

}
