/*
 * @(#)EnumCounter.java
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
package pt.ist.expenditureTrackingSystem.util;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class EnumCounter<E extends Enum<E>> {

    private int[] counters = null;

    private Class<? extends Enum> clazz = null;

    public EnumCounter() {
    }

    private void init(Class clazz) {
	this.clazz = clazz;
	final Enum[] enums = (Enum[]) clazz.getEnumConstants();
	counters = new int[enums.length];
    }

    public void count(final E e, int count) {
	if (counters == null) {
	    final Class clazz = e.getClass();
	    final Class enclosingClass = clazz.getEnclosingClass();
	    init(enclosingClass == null ? clazz : enclosingClass);
	}
	counters[e.ordinal()] += count;
    }

    public void count(final E e) {
	count(e, 1);
    }

    public SortedMap<E, Integer> getCounts() {
	final SortedMap<E, Integer> result = new TreeMap<E, Integer>();
	if (clazz != null) { 
	    for (final E e : (E[]) clazz.getEnumConstants()) {
		final int count = counters[e.ordinal()];
		result.put(e, Integer.valueOf(count));
	    }
    	}
	return result;
    }

}
