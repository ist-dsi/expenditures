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

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionYear;
import module.mission.domain.NationalMissionProcess;
import module.workflow.domain.ProcessCounter;

import org.jfree.data.time.Month;
import org.joda.time.LocalDate;

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

			final int takenByUser = missionYear.getTaken().size() + (previousYear == null ? 0 : previousYear.getTaken().size());
			final int pendingApprovalCount =
					missionYear.getPendingAproval().size() + (previousYear == null ? 0 : previousYear.getPendingAproval().size());
			final int pendingAuthorizationCount =
					missionYear.getPendingAuthorization().size()
							+ (previousYear == null ? 0 : previousYear.getPendingAuthorization().size());
			final int pendingFundAllocationCount =
					missionYear.getPendingFundAllocation().size()
							+ (previousYear == null ? 0 : previousYear.getPendingFundAllocation().size());
			final int pendingProcessingCount =
					missionYear.getPendingProcessingPersonelInformation().size()
							+ (previousYear == null ? 0 : previousYear.getPendingProcessingPersonelInformation().size());

			return takenByUser + pendingApprovalCount + pendingAuthorizationCount + pendingFundAllocationCount
					+ pendingProcessingCount;
		} catch (final Throwable t) {
			t.printStackTrace();
			//throw new Error(t);
			return 0;
		}
	}

}
