/*
 * @(#)RefundProcessAction.java
 *
 * Copyright 2009 Instituto Superior Tecnico
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
package pt.ist.expenditureTrackingSystem.presentationTier.actions.acquisitions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.presentationTier.actions.ProcessManagement;
import pt.ist.bennu.core.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/acquisitionRefundProcess")
/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class RefundProcessAction extends PaymentProcessAction {

    public ActionForward prepareCreateRefundProcessUnderCCP(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = getRenderedObject();
	if (bean == null) {
	    bean = new CreateRefundProcessBean(getLoggedPerson(), true);
	}
	request.setAttribute("bean", bean);
	return forward(request, "/acquisitions/refund/createRefundRequest.jsp");
    }

    public ActionForward prepareCreateRefundProcessUnderRCIST(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = getRenderedObject();
	if (bean == null) {
	    bean = new CreateRefundProcessBean(getLoggedPerson(), false);
	}
	request.setAttribute("bean", bean);
	return forward(request, "/acquisitions/refund/createRefundRequest.jsp");
    }

    public ActionForward prepareCreateRefundProcessUnderNormal(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {

	CreateRefundProcessBean bean = getRenderedObject();
	if (bean == null) {
	    bean = new CreateRefundProcessBean(getLoggedPerson(), false);
	}
	request.setAttribute("bean", bean);
	return forward(request, "/acquisitions/refund/createRefundRequestNormal.jsp");
    }

    public ActionForward createRefundProcess(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	CreateRefundProcessBean bean = getRenderedObject("createRefundProcess");
	try {
	    RefundProcess process = RefundProcess.createNewRefundProcess(bean);
	    return ProcessManagement.forwardToProcess(process);
	} catch (DomainException e) {
	    addLocalizedMessage(request, e.getLocalizedMessage());
	    request.setAttribute("bean", bean);
	    RenderUtils.invalidateViewState("createRefundProcess");
	    return forward(request, "/acquisitions/refund/createRefundRequest.jsp");
	}
    }

    public ActionForward createRefundProcessPostBack(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	CreateRefundProcessBean bean = getRenderedObject("createRefundProcess");
	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("createRefundProcess");
	return forward(request, "/acquisitions/refund/createRefundRequest.jsp");
    }

}
