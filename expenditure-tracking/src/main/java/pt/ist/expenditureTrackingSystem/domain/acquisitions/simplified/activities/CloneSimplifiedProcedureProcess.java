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
package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

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
import module.workflow.domain.ActivityLog;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProposalDocument;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.UnitItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAcquisitionProcessBean;
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
public class CloneSimplifiedProcedureProcess extends WorkflowActivity<SimplifiedProcedureProcess, CloneSimplifiedProcedureProcessInformation> {

    @Override
    public boolean isActive(final SimplifiedProcedureProcess process, final User user) {
        return process.isAccessible(user);
    }

    @Override
    protected void process(final CloneSimplifiedProcedureProcessInformation activityInformation) {
        final SimplifiedProcedureProcess process = activityInformation.getProcess();
        final AcquisitionRequest request = process.getRequest();

        final CreateAcquisitionProcessBean bean = new CreateAcquisitionProcessBean(process.getProcessClassification());
        bean.setRequester(Authenticate.getUser().getExpenditurePerson());
        bean.setSupplier(request.getSelectedSupplier());
        bean.setRequestingUnit(process.getRequestingUnit());
        bean.setRequestUnitPayingUnit(false);
        final SimplifiedProcedureProcess newProcess = SimplifiedProcedureProcess.createNewAcquisitionProcess(bean);
        newProcess.setMissionProcess(process.getMissionProcess());
        //newProcess.setPurchaseOrderDocument();
        final AcquisitionRequest newRequest = newProcess.getRequest();

        newRequest.setContractSimpleDescription(request.getContractSimpleDescription());
        newRequest.setRefundee(request.getRefundee());
        //newRequest.setRequester(Authenticate.getUser().getExpenditurePerson());
        //newRequest.setRequestingUnit(request.getRequestingUnit());
        newRequest.setSelectedSupplier(request.getSelectedSupplier());

        newRequest.getSuppliersSet().addAll(request.getSuppliersSet());
        final Map<Financer, Financer> financerMap = new HashMap<>();
        request.getFinancersSet().forEach(f -> {
            final Financer newFinancer = copyFinancer(f, newRequest);
            newFinancer.setAccountingUnit(f.getAccountingUnit());
            financerMap.put(f, newFinancer);
        });
        request.getRequestItemsSet().stream()
            .map(ri -> (AcquisitionRequestItem) ri)
            .forEach(requestItem -> {
					final AcquisitionRequestItem newRequestItem = new AcquisitionRequestItem(newRequest,
							requestItem.getDescription(), requestItem.getQuantity(), requestItem.getUnitValue(),
							requestItem.getVatValue(), requestItem.getAdditionalCostValue(),
							requestItem.getProposalReference(), requestItem.getMaterial(),
							requestItem.getResearchAndDevelopmentPurpose(), requestItem.getRecipient(),
							requestItem.getAddress(), requestItem.getRecipientPhone(), requestItem.getRecipientEmail(),
							requestItem.getClassification());
                if (financerMap.size() > 1) {
                    requestItem.getUnitItemsSet().forEach(unitItem -> {
                        new UnitItem(financerMap.get(unitItem.getFinancer()), newRequestItem, unitItem.getShareValue(), false, false);
                    });
                }
            });

        final AcquisitionProposalDocument document = (AcquisitionProposalDocument) process.getFileStream(AcquisitionProposalDocument.class).findAny().orElse(null);
        if (document != null) {
            final AcquisitionProposalDocument newDocument = new AcquisitionProposalDocument(document.getDisplayName(), document.getFilename(), document.getContent());
            newProcess.getFilesSet().add(newDocument);
        }

        activityInformation.setNewProcess(newProcess);
        ActivityLog log = newProcess.logExecution(getClass().getSimpleName(), process.getProcessNumber());
    }

    @Override
    public ActivityInformation<SimplifiedProcedureProcess> getActivityInformation(SimplifiedProcedureProcess process) {
        return new CloneSimplifiedProcedureProcessInformation(process, this);
    }

    private Financer copyFinancer(final Financer f, final AcquisitionRequest request) {
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
    public boolean isUserAwarenessNeeded(final SimplifiedProcedureProcess process, final User user) {
        return false;
    }

    @Override
    public void handleResponse(final HttpServletRequest request, final HttpServletResponse response, final CloneSimplifiedProcedureProcessInformation activityInformation) {
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
