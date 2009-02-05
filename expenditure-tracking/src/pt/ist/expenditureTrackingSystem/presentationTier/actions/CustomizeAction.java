package pt.ist.expenditureTrackingSystem.presentationTier.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.Options;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping( path="/customize" )
public class CustomizeAction extends BaseAction {

    protected Options getOptions() {
	final Person person = getLoggedPerson();
	return person.getOptions();
    }

    protected final ActionForward showOptions(final ActionMapping mapping, final HttpServletRequest request,
	    final String forward)
    		throws Exception {
	final Options options = getOptions();
	request.setAttribute("options", options);
	return forward(request, forward);
    }

    public final ActionForward showNotificationOptions(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	return showOptions(mapping, request, "/options/notificationOptions.jsp");
    }

    public final ActionForward showInterfaceOptions(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) throws Exception {
	final FileUploadBean fileUploadBean = new FileUploadBean();
	request.setAttribute("fileUploadBean", fileUploadBean);
	return showOptions(mapping, request, "/options/interfaceOptions.jsp");
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
