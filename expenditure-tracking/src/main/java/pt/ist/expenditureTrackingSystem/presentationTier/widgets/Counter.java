/* 
* @(#)Counter.java 
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

import java.util.HashSet;
import java.util.Set;

public class Counter<T> {

    T countableObject;
    int counter;
    final Set<Object> objects = new HashSet<Object>();

    public Counter(T countableObject) {
        this.countableObject = countableObject;
        this.counter = 0;
    }

    public Counter(T countableObject, int startCounter) {
        this.countableObject = countableObject;
        this.counter = startCounter;
    }

    public void increment() {
        this.counter++;
    }

    public void increment(final Object object) {
        increment();
        objects.add(object);
    }

    public int getValue() {
        return this.counter;
    }

    public T getCountableObject() {
        return this.countableObject;
    }

    public Set<Object> getObjects() {
        return objects;
    }

}