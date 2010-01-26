package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;

public class ChangeFinancerAccountingUnitBean implements Serializable {
    private Financer financer;
    private AccountingUnit accountingUnit;

    public ChangeFinancerAccountingUnitBean(final Financer financer, final AccountingUnit accountingUnit) {
	setFinancer(financer);
	setAccountingUnit(accountingUnit);
    }

    public void setFinancer(Financer financer) {
	this.financer = financer;
    }

    public Financer getFinancer() {
	return this.financer;
    }

    public void setAccountingUnit(AccountingUnit accountingUnit) {
	this.accountingUnit = accountingUnit;
    }

    public AccountingUnit getAccountingUnit() {
	return this.accountingUnit;
    }

}
