package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import pt.ist.expenditureTrackingSystem.domain.dto.AfterTheFactAcquisitionProcessBean;

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

}
