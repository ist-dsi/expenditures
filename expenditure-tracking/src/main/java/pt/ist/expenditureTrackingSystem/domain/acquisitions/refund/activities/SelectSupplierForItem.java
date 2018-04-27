package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.math.BigDecimal;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem._development.Bundle;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.DomainException;

public class SelectSupplierForItem extends WorkflowActivity<RefundProcess, SelectSupplierForItemInformation> {

    @Override
    public boolean isActive(final RefundProcess process, final User user) {
        return process.getRequestor() == user.getExpenditurePerson()
                && !process.isCanceled()
                && isUserProcessOwner(process, user)
                && missingInvoices(process);
    }

    @Override
    protected void process(final SelectSupplierForItemInformation activityInformation) {
        final RefundItem item = activityInformation.getItem();
        final Supplier supplier = activityInformation.getSupplier();
        if (supplier != null) {
            check(item, supplier, item.getValueEstimation(), item.getVatValue(), item.getValueEstimation());
        }
        item.setSupplier(supplier);
    }

    private void check(RequestItem item, Supplier supplier, Money value, BigDecimal vatValue, Money refundableValue) {
        RefundProcess process = item.getRequest().getProcess();
        if (!process.getShouldSkipSupplierFundAllocation() && isFundAllocationAllowed(supplier, item.getCPVReference(), value)) {
            throw new DomainException(Bundle.ACQUISITION, "acquisitionRequestItem.message.exception.fundAllocationNotAllowed");
        }
        Money realValue = item.getRealValue();
        Money estimatedValue = item.getValue();

        if ((realValue != null && realValue.add(refundableValue).isGreaterThan(estimatedValue)) || realValue == null
                && refundableValue.isGreaterThan(estimatedValue.round())) {
            throw new DomainException(Bundle.ACQUISITION, "refundItem.message.info.realValueLessThanRefundableValue");
        }

        if (new Money(value.addPercentage(vatValue).getRoundedValue()).isLessThan(refundableValue)) {
            throw new DomainException(Bundle.ACQUISITION, "refundItem.message.info.refundableValueCannotBeBiggerThanInvoiceValue");
        }
    }

    private boolean isFundAllocationAllowed(final Supplier supplier, final CPVReference cpvReference, final Money value) {
        return ExpenditureTrackingSystem.getInstance().checkSupplierLimitsByCPV() ? !supplier.isFundAllocationAllowed(
                cpvReference.getCode(), value) : !supplier.isFundAllocationAllowed(value);
    }

    @Override
    public ActivityInformation<RefundProcess> getActivityInformation(RefundProcess process) {
        return new SelectSupplierForItemInformation(process, this);
    }

    @Override
    public boolean isVisible() {
        return false;
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
    public boolean isDefaultInputInterfaceUsed() {
        return true;
    }

    @Override
    public boolean isUserAwarenessNeeded(RefundProcess process, User user) {
        return false;
    }

    private boolean missingInvoices(final RefundProcess process) {
        final RefundRequest request = process.getRequest();
        return request.getRequestItemsSet().stream().anyMatch(i -> i.getInvoicesFilesSet().isEmpty());
    }

}
