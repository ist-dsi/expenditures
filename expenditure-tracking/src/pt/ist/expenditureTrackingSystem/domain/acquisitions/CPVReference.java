package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

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
}
