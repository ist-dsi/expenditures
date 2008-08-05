package pt.ist.expenditureTrackingSystem.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import pt.ist.expenditureTrackingSystem.presentationTier.servlets.filters.requestWrappers.ResponseWrapper;
import pt.ist.fenixWebFramework.servlets.filters.RequestWrapperFilter;

public class ExpendituresRequestWrapperFilter extends RequestWrapperFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	    ServletException {
	ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
	super.doFilter(request, responseWrapper, chain);
	responseWrapper.writeRealResponse();
    }
}
