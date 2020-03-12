package pt.ist.expenditureTrackingSystem.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class RedirectToStrutsAction {

    public static String redirect(final String action, final String method, String ... paramValuePairs) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String contextPath = request.getContextPath();

        final StringBuilder pathBuilder = new StringBuilder();
        pathBuilder.append("/" + action + ".do?method=" + method);
        for(int i = 0; i < paramValuePairs.length; i += 2) {
            String param = paramValuePairs[i];
            String value = paramValuePairs[i + 1];

            pathBuilder.append("&" + param + "=" + value);
        }
        String path = pathBuilder.toString();
        final String safePath = path + "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                + GenericChecksumRewriter.calculateChecksum(contextPath + path, request.getSession());
        return "redirect:" + safePath;
    }
}
