package module.internalrequest.domain.util;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

import module.internalrequest.domain.InternalRequestProcess;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum InternalRequestState implements IPresentableEnum {

    APPROVAL {
        @Override
        public InternalRequestStateProgress getStateProgress(InternalRequestProcess internalRequestProcess) {
            if (internalRequestProcess.getIsCancelled()) {
                return InternalRequestStateProgress.IDLE;
            }
            if (internalRequestProcess.getApproved()) {
                return InternalRequestStateProgress.COMPLETED;
            }
            if (!internalRequestProcess.getIsUnderConstruction()) {
                return InternalRequestStateProgress.PENDING;
            }
            return InternalRequestStateProgress.IDLE;
        }
    },

    BUDGETING {
        @Override
        public InternalRequestStateProgress getStateProgress(InternalRequestProcess internalRequestProcess) {
            if (internalRequestProcess.getIsCancelled()) {
                return InternalRequestStateProgress.IDLE;
            }
            if (internalRequestProcess.getHasBudget()) {
                return InternalRequestStateProgress.COMPLETED;
            }
            if (internalRequestProcess.getApproved()) {
                return InternalRequestStateProgress.PENDING;
            }
            return InternalRequestStateProgress.IDLE;
        }
    },

    AUTHORIZATION {
        @Override
        public InternalRequestStateProgress getStateProgress(InternalRequestProcess internalRequestProcess) {
            if (internalRequestProcess.getIsCancelled()) {
                return InternalRequestStateProgress.IDLE;
            }
            if (internalRequestProcess.getAuthorized()) {
                return InternalRequestStateProgress.COMPLETED;
            }
            if (internalRequestProcess.getHasBudget()) {
                return InternalRequestStateProgress.PENDING;
            }
            return InternalRequestStateProgress.IDLE;
        }
    },

    PROCESSING {
        @Override
        public InternalRequestStateProgress getStateProgress(InternalRequestProcess internalRequestProcess) {
            if (internalRequestProcess.getIsCancelled()) {
                return InternalRequestStateProgress.IDLE;
            }
            if (internalRequestProcess.getFinishedProcessing()) {
                return InternalRequestStateProgress.COMPLETED;
            }
            if (internalRequestProcess.getAuthorized()) {
                return InternalRequestStateProgress.PENDING;
            }
            return InternalRequestStateProgress.IDLE;
        }
    },

    DELIVERY {
        @Override
        public InternalRequestStateProgress getStateProgress(InternalRequestProcess internalRequestProcess) {
            if (internalRequestProcess.getIsCancelled()) {
                return InternalRequestStateProgress.IDLE;
            }
            if (internalRequestProcess.getHasBeenDelivered()) {
                return InternalRequestStateProgress.COMPLETED;
            }
            if (internalRequestProcess.getFinishedProcessing()) {
                return InternalRequestStateProgress.PENDING;
            }
            return InternalRequestStateProgress.IDLE;
        }
    },

    COST_IMPUTATION {
        @Override
        public InternalRequestStateProgress getStateProgress(InternalRequestProcess internalRequestProcess) {
            if (internalRequestProcess.getIsCancelled()) {
                return InternalRequestStateProgress.IDLE;
            }
            if (internalRequestProcess.getHasCostImputation()) {
                return InternalRequestStateProgress.COMPLETED;
            }
            if (internalRequestProcess.getFinishedProcessing()) {
                return InternalRequestStateProgress.PENDING;
            }
            return InternalRequestStateProgress.IDLE;
        }
    }
    ;

    private static final String BUNDLE = "resources.InternalRequestResources";
    private static final String KEY_PREFIX = "label.InternalRequestState.";
    private static final String KEY_PREFIX_DESCRIPTION = "label.InternalRequestState.description.";

    @Override
    public String getLocalizedName() {
        return this.getName().getContent();
    }

    public String getLocalizedDescription() {
        return this.getDescription().getContent();
    }

    public LocalizedString getName() {
        final String key = KEY_PREFIX + name();
        return BundleUtil.getLocalizedString(BUNDLE, key);
    }

    public LocalizedString getDescription() {
        final String key = KEY_PREFIX_DESCRIPTION + name();
        return BundleUtil.getLocalizedString(BUNDLE, key);
    }

    public boolean isCompleted(InternalRequestProcess internalRequestProcess) {
        return getStateProgress(internalRequestProcess) == InternalRequestStateProgress.COMPLETED;
    }

    public boolean isPending(InternalRequestProcess internalRequestProcess) {
        return getStateProgress(internalRequestProcess) == InternalRequestStateProgress.PENDING;
    }

    public boolean isIdle(InternalRequestProcess internalRequestProcess) {
        return getStateProgress(internalRequestProcess) == InternalRequestStateProgress.IDLE;
    }

    public abstract InternalRequestStateProgress getStateProgress(InternalRequestProcess internalRequestProcess);
}
