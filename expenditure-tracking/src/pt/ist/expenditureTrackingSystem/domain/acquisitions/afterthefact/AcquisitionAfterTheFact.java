package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class AcquisitionAfterTheFact extends AcquisitionAfterTheFact_Base {

    public AcquisitionAfterTheFact(final AfterTheFactAcquisitionProcess afterTheFactAcquisitionProcess) {
	super();
	setAfterTheFactAcquisitionProcess(afterTheFactAcquisitionProcess);
    }

    public void edit(final AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean) {
	setAfterTheFactAcquisitionType(afterTheFactAcquisitionProcessBean.getAfterTheFactAcquisitionType());
	setValue(afterTheFactAcquisitionProcessBean.getValue());
	setVatValue(afterTheFactAcquisitionProcessBean.getVatValue());
	setSupplier(afterTheFactAcquisitionProcessBean.getSupplier());
	setDescription(afterTheFactAcquisitionProcessBean.getDescription());
	setDeletedState(Boolean.FALSE);
    }

    @Override
    public void delete() {
	setDeletedState(Boolean.TRUE);
    }

    public String getAcquisitionProcessId() {
	return getAfterTheFactAcquisitionProcess().getAcquisitionProcessId();
    }

    @Override
    public void setSupplier(final Supplier supplier) {
	if (supplier != getSupplier()) {
	    if (getValue() != null && !supplier.isFundAllocationAllowed(getValue())) {
		throw new DomainException("acquisitionProcess.message.exception.SupplierDoesNotAlloweAmount");
	    }
	    super.setSupplier(supplier);
	}
    }

    @Override
    public void setValue(final Money value) {
	if (getSupplier() != null && !getSupplier().isFundAllocationAllowed(Money.ZERO)) {
	    throw new DomainException("acquisitionProcess.message.exception.SupplierDoesNotAlloweAmount");
	}
	super.setValue(value);
    }

}
