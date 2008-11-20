package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.util.DomainReference;

public class SetRefundeeBean implements Serializable {

    private DomainReference<Person> refundee;
    private DomainReference<AcquisitionProcess> acquisitionProcess;

    public SetRefundeeBean(final Person refundee) {
	setRefundee(refundee);
    }

    public SetRefundeeBean(final AcquisitionProcess acquisitionProcess) {
	this(acquisitionProcess.getAcquisitionRequest().getRefundee());
	setAcquisitionProcess(acquisitionProcess);
    }

    public Person getRefundee() {
        return refundee == null ? null : refundee.getObject();
    }

    public void setRefundee(final Person refundee) {
        this.refundee = refundee == null ? null : new DomainReference<Person>(refundee);
    }

    public AcquisitionProcess getAcquisitionProcess() {
        return acquisitionProcess == null ? null : acquisitionProcess.getObject();
    }

    public void setAcquisitionProcess(final AcquisitionProcess acquisitionProcess) {
        this.acquisitionProcess = acquisitionProcess == null ? null : new DomainReference<AcquisitionProcess>(acquisitionProcess);
    }

}
