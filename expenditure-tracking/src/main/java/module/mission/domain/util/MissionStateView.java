/*
 * @(#)MissionStateView.java
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

import java.util.SortedMap;
import java.util.TreeMap;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;

/**
 * 
 * @author João Neves
 * @author Luis Cruz
 * 
 */
public class MissionStateView {

    private final MissionProcess missionProcess;

    public MissionStateView(final MissionProcess missionProcess) {
        this.missionProcess = missionProcess;
    }

    public SortedMap<MissionState, MissionStateProgress> getMissionStateProgress() {
        final SortedMap<MissionState, MissionStateProgress> stateProgress = new TreeMap<MissionState, MissionStateProgress>();
        final Mission mission = missionProcess.getMission();

        stateProgress.put(MissionState.PROCESS_APPROVAL, getProcessApprovalStateProgress());

        if (mission.hasAnyMissionItems()) {
            if (mission.hasAnyVehicleItems()) {
                stateProgress.put(MissionState.VEHICLE_APPROVAL, getVehicleApprovalStateProgress());
            }

            stateProgress.put(MissionState.FUND_ALLOCATION, getFundAllocationStateProgress());
        }

        stateProgress.put(MissionState.PARTICIPATION_AUTHORIZATION, getParticipationAuthorizationStateProgress());

        if (mission.hasAnyMissionItems()) {
            stateProgress.put(MissionState.EXPENSE_AUTHORIZATION, getExpenseAuthorizationStateProgress());
        }

        stateProgress.put(MissionState.PERSONEL_INFORMATION_PROCESSING, getPersonelInformationProcessingStateProgress());

        stateProgress.put(MissionState.ARCHIVED, getArchivedStateProgress());

        return stateProgress;
    }

    protected MissionStateProgress getProcessApprovalStateProgress() {
        if (missionProcess.isApprovedByResponsible()) {
            return MissionStateProgress.COMPLETED;
        }
        if (missionProcess.isUnderConstruction() || missionProcess.isCanceled()) {
            return MissionStateProgress.IDLE;
        }
        return MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getVehicleApprovalStateProgress() {
        // depends on ProcessApproval
        if (getProcessApprovalStateProgress() != MissionStateProgress.COMPLETED) {
            return MissionStateProgress.IDLE;
        }

        // is no longer needed after ParticipationAuthorization
        if (getParticipationAuthorizationStateProgress() == MissionStateProgress.COMPLETED) {
            return MissionStateProgress.COMPLETED;
        }

        if (missionProcess.getMission().areAllVehicleItemsAuthorized()) {
            return MissionStateProgress.COMPLETED;
        }
        return MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getFundAllocationStateProgress() {
        // this should not be here? (dependency on VehicleApproval should be enough)
        if (getProcessApprovalStateProgress() != MissionStateProgress.COMPLETED) {
            return MissionStateProgress.IDLE;
        }

        // depends on VehicleApproval
        if (getVehicleApprovalStateProgress() == MissionStateProgress.PENDING) {
            return MissionStateProgress.IDLE;
        }

        // fund allocations of canceled mission processes must be removed
        if (missionProcess.isCanceled()) {
            if (missionProcess.hasAnyAllocatedFunds() || missionProcess.hasAnyAllocatedProjectFunds()) {
                return MissionStateProgress.PENDING;
            }
            return MissionStateProgress.IDLE;
        }

        if (missionProcess.hasAllAllocatedFunds() && missionProcess.hasAllCommitmentNumbers()
                && (!missionProcess.hasAnyProjectFinancer() || missionProcess.hasAllAllocatedProjectFunds())) {
            return MissionStateProgress.COMPLETED;
        }

        return MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getParticipationAuthorizationStateProgress() {
        // this should not be here? (dependency on FundAllocation should be enough)
        if (!missionProcess.isApproved()) {
            return MissionStateProgress.IDLE;
        }

        // depends on FundAllocation ???
        if (missionProcess.hasAnyMissionItems()
                && (!missionProcess.hasAllAllocatedFunds() || !missionProcess.hasAllCommitmentNumbers())) {
            return MissionStateProgress.IDLE;
        }

        // this should not be here? (dependency on FundAllocation should be enough)
        if (missionProcess.isCanceled()) {
            return MissionStateProgress.IDLE;
        }

        if (missionProcess.areAllParticipantsAuthorized()) {
            return MissionStateProgress.COMPLETED;
        }
        return MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getExpenseAuthorizationStateProgress() {
        // this should not be here? (dependency on ParticipationAuthorization should be enough)
        if (missionProcess.isCanceled()) {
            return MissionStateProgress.IDLE;
        }
        // this should not be here? (dependency on ParticipationAuthorization should be enough)
        if (!missionProcess.isApproved()) {
            return MissionStateProgress.IDLE;
        }
        // this should not be here? (dependency on ParticipationAuthorization should be enough)
        if (!missionProcess.hasAllAllocatedFunds()) {
            return MissionStateProgress.IDLE;
        }

        // depends on ParticipationAuthorization ???
        if (!missionProcess.areAllParticipantsAuthorized()) {
            return MissionStateProgress.IDLE;
        }

        if (missionProcess.isAuthorized()) {
            return MissionStateProgress.COMPLETED;
        }

        return MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getPersonelInformationProcessingStateProgress() {
        // this should not be here? (dependency on ExpenseAuthorization should be enough)
        if (!missionProcess.areAllParticipantsAuthorized()) {
            return MissionStateProgress.IDLE;
        }

        // depends on ExpenseAuthorization ??? (second if clause should be removed, redundant with ExpenseAuthorization)
        if (!missionProcess.isAuthorized() && !missionProcess.hasNoItemsAndParticipantesAreAuthorized()) {
            return MissionStateProgress.IDLE;
        }

        if (!missionProcess.getCurrentQueuesSet().isEmpty()) {
            return MissionStateProgress.PENDING;
        }

        // this should not be here? (dependency on ExpenseAuthorization should be enough)
        if (missionProcess.isCanceled()) {
            return MissionStateProgress.IDLE;
        }

        return MissionStateProgress.COMPLETED;
    }

    protected MissionStateProgress getArchivedStateProgress() {
        if (!missionProcess.isTerminated()) {
            return MissionStateProgress.IDLE;
        }
        if (missionProcess.isArchived()) {
            return MissionStateProgress.COMPLETED;
        }
        return MissionStateProgress.PENDING;
    }
}
