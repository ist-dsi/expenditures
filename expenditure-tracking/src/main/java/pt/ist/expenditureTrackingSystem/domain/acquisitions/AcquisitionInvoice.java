/*
 * @(#)AcquisitionInvoice.java
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
package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.InvoiceFileBean;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans.InvoiceFileBean.RequestItemHolder;

/**
 * 
 * @author Luis Cruz
 * @author Paulo Abrantes
 * 
 */
@ClassNameBundle(bundle = "AcquisitionResources")
public class AcquisitionInvoice extends AcquisitionInvoice_Base {

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(AcquisitionInvoice.class, InvoiceFileBean.class);
    }

    public AcquisitionInvoice(String displayName, String filename, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fillInNonDefaultFields(WorkflowFileUploadBean bean) {
        super.fillInNonDefaultFields(bean);

        InvoiceFileBean fileBean = (InvoiceFileBean) bean;
        AcquisitionRequest request = fileBean.getRequest();
        request.validateInvoiceNumber(fileBean.getInvoiceNumber());

        setInvoiceNumber(fileBean.getInvoiceNumber());
        setInvoiceDate(fileBean.getInvoiceDate());

        setInvoiceDate(fileBean.getInvoiceDate());
        StringBuilder builder = new StringBuilder("<ul>");
        for (RequestItemHolder itemHolder : fileBean.getItems()) {
            if (itemHolder.isAccountable()) {
                addRequestItems(itemHolder.getItem());
                builder.append("<li>");
                builder.append(itemHolder.getDescription());
                builder.append(" - ");
                builder.append(BundleUtil.getString(Bundle.ACQUISITION, "acquisitionRequestItem.label.quantity"));
                builder.append(":");
                builder.append(itemHolder.getAmount());
                builder.append("</li>");
            }
        }
        builder.append("</ul>");
        setConfirmationReport(builder.toString());
    }

    @Override
    public void validateUpload(WorkflowProcess workflowProcess) {
        RegularAcquisitionProcess process = (RegularAcquisitionProcess) workflowProcess;

        if (process.isAcquisitionProcessed() && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(Authenticate.getUser())) {
            return;
        }

        if (ExpenditureTrackingSystem.isInvoiceAllowedToStartAcquisitionProcess()) {
            if (process.isInGenesis() && process.getRequestor() == Authenticate.getUser().getExpenditurePerson()) {
                return;
            }
            throw new ProcessFileValidationException("resources/AcquisitionResources",
                    "error.acquisitionInvoice.upload.invalid.or.in.construction");
        }

        throw new ProcessFileValidationException("resources/AcquisitionResources", "error.acquisitionInvoice.upload.invalid");
    }

    @Override
    public void postProcess(final WorkflowFileUploadBean bean) {
        final InvoiceFileBean fileBean = (InvoiceFileBean) bean;
        final AcquisitionRequest request = fileBean.getRequest();
        final AcquisitionProcess process = request.getProcess();
        if (!ExpenditureTrackingSystem.isInvoiceAllowedToStartAcquisitionProcess() || !process.isInGenesis()) {
            if (!fileBean.getHasMoreInvoices()) {
                ((RegularAcquisitionProcess) request.getProcess()).invoiceReceived();
            }
            request.processReceivedInvoice();
        }
    }

    @Override
    public boolean isPossibleToArchieve() {
        RegularAcquisitionProcess process = (RegularAcquisitionProcess) getProcess();
        return (process.isAcquisitionProcessed() || process.isInvoiceReceived())
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(Authenticate.getUser());
    }

    @Override
    public String getDisplayName() {
        return getFilename();
    }

    @Override
    public void processRemoval() {
        for (; !getFinancers().isEmpty(); getFinancers().iterator().next().removeAllocatedInvoices(this)) {
            ;
        }
        getProjectFinancers().clear();
        for (; !getRequestItems().isEmpty(); getRequestItems().iterator().next().removeInvoicesFiles(this)) {
            ;
        }
        for (; !getUnitItems().isEmpty(); getUnitItems().iterator().next().removeConfirmedInvoices(this)) {
            ;
        }
    }

    @Deprecated
    public boolean hasConfirmationReport() {
        return getConfirmationReport() != null;
    }

}
