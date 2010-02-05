package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import module.workflow.domain.WorkflowProcess;
import module.workflow.domain.WorkflowProcess.WorkflowProcessIndex;
import module.workflow.presentationTier.actions.ProcessManagement;
import myorg.util.ClassNameBundle;

import org.apache.struts.action.ActionForward;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixframework.plugins.luceneIndexing.DomainIndexer;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "title.quickAccess")
public class QuickViewWidget extends WidgetController {

    public static String NOT_FOUND = "NF";

    @Override
    public void doView(WidgetRequest request) {
	request.setAttribute("searchBean", new SearchPaymentProcess());
    }

    @Override
    public ActionForward doSubmit(WidgetRequest request) {

	SearchPaymentProcess searchBean = getRenderedObject("quickAccess");
	searchBean.setHasAvailableAndAccessibleActivityForUser(Boolean.FALSE);
	Set<PaymentProcess> search = searchBean.search();

	// SearchPaymentProcess searchBean = getRenderedObject("quickAccess");
	//	
	// List<WorkflowProcess> search =
	// DomainIndexer.getInstance().search(WorkflowProcess.class,
	// WorkflowProcessIndex.NUMBER,
	// searchBean.getProcessId(), 2);

	HttpServletResponse response = request.getResponse();
	response.setContentType("text");
	ServletOutputStream stream = null;

	try {
	    String write = (search.size() != 1 || !search.iterator().next().isAccessibleToCurrentUser()) ? QuickViewWidget.NOT_FOUND
		    : GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(),
			    ProcessManagement.workflowManagementURL + search.iterator().next().getExternalId());

	    stream = response.getOutputStream();
	    response.setContentLength(write.length());
	    stream.write(write.getBytes());
	    stream.flush();
	    stream.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    protected <T extends Object> T getRenderedObject(final String id) {
	final IViewState viewState = RenderUtils.getViewState(id);
	return (T) getRenderedObject(viewState);
    }

    protected <T extends Object> T getRenderedObject(final IViewState viewState) {
	if (viewState != null) {
	    MetaObject metaObject = viewState.getMetaObject();
	    if (metaObject != null) {
		return (T) metaObject.getObject();
	    }
	}
	return null;
    }
}
