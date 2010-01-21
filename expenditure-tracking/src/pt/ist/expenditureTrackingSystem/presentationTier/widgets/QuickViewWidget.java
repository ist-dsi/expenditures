package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.util.Set;

import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import module.workflow.presentationTier.actions.ProcessManagement;
import myorg.util.ClassNameBundle;

import org.apache.struts.action.ActionForward;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "title.quickAccess")
public class QuickViewWidget extends WidgetController {

    @Override
    public void doView(WidgetRequest request) {
	request.setAttribute("searchBean", new SearchPaymentProcess());
    }

    @Override
    public ActionForward doSubmit(WidgetRequest request) {
	SearchPaymentProcess searchBean = getRenderedObject("quickAccess");
	searchBean.setHasAvailableAndAccessibleActivityForUser(Boolean.FALSE);
	Set<PaymentProcess> search = searchBean.search();

	if (search.size() != 1) {
	    request.setAttribute("widgetQuickView.messages", "widget.widgetQuickView.noProcessFound");
	    return super.doSubmit(request);
	}

	PaymentProcess process = search.iterator().next();
	return ProcessManagement.forwardToProcess(process);
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
