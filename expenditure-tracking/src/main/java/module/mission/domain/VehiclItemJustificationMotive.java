package module.mission.domain;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum VehiclItemJustificationMotive implements IPresentableEnum {

    FOR_SERVICE_BENEFIT, FOR_OWN_BENEFIT;

    private static final String BUNDLE = "resources.MissionResources";
    private static final String KEY_PREFIX = "label.mission.VehiclItemJustificationMotive.";

    @Override
    public String getLocalizedName() {
        final String key = KEY_PREFIX + name();
        return BundleUtil.getString(BUNDLE, key);
    }

}
