package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;
import java.util.Comparator;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequestInformation extends AcquisitionRequestInformation_Base {

    public static final Comparator<AcquisitionRequestInformation> COMPARATOR_BY_SINCE = new Comparator<AcquisitionRequestInformation>() {
	@Override
	public int compare(AcquisitionRequestInformation o1, AcquisitionRequestInformation o2) {
	    return o1.getSince().compareTo(o2.getSince());
	}};

    AcquisitionRequestInformation(final AcquisitionRequest acquisitionRequest) {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setAcquisitionRequest(acquisitionRequest);
        final User user = UserView.getUser();
        if (user == null) {
            throw new DomainException("error.anonymous.creation.of.acquisition.request.information.not.allowed");
        }
        setRequester(user.getUsername());
    }

    public void addAcquisitionProposalDocument(final String filename, final byte[] bytes) {
	AcquisitionProposalDocument acquisitionProposalDocument = getAcquisitionProposalDocument();
	if (acquisitionProposalDocument == null) {
	    acquisitionProposalDocument = new AcquisitionProposalDocument();
	    setAcquisitionProposalDocument(acquisitionProposalDocument);
	}
	acquisitionProposalDocument.setFilename(filename);
	acquisitionProposalDocument.setContent(new ByteArray(bytes));
    }

    public AcquisitionRequestItem createAcquisitionRequestItem() {
	return new AcquisitionRequestItem(this);
    }

    public AcquisitionProcess getAcquisitionProcess() {
	final AcquisitionRequest acquisitionRequest = getAcquisitionRequest();
	return acquisitionRequest.getAcquisitionProcess();
    }

    public BigDecimal getTotalItemValue() {
	BigDecimal result = new BigDecimal(0);
	for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    result = result.add(acquisitionRequestItem.getTotalItemValue());
	}
	return result;
    }

    public void delete() {
	final AcquisitionProposalDocument acquisitionProposalDocument =getAcquisitionProposalDocument();
	if (acquisitionProposalDocument != null) {
	    if (acquisitionProposalDocument.getAcquisitionRequestInformationsCount() == 1) {
		acquisitionProposalDocument.delete();
	    } else {
		removeAcquisitionProposalDocument();
	    }
	}
	for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    if (acquisitionRequestItem.getAcquisitionRequestInformationsCount() == 1) {
		acquisitionRequestItem.delete();
	    } else {
		removeAcquisitionRequestItems(acquisitionRequestItem);
	    }
	}
	removeAcquisitionRequest();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
