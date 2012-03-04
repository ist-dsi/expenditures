/*
 * @(#)PersonNameAutoComplete.java
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
package pt.ist.expenditureTrackingSystem.presentationTier.renderers.autoCompleteProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import myorg.domain.MyOrg;
import myorg.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class PersonNameAutoComplete implements AutoCompleteProvider {

    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	List<Person> people = new ArrayList<Person>();
	String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
	for (Person person : MyOrg.getInstance().getPeopleFromExpenditureTackingSystemSet()) {
	    final String normalizedName = StringNormalizer.normalize(person.getName()).toLowerCase();
	    if (hasMatch(values, normalizedName)) {
		people.add(person);
	    }
	    if (people.size() >= maxCount) {
		break;
	    }
	}
	return people;
    }

    private boolean hasMatch(String[] input, String personNameParts) {
	for (final String namePart : input) {
	    if (personNameParts.indexOf(namePart) == -1) {
		return false;
	    }
	}
	return true;
    }
}
