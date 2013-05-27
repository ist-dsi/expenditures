package module.mission.domain.util;

import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum MissionStateProgress implements IPresentableEnum {

    IDLE, PENDING, COMPLETED;

    private static final String BUNDLE = "resources.MissionResources";
    private static final String KEY_PREFIX = "label.MissionStateProgress.";

    @Override
    public String getLocalizedName() {
        final String key = KEY_PREFIX + name();
        return BundleUtil.getStringFromResourceBundle(BUNDLE, key);
    }

}
