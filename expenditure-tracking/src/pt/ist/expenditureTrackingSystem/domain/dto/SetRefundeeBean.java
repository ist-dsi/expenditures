package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SetRefundeeBean implements Serializable {

    private Person refundee;
    private AcquisitionProcess acquisitionProcess;

    public SetRefundeeBean(final Person refundee) {
	setRefundee(refundee);
    }

    public SetRefundeeBean(final AcquisitionProcess acquisitionProcess) {
	this(acquisitionProcess.getAcquisitionRequest().getRefundee());
	setAcquisitionProcess(acquisitionProcess);
    }

    public Person getRefundee() {
	return refundee;
    }

    public void setRefundee(final Person refundee) {
	this.refundee = refundee;
    }

    public AcquisitionProcess getAcquisitionProcess() {
	return acquisitionProcess;
    }

    public void setAcquisitionProcess(final AcquisitionProcess acquisitionProcess) {
	this.acquisitionProcess = acquisitionProcess;
    }

}
