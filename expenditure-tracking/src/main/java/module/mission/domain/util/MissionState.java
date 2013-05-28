package module.mission.domain.util;

import module.mission.domain.MissionProcess;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum MissionState implements IPresentableEnum {

    PROCESS_APPROVAL {
        @Override
        public MissionStateProgress getStateProgress(MissionProcess missionProcess) {
            if (missionProcess.isApprovedByResponsible()) {
                return MissionStateProgress.COMPLETED;
            }
            if (missionProcess.isUnderConstruction() || missionProcess.isCanceled()) {
                return MissionStateProgress.IDLE;
            }
            return MissionStateProgress.PENDING;
        }
    },

    VEHICLE_APPROVAL {
        @Override
        public boolean isValidForProcess(MissionProcess missionProcess) {
            return missionProcess.hasAnyVehicleItems();
        }

        @Override
        public MissionStateProgress getStateProgress(MissionProcess missionProcess) {
            // depends on ProcessApproval
            if (PROCESS_APPROVAL.getStateProgress(missionProcess) != MissionStateProgress.COMPLETED) {
                return MissionStateProgress.IDLE;
            }

            // is no longer needed after ParticipationAuthorization
            if (PARTICIPATION_AUTHORIZATION.getStateProgress(missionProcess) == MissionStateProgress.COMPLETED) {
                return MissionStateProgress.COMPLETED;
            }

            if (missionProcess.getMission().areAllVehicleItemsAuthorized()) {
                return MissionStateProgress.COMPLETED;
            }
            return MissionStateProgress.PENDING;
        }
    },

    FUND_ALLOCATION {
        @Override
        public boolean isValidForProcess(MissionProcess missionProcess) {
            return missionProcess.hasAnyMissionItems();
        }

        @Override
        public MissionStateProgress getStateProgress(MissionProcess missionProcess) {
            // this should not be here? (dependency on VehicleApproval should be enough)
            if (PROCESS_APPROVAL.getStateProgress(missionProcess) != MissionStateProgress.COMPLETED) {
                return MissionStateProgress.IDLE;
            }

            // depends on VehicleApproval
            if (VEHICLE_APPROVAL.getStateProgress(missionProcess) == MissionStateProgress.PENDING) {
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
    },

    PARTICIPATION_AUTHORIZATION {
        @Override
        public MissionStateProgress getStateProgress(MissionProcess missionProcess) {
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
    },

    EXPENSE_AUTHORIZATION {
        @Override
        public boolean isValidForProcess(MissionProcess missionProcess) {
            return missionProcess.hasAnyMissionItems();
        }

        @Override
        public MissionStateProgress getStateProgress(MissionProcess missionProcess) {
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
    },

    PERSONEL_INFORMATION_PROCESSING {
        @Override
        public MissionStateProgress getStateProgress(MissionProcess missionProcess) {
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
    },

    ARCHIVED {
        @Override
        public MissionStateProgress getStateProgress(MissionProcess missionProcess) {
            if (!missionProcess.isTerminated()) {
                return MissionStateProgress.IDLE;
            }
            if (missionProcess.isArchived()) {
                return MissionStateProgress.COMPLETED;
            }
            return MissionStateProgress.PENDING;
        }
    };

    private static final String BUNDLE = "resources.MissionResources";
    private static final String KEY_PREFIX = "label.MissionState.";
    private static final String KEY_PREFIX_DESCRIPTION = "label.MissionState.description.";

    @Override
    public String getLocalizedName() {
        final String key = KEY_PREFIX + name();
        return BundleUtil.getStringFromResourceBundle(BUNDLE, key);
    }

    public String getLocalizedDescription() {
        final String key = KEY_PREFIX_DESCRIPTION + name();
        return BundleUtil.getStringFromResourceBundle(BUNDLE, key);
    }

    public boolean isValidForProcess(MissionProcess missionProcess) {
        return true;
    }

    public abstract MissionStateProgress getStateProgress(MissionProcess missionProcess);
}
