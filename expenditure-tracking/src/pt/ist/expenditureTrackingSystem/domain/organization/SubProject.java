package pt.ist.expenditureTrackingSystem.domain.organization;

import module.organizationIst.domain.IstPartyType;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;

public class SubProject extends SubProject_Base {

    public SubProject(final Project parentUnit, final String name) {
	super();
	final String acronym = StringUtils.abbreviate(name, 5);
	createRealUnit(this, parentUnit, IstPartyType.SUB_PROJECT, acronym, name);

	// TODO : After this object is refactored to retrieve the name and parent from the real unit,
	//        the following two lines may be deleted.
	setName(name);
	setParentUnit(parentUnit);
    }

    @Override
    public void setName(final String name) {
        super.setName(name);
        final String acronym = StringUtils.abbreviate(name, 5);
        getUnit().setAcronym(acronym);
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
    public Financer finance(final RequestWithPayment acquisitionRequest) {
	return new ProjectFinancer(acquisitionRequest, this);
    }

}
