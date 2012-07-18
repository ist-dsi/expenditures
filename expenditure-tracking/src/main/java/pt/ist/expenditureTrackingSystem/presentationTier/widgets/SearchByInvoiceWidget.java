/*
 * @(#)SearchByInvoiceWidget.java
 *
 * Copyright 2010 Instituto Superior Tecnico
 * Founding Authors: Luis Cruz, Nuno Ochoa, Paulo Abrantes
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Expenditure Tracking Module.
 *
 *   The Expenditure Tracking Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Expenditure Tracking Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Expenditure Tracking Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package pt.ist.expenditureTrackingSystem.presentationTier.widgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import module.dashBoard.presentationTier.DashBoardManagementAction;
import module.dashBoard.presentationTier.WidgetRequest;
import module.dashBoard.widgets.WidgetController;
import module.workflow.presentationTier.ProcessNodeSelectionMapper;
import module.workflow.presentationTier.actions.ProcessManagement;
import pt.ist.bennu.core.domain.contents.Node;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.bennu.core.util.BundleUtil;
import pt.ist.bennu.core.util.ClassNameBundle;

import org.apache.struts.action.ActionForward;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.SearchByInvoiceBean;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

@ClassNameBundle(bundle = "resources/ExpenditureResources", key = "title.widget.searchByInvoice")
/**
 * 
 * @author Bruno Santos
 * 
 */
public class SearchByInvoiceWidget extends WidgetController {

    final public static String NOT_FOUND = "NF";
    final public static String SINGLE_FOUND = "SF";

    @Override
    public void doView(WidgetRequest request) {
	request.setAttribute("searchBean", new SearchByInvoiceBean());
    }

    @Override
    public ActionForward doSubmit(WidgetRequest request) {
	SearchByInvoiceBean searchBean = getRenderedObject("searchByInvoiceBean");
	List<PaymentProcess> processesFound = new ArrayList<PaymentProcess>();

	for (PaymentProcess process : searchBean.search()) {
	    if (process.isAccessibleToCurrentUser()) {
		processesFound.add(process);
	    }
	}

	try {
	    String write = null;
	    if (processesFound.size() == 0) {
		write = SearchByInvoiceWidget.NOT_FOUND;
	    } else if (processesFound.size() == 1) {
		PaymentProcess process = processesFound.get(0);

		List<Node> nodes = ProcessNodeSelectionMapper.getForwardFor(process.getClass());
		String url = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(),
			ProcessManagement.workflowManagementURL + process.getExternalId() + "&" + ContextBaseAction.CONTEXT_PATH
				+ "=" + ((nodes.size() > 0) ? nodes.get(nodes.size() - 1).getContextPath() : ""));

		write = SearchByInvoiceWidget.SINGLE_FOUND + url;
	    } else {
		request.setAttribute("multipleProcessesFound", processesFound);
		return DashBoardManagementAction.forwardToWidget(request);
	    }

	    HttpServletResponse response = request.getResponse();
	    response.setContentType("text");
	    ServletOutputStream stream = response.getOutputStream();

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
