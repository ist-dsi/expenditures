package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequest extends AcquisitionRequest_Base {
    
    AcquisitionRequest(final AcquisitionProcess acquisitionProcess) {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setAcquisitionProcess(acquisitionProcess);
        new AcquisitionRequestInformation(this);
    }

    public AcquisitionRequestInformation getAcquisitionRequestInformation() {
	final Set<AcquisitionRequestInformation> infomations = getAcquisitionRequestInformationsSet();
	final int count = infomations.size();
	if (count < 2) {
	    return count == 0 ? null : infomations.iterator().next();
	} else {
	    return Collections.max(getAcquisitionRequestInformationsSet(), AcquisitionRequestInformation.COMPARATOR_BY_SINCE);
	}
    }

    public void addAcquisitionProposalDocument(final String filename, final byte[] bytes) {
	final AcquisitionRequestInformation acquisitionRequestInformation = getAcquisitionRequestInformation();
	acquisitionRequestInformation.addAcquisitionProposalDocument(filename, bytes);
    }

    public AcquisitionRequestItem createAcquisitionRequestItem() {
	final AcquisitionRequestInformation acquisitionRequestInformation = getAcquisitionRequestInformation();
	return acquisitionRequestInformation.createAcquisitionRequestItem();
    }

    public void delete() {
	for (final AcquisitionRequestInformation acquisitionRequestInformation : getAcquisitionRequestInformationsSet()) {	    
	    acquisitionRequestInformation.delete();
	}
	removeAcquisitionProcess();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
