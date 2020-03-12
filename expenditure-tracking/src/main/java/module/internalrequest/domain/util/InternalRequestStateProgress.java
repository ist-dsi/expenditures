package module.internalrequest.domain.util;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum InternalRequestStateProgress implements IPresentableEnum {

    IDLE, PENDING, COMPLETED;

    private static final String BUNDLE = "resources.InternalRequestResources";
    private static final String KEY_PREFIX = "label.InternalRequestStateProgress.";

    @Override
    public String getLocalizedName() {
        final String key = KEY_PREFIX + name();
        return BundleUtil.getString(BUNDLE, key);
    }

}
