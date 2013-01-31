package module.mission.domain;

import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum VehiclItemJustificationMotive implements IPresentableEnum {

	FOR_SERVICE_BENEFIT, FOR_OWN_BENEFIT;

	private static final String BUNDLE = "resources.MissionResources";
	private static final String KEY_PREFIX = "label.mission.VehiclItemJustificationMotive.";

	public String getLocalizedName() {
		final String key = KEY_PREFIX + name();
		return BundleUtil.getStringFromResourceBundle(BUNDLE, key);
	}

}
