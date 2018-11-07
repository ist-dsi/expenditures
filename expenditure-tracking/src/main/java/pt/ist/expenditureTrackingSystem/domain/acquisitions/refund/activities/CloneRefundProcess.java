/*
 * @(#)SubmitForApproval.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.Refundee;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.CostCenter;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

/**
 * 
 * @author Paulo Abrantes
 * @author Luis Cruz
 * 
 */
public class CloneRefundProcess extends WorkflowActivity<RefundProcess, CloneRefundProcessInformation> {

    @Override
    public boolean isActive(final RefundProcess process, final User user) {
        return process.isAccessible(user);
    }

    @Override
    protected void process(final CloneRefundProcessInformation activityInformation) {
        final RefundProcess process = activityInformation.getProcess();
        final RefundRequest request = process.getRequest();

        final CreateRefundProcessBean bean = new CreateRefundProcessBean(Authenticate.getUser().getExpenditurePerson(), process.getUnderCCPRegime());
        final Refundee refundee = request.getRefundee();
        if (refundee.getPerson() == null) {
            bean.setRefundeeName(refundee.getName());
            bean.setRefundeeFiscalCode(refundee.getFiscalCode());
            bean.setExternalPerson(true);
        } else {
            bean.setRefundee(refundee.getPerson());
        }
        bean.setRequestingUnit(request.getRequestingUnit());
        bean.setRequestUnitPayingUnit(false);
        bean.setMissionProcess(process.getMissionProcess());
        bean.setRapid(process.getRapid());
        final RefundProcess newRefundProcess = RefundProcess.createNewRefundProcess(bean);
        final RefundRequest newRequest = newRefundProcess.getRequest();

        final Map<Financer, Financer> financerMap = new HashMap<>();
        request.getFinancersSet().forEach(f -> {
            final Financer newFinancer = copyFinancer(f, newRequest);
            newFinancer.setAccountingUnit(f.getAccountingUnit());
            financerMap.put(f, newFinancer);
        });
        request.getRequestItemsSet().stream()
            .map(ri -> (RefundItem) ri)
            .forEach(requestItem -> {
                final RefundItem newRequestItem = new RefundItem(newRequest, requestItem.getValueEstimation(), requestItem.getMaterial(),
                        requestItem.getClassification(), requestItem.getDescription());
                requestItem.getUnitItemsSet().forEach(unitItem -> {
                    new UnitItem(financerMap.get(unitItem.getFinancer()), newRequestItem, unitItem.getShareValue(), false, false);
                });
            });

        activityInformation.setNewProcess(newRefundProcess);
        newRefundProcess.logExecution(getClass().getSimpleName(), process.getProcessNumber());
    }

    @Override
    public ActivityInformation<RefundProcess> getActivityInformation(RefundProcess process) {
        return new CloneRefundProcessInformation(process, this);
    }

    private Financer copyFinancer(final Financer f, final RefundRequest request) {
        final Unit unit = f.getUnit();
        if (f instanceof ProjectFinancer) {
            if (unit instanceof Project) {
                return new ProjectFinancer(request, (Project) unit);
            } else if (unit instanceof SubProject) {
                return new ProjectFinancer(request, (SubProject) unit);
            }
        }
        if (unit instanceof CostCenter) {
            return new Financer(request, (CostCenter) unit);
        }
        throw new Error("Unsupported financer type: " + f.getClass().getSimpleName() + " for unit type " + unit.getClass().getSimpleName());
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return Bundle.ACQUISITION;
    }

    @Override
    public boolean isUserAwarenessNeeded(final RefundProcess process, final User user) {
        return false;
    }

    @Override
    public void handleResponse(final HttpServletRequest request, final HttpServletResponse response, final CloneRefundProcessInformation activityInformation) {
        try {
            response.sendRedirect(request.getContextPath() + "/ForwardToProcess/" + activityInformation.getNewProcess().getExternalId());
        } catch (final IOException e) {
            throw new Error(e);
        }
    }

    @Override
    public boolean customHandleResponse() {
        return true;
    }
}
