package pt.ist.expenditureTrackingSystem.util;

import pt.ist.bennu.core._development.PropertiesManager;
import pt.ist.bennu.core.domain.User;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhotoTool {

    public static String getPhotoUrl(final User user, final String contextPath) {
	final String username = user == null ? null : user.getUsername();
	return getPhotoUrl(username, contextPath);
    }

    public static String getPhotoUrl(final String username, final String contextPath) {
	final String uri = PropertiesManager.getProperty("user.photo.url");
	return uri == null || username == null ? getFallbackPhoto(contextPath) : uri + username;
    }

    private static String getFallbackPhoto(final String contextPath) {
	return contextPath + "/images/photo_placer01_" + Language.getLanguage().name() + ".gif";
    }

}
