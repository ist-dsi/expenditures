/*
 * @(#)MissionStageView.java
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
public class MissionStageView {

    private final MissionProcess missionProcess;

    public MissionStageView(final MissionProcess missionProcess) {
        this.missionProcess = missionProcess;
    }

    public SortedMap<MissionStage, MissionStateProgress> getMissionStateProgress() {
        final SortedMap<MissionStage, MissionStateProgress> result = new TreeMap<MissionStage, MissionStateProgress>();
        final Mission mission = missionProcess.getMission();

        result.put(MissionStage.PROCESS_APPROVAL, getApprovalState());

        if (mission.hasAnyMissionItems()) {
            if (mission.hasAnyVehicleItems()) {
                result.put(MissionStage.VEHICLE_APPROVAL, getVehicleApprovalState());
            }

            result.put(MissionStage.FUND_ALLOCATION, getFundAllocationState());
        }

        result.put(MissionStage.PARTICIPATION_AUTHORIZATION, getParticipationAuthorizationState());

        if (mission.hasAnyMissionItems()) {
            result.put(MissionStage.EXPENSE_AUTHORIZATION, getExpenseAuthorizationState());
        }

        result.put(MissionStage.PERSONEL_INFORMATION_PROCESSING, getPersonelInformationProcessingState());

        result.put(MissionStage.ARCHIVED, getArchivedState());

        return result;
    }

    protected MissionStateProgress getApprovalState() {
        return missionProcess.isApprovedByResponsible() ? MissionStateProgress.COMPLETED : getApprovalStateUnderConstruction();
    }

    protected MissionStateProgress getApprovalStateUnderConstruction() {
        return !missionProcess.isUnderConstruction() && !missionProcess.getIsCanceled() ? MissionStateProgress.PENDING : MissionStateProgress.IDLE;
    }

    protected MissionStateProgress getVehicleApprovalState() {
        if (getApprovalState() != MissionStateProgress.COMPLETED) {
            return MissionStateProgress.IDLE;
        }
        if (getParticipationAuthorizationState() == MissionStateProgress.COMPLETED) {
            return MissionStateProgress.COMPLETED;
        }
        if (missionProcess.getMission().areAllVehicleItemsAuthorized()) {
            return MissionStateProgress.COMPLETED;
        }
        return MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getFundAllocationState() {
        if (getApprovalState() != MissionStateProgress.COMPLETED) {
            return MissionStateProgress.IDLE;
        }

        if (getVehicleApprovalState() == MissionStateProgress.PENDING) {
            return MissionStateProgress.IDLE;
        }

        if (missionProcess.getIsCanceled().booleanValue()) {
            return missionProcess.hasAnyAllocatedFunds() || missionProcess.hasAnyAllocatedProjectFunds() ? MissionStateProgress.PENDING : MissionStateProgress.IDLE;
        }
        return missionProcess.hasAllAllocatedFunds() && missionProcess.hasAllCommitmentNumbers()
                && (!missionProcess.hasAnyProjectFinancer() || missionProcess.hasAllAllocatedProjectFunds()) ? MissionStateProgress.COMPLETED : MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getParticipationAuthorizationState() {
        return missionProcess.isApproved()
                && (!missionProcess.getMission().hasAnyFinancer() || (missionProcess.hasAllAllocatedFunds() && missionProcess
                        .hasAllCommitmentNumbers())) ? getParticipationAuthorizationStateForApproved() : MissionStateProgress.IDLE;
    }

    private MissionStateProgress getParticipationAuthorizationStateForApproved() {
        return missionProcess.isCanceled() ? MissionStateProgress.IDLE : (missionProcess.areAllParticipantsAuthorized() ? MissionStateProgress.COMPLETED : MissionStateProgress.PENDING);
    }

    protected MissionStateProgress getExpenseAuthorizationState() {
        return !missionProcess.isCanceled() && missionProcess.isApproved() && missionProcess.hasAllAllocatedFunds()
                && missionProcess.areAllParticipantsAuthorized() ? getExpenseAuthorizationStateCompletedOrUnderWay() : MissionStateProgress.IDLE;
    }

    private MissionStateProgress getExpenseAuthorizationStateCompletedOrUnderWay() {
        return missionProcess.isAuthorized() ? MissionStateProgress.COMPLETED : MissionStateProgress.PENDING;
    }

    protected MissionStateProgress getPersonelInformationProcessingState() {
        return missionProcess.areAllParticipantsAuthorized()
                && (missionProcess.isAuthorized() || missionProcess.hasNoItemsAndParticipantesAreAuthorized()) ? getPersonelInformationProcessingStateForAuthorizedParticipantes() : MissionStateProgress.IDLE;
    }

    protected MissionStateProgress getPersonelInformationProcessingStateForAuthorizedParticipantes() {
        return missionProcess.hasAnyCurrentQueues() ? MissionStateProgress.PENDING : (missionProcess.getIsCanceled()
                .booleanValue() ? MissionStateProgress.IDLE : MissionStateProgress.COMPLETED);
    }

    protected MissionStateProgress getArchivedState() {
        return missionProcess.isTerminated() ? getTerminatedArchivedState() : MissionStateProgress.IDLE;
    }

    protected MissionStateProgress getTerminatedArchivedState() {
        return missionProcess.isArchived() ? MissionStateProgress.COMPLETED : MissionStateProgress.PENDING;
    }

}
