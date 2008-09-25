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
    public void removePeople(final Person people) {
        super.removePeople(people);
    }

}
