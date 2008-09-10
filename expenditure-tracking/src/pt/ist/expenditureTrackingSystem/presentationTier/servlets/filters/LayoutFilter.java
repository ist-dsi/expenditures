package pt.ist.expenditureTrackingSystem.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.CascadingStyleSheet;
import pt.ist.expenditureTrackingSystem.domain.Options;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;

public class LayoutFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
	    throws IOException, ServletException {
	final ServletOutputStream servletOutputStream = servletResponse.getOutputStream();
	try {
	    final User user = UserView.getUser();
	    final Person person = user.getPerson();
	    final Options options = person.getOptions();
	    final CascadingStyleSheet cascadingStyleSheet = options.getCascadingStyleSheet();
	    final byte[] bytes = cascadingStyleSheet.getContent().getBytes();
	    servletOutputStream.write(bytes);
	} finally {
	    servletOutputStream.close();
	}
    }

}

