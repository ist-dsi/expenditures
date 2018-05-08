/*
 * @(#)SendPurchaseOrderToSupplier.java
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

import org.fenixedu.bennu.WorkflowConfiguration;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.messaging.core.domain.MessageTemplate;
import org.fenixedu.messaging.core.domain.Sender;
import org.fenixedu.messaging.core.template.DeclareMessageTemplate;
import org.fenixedu.messaging.core.template.TemplateParameter;
import org.fenixedu.messaging.core.ui.MessageBean;
import org.fenixedu.messaging.core.ui.MessagingUtils;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.SigningState;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * @author Ricardo Almeida
 * 
 */
@DeclareMessageTemplate(id = "template.SendPurchaseOrderToSupplier",
        description = "template.SendPurchaseOrderToSupplier.description",
        subject = "template.SendPurchaseOrderToSupplier.subject", text = "template.SendPurchaseOrderToSupplier.text",
        parameters = {
                @TemplateParameter(id = "processNumber", description = "template.SendPurchaseOrderToSupplier.processNumber"),
                @TemplateParameter(id = "requestId", description = "template.SendPurchaseOrderToSupplier.requestId"), },
        bundle = Bundle.EXPENDITURE)

public class SendPurchaseOrderToSupplier
        extends WorkflowActivity<RegularAcquisitionProcess, ActivityInformation<RegularAcquisitionProcess>> {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        return isUserProcessOwner(process, user) && process.getAcquisitionProcessState().isAuthorized()
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(user) && process.hasPurchaseOrderDocument()
                && process.isCommitted() && process.isReverifiedAfterCommitment()
                && (!WorkflowConfiguration.getConfiguration().smartsignerIntegration() || (process.hasPurchaseOrderDocument()
                        && process.getPurchaseOrderDocument().getSigningState() != null
                        && process.getPurchaseOrderDocument().getSigningState().compareTo(SigningState.SIGNED) == 0));
    }

    @Override
    protected void process(ActivityInformation<RegularAcquisitionProcess> activityInformation) {
        activityInformation.getProcess().processAcquisition();
    }

    @Override
    public String getLocalizedName() {
        return BundleUtil.getString(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
        return "resources/AcquisitionResources";
    }

    @Override
    public boolean customHandleResponse() {
        return WorkflowConfiguration.getConfiguration().smartsignerIntegration();
    }

    @Override
    public void handleResponse(final HttpServletRequest request, final HttpServletResponse response, final ActivityInformation<RegularAcquisitionProcess> activityInformation) {
        final Map<String, Object> params = new HashMap<>();
        params.put("processNumber", activityInformation.getProcess().getProcessNumber());
        params.put("requestId", activityInformation.getProcess().getAcquisitionRequestDocumentID());

        final MessageTemplate mt = MessageTemplate.get("template.SendPurchaseOrderToSupplier");
        final LocalizedString subject = mt.getCompiledSubject(params);
        final LocalizedString body = mt.getCompiledTextBody(params);

        final MessageBean mb = new MessageBean();
        final Sender purchaseOrderSender = ExpenditureTrackingSystem.getInstance().getPurchaseOrderSender();

        mb.setLockedSender(purchaseOrderSender);
        mb.setSingleRecipients(activityInformation.getProcess().getRequest().getSelectedSupplier().getEmail());
        mb.setSubject(subject);
        mb.setTextBody(body);
        mb.addAttachment(activityInformation.getProcess().getPurchaseOrderDocument());

        try {
            MessagingUtils.redirectToNewMessage(request, response, mb);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

}
