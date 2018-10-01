/*
 * @(#)MissionPendingProcessCounter.java
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
package module.mission.domain.util;

import org.jfree.data.time.Month;
import org.joda.time.LocalDate;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionYear;
import module.mission.domain.NationalMissionProcess;
import module.workflow.domain.ProcessCounter;

/**
 * 
 * @author Luis Cruz
 * 
 */
public class MissionPendingProcessCounter extends ProcessCounter {

    public MissionPendingProcessCounter() {
        super(MissionProcess.class);
    }

    @Override
    public Class getProcessClassForForwarding() {
        return NationalMissionProcess.class;
    }

    @Override
    public int getCount() {
        try {
            final MissionYear missionYear = MissionYear.getCurrentYear();
            final LocalDate today = new LocalDate();
            final MissionYear previousYear =
                    today.getMonthOfYear() == Month.JANUARY ? MissionYear.findOrCreateMissionYear(today.getYear() - 1) : null;

            final int takenByUser =
                    (int) (missionYear.getTakenStream().count() + (previousYear == null ? 0 : previousYear.getTakenStream().count()));
            final long pendingApprovalCount =
                    missionYear.getPendingAproval().count() + (previousYear == null ? 0 : previousYear.getPendingAproval().count());
            final long pendingAuthorizationCount =
                    missionYear.getPendingAuthorization().count()
                            + (previousYear == null ? 0 : previousYear.getPendingAuthorization().count());
            final long pendingFundAllocationCount =
                    missionYear.getPendingFundAllocation().count()
                            + (previousYear == null ? 0 : previousYear.getPendingFundAllocation().count());
            final long pendingProcessingCount =
                    missionYear.getPendingProcessingPersonelInformation().count()
                            + (previousYear == null ? 0 : previousYear.getPendingProcessingPersonelInformation().count());

            return (int) (takenByUser + pendingApprovalCount + pendingAuthorizationCount + pendingFundAllocationCount
                    + pendingProcessingCount);
        } catch (final Throwable t) {
            t.printStackTrace();
            //throw new Error(t);
            return 0;
        }
    }

}
