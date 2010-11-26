package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import module.workflow.presentationTier.ProcessNodeSelectionMapper;
import module.workflow.presentationTier.actions.ProcessManagement;
import module.workflow.widgets.QuickViewWidget;
import myorg.domain.contents.Node;
import myorg.presentationTier.actions.ContextBaseAction;
import myorg.util.BundleUtil;
import myorg.util.ClassNameBundle;

import org.apache.struts.action.ActionForward;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.SearchByInvoiceBean;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "title.widget.searchByInvoice")
public class SearchByInvoiceWidget extends WidgetController {

    public static String NOT_FOUND = "NF";

    @Override
    public void doView(WidgetRequest request) {
	request.setAttribute("searchBean", new SearchByInvoiceBean());
    }

    @Override
    public ActionForward doSubmit(WidgetRequest request) {

	SearchByInvoiceBean searchBean = getRenderedObject("searchByInvoiceBean");
	List<PaymentProcess> processesFound = searchBean.search();

	HttpServletResponse response = request.getResponse();
	response.setContentType("text");
	ServletOutputStream stream = null;

	try {
	    String write = null;
	    if (processesFound.size() == 0) {
		write = QuickViewWidget.NOT_FOUND;
	    } else {
		for (PaymentProcess process : processesFound) {
		    if (!process.isAccessibleToCurrentUser()) {
			continue;
		    }
		    List<Node> nodes = ProcessNodeSelectionMapper.getForwardFor(process.getClass());
		    write = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(),
			    ProcessManagement.workflowManagementURL + process.getExternalId() + "&"
				    + ContextBaseAction.CONTEXT_PATH + "="
				    + ((nodes.size() > 0) ? nodes.get(nodes.size() - 1).getContextPath() : ""));
		    break;
		}

	    }

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

    @Override
    public String getWidgetDescription() {
	return BundleUtil.getStringFromResourceBundle("resources/ExpenditureResources",
		"widget.description.SearchByInvoiceWidget");
    }
}
