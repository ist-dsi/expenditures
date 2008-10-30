package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.fenixWebFramework.util.DomainReference;

public class ChangeFinancerAccountingUnitBean implements Serializable {
    private DomainReference<Financer> financer;
    private DomainReference<AccountingUnit> accountingUnit;

    public ChangeFinancerAccountingUnitBean(final Financer financer, final AccountingUnit accountingUnit) {
	setFinancer(financer);
	setAccountingUnit(accountingUnit);
    }

    public void setFinancer(Financer financer) {
	this.financer = new DomainReference<Financer>(financer);
    }

    public Financer getFinancer() {
	return this.financer.getObject();
    }

    public void setAccountingUnit(AccountingUnit accountingUnit) {
	this.accountingUnit = new DomainReference<AccountingUnit>(accountingUnit);
    }

    public AccountingUnit getAccountingUnit() {
	return this.accountingUnit.getObject();
    }

}
