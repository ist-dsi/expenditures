package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.Set;

import myorg.domain.util.Money;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RCISTAnnouncement extends RCISTAnnouncement_Base {

    public RCISTAnnouncement(AcquisitionRequest request) {
	super();
	setAcquisition(request);
	setDescription(request.getContractSimpleDescription());
    }

    @Override
    public AcquisitionRequest getAcquisition() {
	return (AcquisitionRequest) super.getAcquisition();
    }

    @Override
    public Set<Unit> getBuyingUnits() {
	return getAcquisition().getPayingUnits();
    }

    @Override
    public Unit getRequestingUnit() {
	return getAcquisition().getRequestingUnit();
    }

    @Override
    public Supplier getSupplier() {
	return getAcquisition().getSelectedSupplier();
    }

    @Override
    public Money getTotalPrice() {
	return getAcquisition().getCurrentTotalItemValueWithAdditionalCostsAndVat();
    }

}
