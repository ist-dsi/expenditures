package pt.ist.expenditureTrackingSystem.presentationTier.servlets.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.CascadingStyleSheet;
import pt.ist.expenditureTrackingSystem.domain.Options;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class LayoutFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    protected Person getLoggedPerson() {
	final User user = UserView.getCurrentUser();
	return user == null ? null : Person.findByUsername(user.getUsername());
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
	    throws IOException, ServletException {
	final ServletOutputStream servletOutputStream = servletResponse.getOutputStream();
	try {
	    final Person person = getLoggedPerson();
	    final Options options = person.getOptions();
	    final CascadingStyleSheet cascadingStyleSheet = options.getCascadingStyleSheet();
	    final byte[] bytes = cascadingStyleSheet.getContent().getBytes();
	    servletOutputStream.write(bytes);
	} finally {
	    servletOutputStream.close();
	}
    }

}

