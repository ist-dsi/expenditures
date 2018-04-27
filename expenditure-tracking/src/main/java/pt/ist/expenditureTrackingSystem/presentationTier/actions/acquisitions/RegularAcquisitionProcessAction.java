/*
 * @(#)RegularAcquisitionProcessAction.java
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import module.workflow.presentationTier.actions.ProcessManagement;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.AllocateProjectFundsPermanently;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.FundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchPaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.search.SearchProcessValues;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.FundAllocationExpirationDate;
import pt.ist.expenditureTrackingSystem.domain.dto.DateIntervalBean;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@StrutsFunctionality(app = SearchPaymentProcessesAction.class, path = "fundAllocations", titleKey = "link.fundAllocations")
@Mapping(path = "/acquisitionProcess")
/**
 * 
 * @author Pedro Santos
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class RegularAcquisitionProcessAction extends PaymentProcessAction {

    @EntryPoint
    public ActionForward checkFundAllocations(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {

        IViewState viewState = RenderUtils.getViewState("dateSelection");

        DateIntervalBean bean =
                viewState == null ? new DateIntervalBean() : (DateIntervalBean) viewState.getMetaObject().getObject();

        if (viewState == null) {
            LocalDate today = new LocalDate();
            bean.setBegin(today);
            bean.setEnd(today);
        }

        DateTime begin = bean.getBegin().toDateTimeAtStartOfDay();
        DateTime end = bean.getEnd().plusDays(1).toDateTimeAtStartOfDay();

        List<AcquisitionProcess> processes = new ArrayList<AcquisitionProcess>();

        for (AcquisitionProcess process : GenericProcess.getAllProcesses(AcquisitionProcess.class)) {
            if (!process.getExecutionLogs(begin, end, FundAllocation.class, ProjectFundAllocation.class,
                    AllocateFundsPermanently.class, AllocateProjectFundsPermanently.class).isEmpty()) {
                processes.add(process);
            }
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("processes", processes);
        request.setAttribute("bean", bean);

        return forward("/acquisitions/viewFundAllocations.jsp");
    }

    public ActionForward allocateAllPendingFundsToSupplier(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final SearchPaymentProcess searchPaymentProcess = new SearchPaymentProcess();
        searchPaymentProcess.setSearchProcess(SearchProcessValues.ACQUISITIONS);
        searchPaymentProcess.setAcquisitionProcessStateType(AcquisitionProcessStateType.SUBMITTED_FOR_FUNDS_ALLOCATION);
        final ProcessManagement processManagement = new ProcessManagement();
        final Set<PaymentProcess> search = searchPaymentProcess.search();
        for (final PaymentProcess paymentProcess : search) {
            if (paymentProcess instanceof RegularAcquisitionProcess) {
                final RegularAcquisitionProcess regularAcquisitionProcess = (RegularAcquisitionProcess) paymentProcess;
                final WorkflowActivity<WorkflowProcess, ActivityInformation<WorkflowProcess>> activity =
                        paymentProcess.getActivity(FundAllocationExpirationDate.class.getSimpleName());
                final ActivityInformation<WorkflowProcess> activityInformation =
                        activity.getActivityInformation(regularAcquisitionProcess);
                if (activity.isActive(regularAcquisitionProcess)) {
                    processManagement.executeActivity(regularAcquisitionProcess, request, response, activity, activityInformation);
                }
            }
        }
        final SearchPaymentProcessesAction action = new SearchPaymentProcessesAction();
        return action.search(mapping, request, searchPaymentProcess, true);
    }

}
