package module.workingCapital.domain.activity;

import java.util.HashSet;
import java.util.Set;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class ChangeAccountingUnitActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private AccountingUnit accountingUnit;

    public ChangeAccountingUnitActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
	final WorkingCapitalProcess process = getProcess();
	final WorkingCapital workingCapital = process.getWorkingCapital();
	accountingUnit = workingCapital.getAccountingUnit();
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && super.hasAllneededInfo() && accountingUnit != null;
    }

    public AccountingUnit getAccountingUnit() {
        return accountingUnit;
    }

    public void setAccountingUnit(AccountingUnit accountingUnit) {
        this.accountingUnit = accountingUnit;
    }

    public Object getAccountingUnits() {
	final Set<AccountingUnit> res = new HashSet<AccountingUnit>();
	final WorkingCapitalProcess process = getProcess();
	final WorkingCapital workingCapital = process.getWorkingCapital();
	final Unit unit = workingCapital.getUnit();
	final AccountingUnit accountingUnit = unit.getAccountingUnit();
	if (accountingUnit != null) {
	    res.add(accountingUnit);
	}
	res.add(AccountingUnit.readAccountingUnitByUnitName("10"));
	return res;
    }

}
