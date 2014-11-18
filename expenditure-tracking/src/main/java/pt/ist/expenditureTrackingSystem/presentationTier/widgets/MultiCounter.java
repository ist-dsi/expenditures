/* 
* @(#)MultiCounter.java 
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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class MultiCounter<T> {
    T countableObject;
    Map<String, Counter<T>> counters;
    final boolean holdElements;

    public MultiCounter(T countableObject, final boolean holdElements, String... counterNames) {
        this.countableObject = countableObject;
        this.holdElements = holdElements;
        counters = new HashMap<String, Counter<T>>();
        for (String name : counterNames) {
            counters.put(name, new Counter<T>(countableObject));
        }
    }

    public T getCountableObject() {
        return this.countableObject;
    }

    public Counter<T> getCounter(String counterName) {
        return counters.get(counterName);
    }

    public void increment(final String counterName, final Object object) {
        final Counter<T> counter = getCounter(counterName);
        if (holdElements) {
            counter.increment(object);
        } else {
            counter.increment();
        }
    }

}