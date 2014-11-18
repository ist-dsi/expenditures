/*
 * @(#)ProcessMapGenerator.java
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
package pt.ist.expenditureTrackingSystem.util;

import java.util.HashMap;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.Counter;
import pt.ist.expenditureTrackingSystem.presentationTier.widgets.MultiCounter;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
public class ProcessMapGenerator {

    public static final String DEFAULT_COUNTER = "default";
    public static final String PRIORITY_COUNTER = "priority";

    public static <T> Counter<T> getDefaultCounter(MultiCounter<T> multiCounter) {
        return multiCounter.getCounter(DEFAULT_COUNTER);
    }

    public static <T> Counter<T> getPriorityCounter(MultiCounter<T> multiCounter) {
        return multiCounter.getCounter(PRIORITY_COUNTER);
    }

    public static Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> generateAcquisitionMap(
            final Person person, final boolean holdElements) {
        Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> map =
                new HashMap<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>>();

        for (SimplifiedProcedureProcess process : GenericProcess.getProcessesForPerson(SimplifiedProcedureProcess.class, person,
                null, true)) {

            AcquisitionProcessStateType type = process.getAcquisitionProcessStateType();
            MultiCounter<AcquisitionProcessStateType> counter = map.get(type);
            if (counter == null) {
                counter =
                        new MultiCounter<AcquisitionProcessStateType>(type, holdElements, new String[] { DEFAULT_COUNTER,
                                PRIORITY_COUNTER });
                map.put(type, counter);
            }
            counter.increment(DEFAULT_COUNTER, process);
            if (process.isPriorityProcess()) {
                counter.increment(PRIORITY_COUNTER, process);
            }
        }
        return map;
    }

    public static Map<AcquisitionProcessStateType, MultiCounter<AcquisitionProcessStateType>> generateAcquisitionMap(Person person) {
        return generateAcquisitionMap(person, false);
    }

    public static Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> generateRefundMap(final Person person,
            final boolean holdElements) {
        Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> map =
                new HashMap<RefundProcessStateType, MultiCounter<RefundProcessStateType>>();

        for (RefundProcess process : GenericProcess.getProcessesForPerson(RefundProcess.class, person, null, true)) {

            RefundProcessStateType type = process.getProcessState().getRefundProcessStateType();
            MultiCounter<RefundProcessStateType> counter = map.get(type);
            if (counter == null) {
                counter =
                        new MultiCounter<RefundProcessStateType>(type, holdElements, new String[] { DEFAULT_COUNTER,
                                PRIORITY_COUNTER });
                map.put(type, counter);
            }
            counter.increment(DEFAULT_COUNTER, process);
            if (process.isPriorityProcess()) {
                counter.increment(PRIORITY_COUNTER, process);
            }
        }
        return map;
    }

    public static Map<RefundProcessStateType, MultiCounter<RefundProcessStateType>> generateRefundMap(final Person person) {
        return generateRefundMap(person, false);
    }

}
