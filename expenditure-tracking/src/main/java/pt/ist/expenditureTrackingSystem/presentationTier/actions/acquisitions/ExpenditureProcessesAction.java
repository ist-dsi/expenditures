/*
 * @(#)ExpenditureProcessesAction.java
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.WorkflowLayoutContext;
import module.workflow.presentationTier.actions.ProcessManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.presentationTier.Context;
import pt.ist.bennu.core.presentationTier.actions.ContextBaseAction;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AbstractFundAllocationActivityInformation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionRequestItemActivityInformation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionRequestItemActivityInformation.CreateItemSchemaType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.PayAcquisitionActivityInformation;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.expenditureTrackingSystem.domain.dto.PaymentReferenceBean;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/expenditureProcesses")
/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author João Alfaiate
 * 
 */
public class ExpenditureProcessesAction extends ContextBaseAction {

    public ActionForward addAllocationFundGeneric(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        AbstractFundAllocationActivityInformation<PaymentProcess> activityInformation = getRenderedObject("activityBean");
        final PaymentProcess process = activityInformation.getProcess();
        request.setAttribute("process", process);
        request.setAttribute("information", activityInformation);

        List<FundAllocationBean> fundAllocationBeans = activityInformation.getBeans();
        Integer index = Integer.valueOf(request.getParameter("index"));

        Financer financer = getDomainObject(request, "financerOID");
        FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
        fundAllocationBean.setFundAllocationId(null);
        fundAllocationBean.setEffectiveFundAllocationId(null);
        fundAllocationBean.setAllowedToAddNewFund(false);

        fundAllocationBeans.add(index + 1, fundAllocationBean);
        RenderUtils.invalidateViewState();
        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    public ActionForward removeAllocationFundGeneric(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        AbstractFundAllocationActivityInformation<PaymentProcess> activityInformation = getRenderedObject("activityBean");
        final PaymentProcess process = activityInformation.getProcess();
        request.setAttribute("process", process);
        request.setAttribute("information", activityInformation);

        List<FundAllocationBean> fundAllocationBeans = activityInformation.getBeans();
        int index = Integer.valueOf(request.getParameter("index")).intValue();

        fundAllocationBeans.remove(index);
        RenderUtils.invalidateViewState();
        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    public ActionForward addDiaryNumber(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {

        PayAcquisitionActivityInformation<PaymentProcess> activityInformation = getRenderedObject("activityBean");
        final PaymentProcess process = activityInformation.getProcess();
        request.setAttribute("process", process);
        request.setAttribute("information", activityInformation);

        List<PaymentReferenceBean> fundAllocationBeans = activityInformation.getBeans();
        Integer index = Integer.valueOf(request.getParameter("index"));

        Financer financer = getDomainObject(request, "financerOID");
        PaymentReferenceBean fundAllocationBean = new PaymentReferenceBean(financer);
        fundAllocationBean.setDiaryNumber(null);

        fundAllocationBeans.add(index + 1, fundAllocationBean);
        RenderUtils.invalidateViewState();
        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    public ActionForward removeDiaryNumber(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        PayAcquisitionActivityInformation<PaymentProcess> activityInformation = getRenderedObject("activityBean");
        final PaymentProcess process = activityInformation.getProcess();
        request.setAttribute("process", process);
        request.setAttribute("information", activityInformation);

        List<PaymentReferenceBean> fundAllocationBeans = activityInformation.getBeans();
        int index = Integer.valueOf(request.getParameter("index")).intValue();

        fundAllocationBeans.remove(index);
        RenderUtils.invalidateViewState();
        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    public ActionForward addTransactionNumber(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        PayAcquisitionActivityInformation<PaymentProcess> activityInformation = getRenderedObject("activityBean");
        final PaymentProcess process = activityInformation.getProcess();
        request.setAttribute("process", process);
        request.setAttribute("information", activityInformation);

        List<PaymentReferenceBean> fundAllocationBeans = activityInformation.getBeans();
        Integer index = Integer.valueOf(request.getParameter("index"));

        Financer financer = getDomainObject(request, "financerOID");
        PaymentReferenceBean fundAllocationBean = new PaymentReferenceBean(financer);
        fundAllocationBean.setTransactionNumber(null);

        fundAllocationBeans.add(index + 1, fundAllocationBean);
        RenderUtils.invalidateViewState();
        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    public ActionForward removeTransactionNumber(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        PayAcquisitionActivityInformation<PaymentProcess> activityInformation = getRenderedObject("activityBean");
        final PaymentProcess process = activityInformation.getProcess();
        request.setAttribute("process", process);
        request.setAttribute("information", activityInformation);

        List<PaymentReferenceBean> fundAllocationBeans = activityInformation.getBeans();
        int index = Integer.valueOf(request.getParameter("index")).intValue();

        fundAllocationBeans.remove(index);
        RenderUtils.invalidateViewState();
        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    public ActionForward itemPostBack(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        CreateAcquisitionRequestItemActivityInformation activityInformation = getRenderedObject("activityBean");
        WorkflowProcess process = FenixFramework.getDomainObject(request.getParameter("processId"));
        RenderUtils.invalidateViewState();
        activityInformation.setRecipient(null);
        activityInformation.setAddress(null);

        if (activityInformation.getCreateItemSchemaType().equals(CreateItemSchemaType.EXISTING_DELIVERY_INFO)) {
            activityInformation.setDeliveryInfo(activityInformation.getAcquisitionRequest().getRequester()
                    .getDeliveryInfoByRecipientAndAddress(activityInformation.getRecipient(), activityInformation.getAddress()));
        } else {
            activityInformation.setDeliveryInfo(null);
        }

        request.setAttribute("information", activityInformation);
        request.setAttribute("process", process);
        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    public ActionForward itemInvalidInfo(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        CreateAcquisitionRequestItemActivityInformation activityInformation = getRenderedObject("activityBean");
        WorkflowProcess process = FenixFramework.getDomainObject(request.getParameter("processId"));

        request.setAttribute("information", activityInformation);
        request.setAttribute("process", process);

        return ProcessManagement.performActivityPostback(activityInformation, request);
    }

    @Override
    public Context createContext(String contextPathString, HttpServletRequest request) {
        WorkflowProcess process = getProcess(request);
        WorkflowLayoutContext layout = process.getLayout();
        layout.setElements(contextPathString);
        return layout;
    }

    protected <T extends WorkflowProcess> T getProcess(HttpServletRequest request) {
        return (T) getDomainObject(request, "processId");
    }
}
