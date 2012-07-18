/*
 * @(#)SearchUsers.java
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
package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import pt.ist.bennu.core.domain.MyOrg;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.Search;

/**
 * 
 * @author Pedro Santos
 * @author Luis Cruz
 * 
 */
public class SearchUsers extends Search<Person> {

    private String username;
    private String name;
    private Person person;
    private RoleType roleType;

    protected class SearchResult extends SearchResultSet<Person> {

	public SearchResult(final Collection<? extends Person> c) {
	    super(c);
	}

	@Override
	protected boolean matchesSearchCriteria(final Person person) {
	    return matchCriteria(username, person.getUsername()) && matchCriteria(name, person.getName())
		    && matchCriteria(roleType, person);
	}

	private boolean matchCriteria(final RoleType roleType, final Person person) {
	    return roleType == null || person.hasRoleType(roleType);
	}

    }

    @Override
    public Set<Person> search() {
	final Person person = getPerson();
	if (person != null) {
	    final Set<Person> people = new HashSet<Person>();
	    people.add(person);
	    return people;
	}
	final Set<Person> people = username != null || name != null || roleType != null ? MyOrg.getInstance()
		.getPeopleFromExpenditureTackingSystemSet() : Collections.EMPTY_SET;
	return new SearchResult(people);
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public RoleType getRoleType() {
	return roleType;
    }

    public void setRoleType(RoleType roleType) {
	this.roleType = roleType;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(final Person person) {
	this.person = person;
    }

}
