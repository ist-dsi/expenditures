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

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import module.finance.util.Money;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
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
        setState(AcquisitionInvoiceState.RECEIVED);
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

        StringBuilder builder = new StringBuilder("<ul>");
        for (RequestItemHolder itemHolder : fileBean.getItems()) {
            if (itemHolder.isAccountable()) {
                final AcquisitionRequestItem item = itemHolder.getItem();
                final int amount = itemHolder.getAmount();

                new AcquisitionInvoiceItem(this, item, amount, itemHolder.getUnitValue(), itemHolder.getVatValue(), itemHolder.getAdditionalCostValue());

                addRequestItems(item);
                builder.append("<li>");
                builder.append(itemHolder.getDescription());
                builder.append(" - ");
                builder.append(BundleUtil.getString(Bundle.ACQUISITION, "acquisitionRequestItem.label.quantity"));
                builder.append(":");
                builder.append(amount);
                builder.append("</li>");
            }
        }
        builder.append("</ul>");
        setConfirmationReport(builder.toString());
    }

    @Override
    public void validateUpload(final WorkflowProcess workflowProcess) {
        final SimplifiedProcedureProcess process = (SimplifiedProcedureProcess) workflowProcess;

        final AcquisitionProcessStateType processStateType = process.getAcquisitionProcessState().getAcquisitionProcessStateType();
        if (processStateType.isActive()
                && processStateType.compareTo(AcquisitionProcessStateType.ACQUISITION_PROCESSED) >= 0
                && ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(Authenticate.getUser())
                && processStateType != AcquisitionProcessStateType.ACQUISITION_PAYED
                //&& !process.areAllInvoicesRegistered()
                ) {
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
            ((RegularAcquisitionProcess) request.getProcess()).invoiceReceived();
            request.processReceivedInvoice();
        }
        setLocalInvoiceNumber(process.getPaymentProcessYear().nextInvoiceNumber());
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

    public boolean isConfirmedByForAllUnits() {
        return getItemSet().stream()
            .map(aii -> aii.getItem())
            .flatMap(i -> i.getUnitItemsSet().stream())
            .noneMatch(ui -> !ui.getConfirmedInvoicesSet().contains(this));
    }

    public Money getTotalValue() {
        return getItemSet().stream().map(i -> i.getTotalValue()).reduce(Money.ZERO, Money::add);
    }

    public Money getVatAmount() {
        return getItemSet().stream().map(i -> i.getVatAmount()).reduce(Money.ZERO, Money::add);
    }

    public void registerPayment(final String paymentDocumentNumber, final String paymentDocumentDate, final String payedValue) {
        setPaymentDocumentNumber(paymentDocumentNumber);
        setPaymentDocumentDate(paymentDocumentDate);
        setPayedValue(payedValue);
        setState(AcquisitionInvoiceState.PAYED);

        getUnitItemsSet().stream()
            .map(i -> (ProjectFinancer) i.getFinancer())
            .forEach(f -> {
                f.addEffectiveProjectFundAllocationId("-");
                f.addEffectiveFundAllocationId("-");
                f.addPaymentDiaryNumber("-");
                f.addTransactionNumber("-");
            });
    }

}
