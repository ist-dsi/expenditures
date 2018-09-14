package pt.ist.expenditureTrackingSystem.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class RedirectToStrutsAction {

    public static String redirect(final String action, final String method) {
        return redirect(action, method, null, null);
    }

    public static String redirect(final String action, final String method, final String param, final String value) {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String contextPath = request.getContextPath();
        String path = "/" + action + ".do?method=" + method;
        if (param != null && value != null) {
            path = path + "&" + param + "=" + value;
        }
        final String safePath = path + "&" + GenericChecksumRewriter.CHECKSUM_ATTRIBUTE_NAME + "="
                + GenericChecksumRewriter.calculateChecksum(contextPath + path, request.getSession());
        return "redirect:" + safePath;
    }
}
