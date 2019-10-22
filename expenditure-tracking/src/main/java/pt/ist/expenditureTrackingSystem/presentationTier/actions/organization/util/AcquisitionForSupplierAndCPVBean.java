package pt.ist.expenditureTrackingSystem.presentationTier.actions.organization.util;

import module.finance.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class AcquisitionForSupplierAndCPVBean implements Comparable<AcquisitionForSupplierAndCPVBean> {

    private final Supplier supplier;
    private final CPVReference cpvReference;
    private final AcquisitionProcess acquisitionProcess;

    public AcquisitionForSupplierAndCPVBean(final Supplier supplier, final CPVReference cpvReference,
            final AcquisitionProcess acquisitionProcess) {
        this.supplier = supplier;
        this.cpvReference = cpvReference;
        this.acquisitionProcess = acquisitionProcess;
    }

    public AcquisitionProcess getAcquisitionProcess() {
        return acquisitionProcess;
    }

    public Money getConfirmedValue() {
        return acquisitionProcess.isAllocatedToSupplier() ? getRequestItemValues() : Money.ZERO;
    }

    public Money getUnconfirmedValue() {
        return acquisitionProcess.getShouldSkipSupplierFundAllocation() ? Money.ZERO : getRequestItemValues();
    }

    private Money getRequestItemValues() {
        Money result = Money.ZERO;
        final AcquisitionRequest acquisitionRequest = acquisitionProcess.getAcquisitionRequest();
        for (final RequestItem requestItem : acquisitionRequest.getRequestItemsSet()) {
            final AcquisitionRequestItem acqRequestItem = (AcquisitionRequestItem) requestItem;
            if (acqRequestItem.getCPVReference() == cpvReference) {
                if (acquisitionProcess.getAcquisitionProcessState().hasBeenAllocatedPermanently()
                        && !acquisitionProcess.hasAdvancePaymentWithoutPaymentInfo()) {
                    result = result.add(acqRequestItem.getTotalRealValue());
                } else {
                    result = result.add(acqRequestItem.getTotalItemValue());
                }
            }
        }
        return result;
    }

    @Override
    public int compareTo(final AcquisitionForSupplierAndCPVBean o) {
        final AcquisitionProcess otherProcess = o.getAcquisitionProcess();
        return PaymentProcess.COMPARATOR_BY_YEAR_AND_ACQUISITION_PROCESS_NUMBER.compare(acquisitionProcess, otherProcess);
    }

}
