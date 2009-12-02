package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import myorg.domain.util.Money;
import myorg.domain.exceptions.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.fenixWebFramework.services.Service;

public class CPVReference extends CPVReference_Base {

    public CPVReference(String code, String description) {
	checkParameters(code, description);

	setCode(code);
	setDescription(description);
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    private void checkParameters(String code, String description) {
	if (code == null || description == null) {
	    throw new DomainException("error.code.and.description.are.required");
	}
	if (getCPVCode(code) != null) {
	    throw new DomainException("error.cpv.code.already.exists");
	}
    }

    public static CPVReference getCPVCode(String code) {
	for (CPVReference reference : ExpenditureTrackingSystem.getInstance().getCPVReferences()) {
	    if (reference.getCode().equals(code)) {
		return reference;
	    }
	}
	return null;
    }

    public String getFullDescription() {
	return getCode() + " - " + getDescription();
    }

    public Money getTotalAmountAllocated(final int year) {
	Money money = Money.ZERO;
	for (final RequestItem requestItem : getAcquisitionItemsSet()) {
	    money = money.add(requestItem.getTotalAmountForCPV(year));
	}
	return money;
    }

    public boolean isPriorityCode() {
	return getExpenditureTrackingSystemForPriorities() != null;
    }

    @Service
    public void markAsPriority() {
	setExpenditureTrackingSystemForPriorities(ExpenditureTrackingSystem.getInstance());
    }

    @Service
    public void unmarkAsPriority() {
	setExpenditureTrackingSystemForPriorities(null);
    }
}
