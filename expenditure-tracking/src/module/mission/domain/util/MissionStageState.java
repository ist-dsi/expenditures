package module.mission.domain.util;

import myorg.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum MissionStageState implements IPresentableEnum {

    NOT_YET_UNDER_WAY, UNDER_WAY, COMPLETED;

    private static final String BUNDLE = "resources.MissionResources";
    private static final String KEY_PREFIX = "label.MissionStageState.";

    public String getLocalizedName() {
	final String key = KEY_PREFIX + name();
	return BundleUtil.getStringFromResourceBundle(BUNDLE, key);
    }

}
