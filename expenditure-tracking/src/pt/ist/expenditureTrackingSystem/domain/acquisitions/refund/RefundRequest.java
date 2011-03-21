package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.CPVReference;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class RefundRequest extends RefundRequest_Base {

    public RefundRequest(RefundProcess process, Person requestor, String refundeeName, String refundeeFiscalCode,
	    Unit requestingUnit) {
	super();
	setProcess(process);
	Refundee refundee = Refundee.getExternalRefundee(refundeeName, refundeeFiscalCode);
	setRefundee(refundee == null ? new Refundee(refundeeName, refundeeFiscalCode) : refundee);
	setRequester(requestor);
	setRequestingUnit(requestingUnit);
    }

    public RefundRequest(RefundProcess process, Person requestor, Person refundeePerson, Unit requestingUnit) {
	super();
	setProcess(process);
	Refundee refundee = refundeePerson.hasRefundee() ? refundeePerson.getRefundee() : new Refundee(refundeePerson);
	setRefundee(refundee);
	setRequester(requestor);
	setRequestingUnit(requestingUnit);
    }

    public void createRefundItem(Money valueEstimation, CPVReference reference, String description) {
	RefundItem refundItem = new RefundItem(this, valueEstimation, reference, description);
	List<Unit> payingUnits = this.getProcess().getPayingUnits();
	if (payingUnits.size() == 1) {
	    refundItem.createUnitItem(payingUnits.get(0), valueEstimation);
	}
    }

    public boolean isEveryItemFullyAttributedToPayingUnits() {
	for (RefundItem item : getRefundItemsSet()) {
	    if (!item.isValueFullyAttributedToUnits()) {
		return false;
	    }
	}
	return true;
    }

    public boolean isApprovedByAtLeastOneResponsible() {
	for (RefundItem item : getRefundItemsSet()) {
	    if (item.hasAtLeastOneResponsibleApproval()) {
		return true;
	    }
	}
	return false;
    }

    public Set<RefundItem> getRefundItemsSet() {
	Set<RefundItem> refundItems = new HashSet<RefundItem>();
	for (RequestItem item : getRequestItems()) {
	    refundItems.add((RefundItem) item);
	}
	return refundItems;
    }

    public String getAcquisitionProcessId() {
	return getProcess().getAcquisitionProcessId();
    }

    public boolean isPayed() {
	final String reference = getPaymentReference();
	return reference != null && !reference.isEmpty();
    }

    public Set<Supplier> getSuppliers() {
	final Set<Supplier> suppliers = new HashSet<Supplier>();
	for (final RefundItem refundItem : getRefundItemsSet()) {
	    refundItem.getSuppliers(suppliers);
	}
	return suppliers;
    }

    @Override
    public SortedSet<RefundItem> getOrderedRequestItemsSet() {
	SortedSet<RefundItem> set = new TreeSet<RefundItem>(RefundItem.COMPARATOR);
	set.addAll(getRefundItemsSet());
	return set;
    }

    public Money getCurrentTotalValue() {
	final Money realTotalValue = getRealTotalValue();
	return realTotalValue == null ? getTotalValue() : realTotalValue;
    }

    public Set<CPVReference> getCPVReferences() {
	final Set<CPVReference> result = new HashSet<CPVReference>();
	for (final RequestItem requestItem : getRequestItemsSet()) {
	    result.add(requestItem.getCPVReference());
	}
	return result;
    }

}
