package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.math.BigDecimal;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixframework.pstm.Transaction;

public class AcquisitionRequest extends AcquisitionRequest_Base {

    AcquisitionRequest(final AcquisitionProcess acquisitionProcess) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setAcquisitionProcess(acquisitionProcess);

	final User user = UserView.getUser();
	if (user == null || user.getPerson() == null) {
	    throw new DomainException("error.anonymous.creation.of.acquisition.request.information.not.allowed");
	}
	setRequester(user.getPerson());
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

    public void delete() {
	for (AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    acquisitionRequestItem.delete();
	}
	removeAcquisitionProposalDocument();
	removeRequester();
	removeSupplier();
	removeAcquisitionProcess();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public String getFiscalIdentificationCode() {
	return getSupplier() != null ? getSupplier().getFiscalIdentificationCode() : null;
    }

    public void setFiscalIdentificationCode(String fiscalIdentificationCode) {
	Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(fiscalIdentificationCode);
	if (supplier == null) {
	    supplier = new Supplier(fiscalIdentificationCode);
	}
	setSupplier(supplier);
    }

    public BigDecimal getTotalItemValue() {
	BigDecimal result = BigDecimal.ZERO;
	for (final AcquisitionRequestItem acquisitionRequestItem : getAcquisitionRequestItemsSet()) {
	    result = result.add(acquisitionRequestItem.getTotalItemValue());
	}
	return result;
    }

}
