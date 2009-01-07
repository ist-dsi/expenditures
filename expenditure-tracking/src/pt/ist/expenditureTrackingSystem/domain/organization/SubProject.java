package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Acquisition;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;

public class SubProject extends SubProject_Base {
    
    public SubProject(final Project parentUnit, final String name) {
        super();
    	setName(name);
    	setParentUnit(parentUnit);
    }

    @Override
    public String getPresentationName() {
	return getParentUnit().getPresentationName() + " - " + super.getPresentationName();
    }

    @Override
    public AccountingUnit getAccountingUnit() {
	final AccountingUnit accountingUnit = super.getAccountingUnit();
	return accountingUnit == null ? getParentUnit().getAccountingUnit() : accountingUnit;
    }

    @Override
    public Financer finance(final Acquisition acquisitionRequest) {
	return new ProjectFinancer(acquisitionRequest, this);
    }

}
