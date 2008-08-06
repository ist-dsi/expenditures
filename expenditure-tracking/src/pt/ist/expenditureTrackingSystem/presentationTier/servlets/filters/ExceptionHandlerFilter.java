package pt.ist.expenditureTrackingSystem.presentationTier.servlets.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ExceptionHandlerFilter implements Filter {

    @Override
    public void destroy() {
	// TODO Auto-generated method stub

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
	    ServletException {
	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	try {
	    filterChain.doFilter(request, response);
	} catch (ServletException servletException) {
	    StringWriter out = new StringWriter();
	    servletException.getRootCause().printStackTrace(new PrintWriter(out));
	    request.setAttribute("error", out.toString());
	    httpServletRequest.getRequestDispatcher("/error.jsp").forward(request, response);
	} catch (Exception exception) {
	    StringWriter out = new StringWriter();
	    exception.printStackTrace(new PrintWriter(out));
	    request.setAttribute("error", out.toString());
	    httpServletRequest.getRequestDispatcher("/error.jsp").forward(request, response);
	}

    }
}
