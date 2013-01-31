package module.mission.domain.util;

import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum MissionStage implements IPresentableEnum {

	PROCESS_APPROVAL, FUND_ALLOCATION, PARTICIPATION_AUTHORIZATION, EXPENSE_AUTHORIZATION, PERSONEL_INFORMATION_PROCESSING,
	ARCHIVED;

	private static final String BUNDLE = "resources.MissionResources";
	private static final String KEY_PREFIX = "label.MissionStage.";
	private static final String KEY_PREFIX_DESCRIPTION = "label.MissionStage.description.";

	public String getLocalizedName() {
		final String key = KEY_PREFIX + name();
		return BundleUtil.getStringFromResourceBundle(BUNDLE, key);
	}

	public String getLocalizedDescription() {
		final String key = KEY_PREFIX_DESCRIPTION + name();
		return BundleUtil.getStringFromResourceBundle(BUNDLE, key);
	}

}
