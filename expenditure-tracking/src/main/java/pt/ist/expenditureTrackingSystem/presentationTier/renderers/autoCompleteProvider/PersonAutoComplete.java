/*
 * @(#)PersonAutoComplete.java
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;
import org.fenixedu.commons.StringNormalizer;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class PersonAutoComplete implements AutoCompleteProvider<Person> {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        Set<Person> people = new HashSet<Person>();
        String[] values = StringNormalizer.normalize(value).toLowerCase().split(" ");
        for (Person person : Bennu.getInstance().getPeopleFromExpenditureTackingSystemSet()) {
            final String name = person.getUser().getName();
            if (name != null) {
                final String normalizedName = StringNormalizer.normalize(name).toLowerCase();
                if (hasMatch(values, normalizedName)) {
                    people.add(person);
                }
                if (person.getUsername().indexOf(value) >= 0) {
                    people.add(person);
                }
                if (people.size() >= maxCount) {
                    break;
                }
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
