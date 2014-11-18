package pt.ist.expenditureTrackingSystem.util;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;

public class PhotoTool {

    public static String getPhotoUrl(final User user, final String contextPath) {
        final String username = user == null ? null : user.getUsername();
        return getPhotoUrl(username, contextPath);
    }

    public static String getPhotoUrl(final String username, final String contextPath) {
        final String uri = ExpenditureConfiguration.get().userPhotoUrl();
        return uri == null || username == null ? getFallbackPhoto(contextPath) : uri + username;
    }

    private static String getFallbackPhoto(final String contextPath) {
        return contextPath + "/images/photo_placer01_" + I18N.getLocale().getLanguage() + ".gif";
    }

}
