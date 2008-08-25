package pt.ist.expenditureTrackingSystem.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetEncodingFilter implements Filter {

    private static String defaultCharset;

    public void destroy() {
	// TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
	    ServletException {
	if (request.getCharacterEncoding() == null) {
	    request.setCharacterEncoding(defaultCharset);
	}
	filterChain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
	defaultCharset = filterConfig.getInitParameter("defaultCharset");
    }

}
