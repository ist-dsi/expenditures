package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.Options;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.Context;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping( path="/customize" )
@Forwards( {
    @Forward(name="show.options", path="/options/notificationOptions.jsp"),
    @Forward(name="show.interface.options", path="/options/interfaceOptions.jsp")
} )
public class CustomizeAction extends BaseAction {

    private static final Context CONTEXT = new Context("options");

    @Override
    protected Context getContextModule() {
	return CONTEXT;
    }

    protected Options getOptions() {
	final User user = UserView.getUser();
	final Person person = user.getPerson();
	return person.getOptions();
    }

    protected final ActionForward showOptions(final ActionMapping mapping, final HttpServletRequest request,
	    final String forward)
    		throws Exception {
	final Options options = getOptions();
	request.setAttribute("options", options);
	return mapping.findForward(forward);
    }

    public final ActionForward showNotificationOptions(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	return showOptions(mapping, request, "show.options");
    }

    public final ActionForward showInterfaceOptions(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final FileUploadBean fileUploadBean = new FileUploadBean();
	request.setAttribute("fileUploadBean", fileUploadBean);
	return showOptions(mapping, request, "show.interface.options");
    }

    public final ActionForward uploadCss(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final FileUploadBean fileUploadBean = getRenderedObject();
	final byte[] bytes = consumeInputStream(fileUploadBean);
	final Options options = getOptions();
	options.setCascadingStyleSheet(bytes, fileUploadBean.getFilename());
	RenderUtils.invalidateViewState();
	return showInterfaceOptions(mapping, form, request, response);
    }

    public final ActionForward deleteCss(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final Options options = getOptions();
	options.deleteCascadingStyleSheet();
	return showInterfaceOptions(mapping, form, request, response);
    }

}
