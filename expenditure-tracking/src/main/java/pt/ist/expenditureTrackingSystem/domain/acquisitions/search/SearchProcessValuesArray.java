/*
 * @(#)SearchProcessValuesArray.java
 *
 * Copyright 2011 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.search;

import java.util.Arrays;
import java.util.Set;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class SearchProcessValuesArray {

    private final SearchProcessValues[] searchProcessValues;

    public SearchProcessValuesArray(final SearchProcessValues[] searchProcessValues) {
        this.searchProcessValues = getSearchProcessValues(searchProcessValues);
    }

    private SearchProcessValuesArray(final String[] strings) {
        searchProcessValues = new SearchProcessValues[strings.length];
        for (int i = 0; i < strings.length; i++) {
            final String string = strings[i];
            searchProcessValues[i] = SearchProcessValues.valueOf(string);
        }
    }

    public String exportAsString() {
        final StringBuilder builder = new StringBuilder();
        for (final SearchProcessValues s : searchProcessValues) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(s);
        }
        return builder.toString();
    }

    public static SearchProcessValuesArray importFromString(final String string) {
        return string == null ? null : new SearchProcessValuesArray(string.split(","));
    }

    public static SearchProcessValuesArray importFromString(final Set<SearchProcessValues> set) {
        return set == null ? null : new SearchProcessValuesArray(set.toArray(new SearchProcessValues[0]));
    }

    private static SearchProcessValues[] getSearchProcessValues(final SearchProcessValues[] searchProcessValues) {
        return searchProcessValues == null ? null : Arrays.copyOf(searchProcessValues, searchProcessValues.length);
    }

    public SearchProcessValues[] getSearchProcessValues() {
        return getSearchProcessValues(searchProcessValues);
    }

    public boolean contains(final SearchProcessValues values) {
        for (final SearchProcessValues s : searchProcessValues) {
            if (s == values) {
                return true;
            }
        }
        return false;
    }

}
