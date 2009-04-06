package pt.ist.expenditureTrackingSystem.domain.organization;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.dto.AccountingUnitBean;
import pt.ist.fenixWebFramework.services.Service;

public class AccountingUnit extends AccountingUnit_Base {

    public AccountingUnit() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    @Service
    public static AccountingUnit createNewAccountingUnit(final AccountingUnitBean accountingUnitBean) {
	final AccountingUnit accountingUnit = new AccountingUnit();
	accountingUnit.setName(accountingUnitBean.getName());
	return accountingUnit;
    }

    @Service
    @Override
    public void addPeople(final Person people) {
	super.addPeople(people);
    }

    @Service
    @Override
    public void addProjectAccountants(final Person people) {
	super.addProjectAccountants(people);
    }

    @Service
    @Override
    public void addTreasuryMembers(Person treasuryMembers) {
        super.addTreasuryMembers(treasuryMembers);
    }

    @Service
    @Override
    public void removePeople(final Person people) {
	super.removePeople(people);
    }

    @Service
    @Override
    public void removeProjectAccountants(final Person people) {
	super.removeProjectAccountants(people);
    }

    @Service
    @Override
    public void removeTreasuryMembers(Person treasuryMembers) {
        super.removeTreasuryMembers(treasuryMembers);
    }

    @Service
    @Override
    public void addUnits(final Unit unit) {
	super.addUnits(unit);
    }

    public static AccountingUnit readAccountingUnitByUnitName(final String name) {
	for (AccountingUnit accountingUnit : ExpenditureTrackingSystem.getInstance().getAccountingUnitsSet()) {
	    if (accountingUnit.getName().equals(name)) {
		return accountingUnit;
	    }
	}
	return null;
    }

}
