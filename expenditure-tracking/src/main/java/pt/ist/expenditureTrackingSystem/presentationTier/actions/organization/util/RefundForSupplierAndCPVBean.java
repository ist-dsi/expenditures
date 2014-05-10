package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.util;

import pt.ist.bennu.core.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundableInvoiceFile;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class RefundForSupplierAndCPVBean implements Comparable<RefundForSupplierAndCPVBean> {

    private final Supplier supplier;
    private final CPVReference cpvReference;
    private final RefundProcess refundProcess;

    public RefundForSupplierAndCPVBean(final Supplier supplier, final CPVReference cpvReference, final RefundProcess refundProcess) {
        this.supplier = supplier;
        this.cpvReference = cpvReference;
        this.refundProcess = refundProcess;
    }

    public RefundProcess getRefundProcess() {
        return refundProcess;
    }

    public Money getConfirmedValue() {
        return refundProcess.getShouldSkipSupplierFundAllocation() ? Money.ZERO : getRequestItemValues();
    }

    public Money getUnconfirmedValue() {
        return refundProcess.getShouldSkipSupplierFundAllocation() ? Money.ZERO : getRequestItemValues();
    }

    private Money getRequestItemValues() {
        Money result = Money.ZERO;
        for (final RefundableInvoiceFile invoiceFile : supplier.getRefundInvoicesSet()) {
            final RefundProcess refundProcess = invoiceFile.getRefundItem().getRequest().getProcess();
            if (refundProcess == this.refundProcess) {
                final RefundItem refundItem = invoiceFile.getRefundItem();
                if (refundItem.getCPVReference() == cpvReference) {
                    result = result.add(invoiceFile.getRefundableValue());
                }
            }
        }
        return result;
    }

    @Override
    public int compareTo(final RefundForSupplierAndCPVBean o) {
        final RefundProcess otherProcess = o.getRefundProcess();
        return PaymentProcess.COMPARATOR_BY_YEAR_AND_ACQUISITION_PROCESS_NUMBER.compare(refundProcess, otherProcess);
    }

}
