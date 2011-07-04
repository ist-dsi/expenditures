package pt.ist.expenditureTrackingSystem.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ExceptionHandlerFilter implements Filter {

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
	    ServletException {
	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	try {
	    filterChain.doFilter(request, response);
	} catch (ServletException servletException) {
	    servletException.getRootCause().printStackTrace();
	    httpServletRequest.getRequestDispatcher("/error.jsp").forward(request, response);
	} catch (Throwable throwable) {
	    throwable.printStackTrace();
	    httpServletRequest.getRequestDispatcher("/error.jsp").forward(request, response);
	}

    }
}
