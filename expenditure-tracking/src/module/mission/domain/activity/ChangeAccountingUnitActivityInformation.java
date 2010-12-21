package module.mission.domain.activity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class ChangeAccountingUnitActivityInformation extends ActivityInformation<MissionProcess> implements Serializable {

    private MissionFinancer financer;
    private AccountingUnit accountingUnit;

    public ChangeAccountingUnitActivityInformation(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getFinancer() != null && accountingUnit != null;
    }

    public MissionFinancer getFinancer() {
	return financer;
    }

    public void setFinancer(final MissionFinancer financer) {
	this.financer = financer;
	accountingUnit = financer.getAccountingUnit();
    }

    public AccountingUnit getAccountingUnit() {
        return accountingUnit;
    }

    public void setAccountingUnit(AccountingUnit accountingUnit) {
        this.accountingUnit = accountingUnit;
    }

    public Object getAccountingUnits() {
	final Set<AccountingUnit> result = new HashSet<AccountingUnit>();
	final Unit unit = financer.getUnit();
	if (unit != null) {
	    final AccountingUnit accountingUnit = unit.getAccountingUnit();
	    if (accountingUnit != null) {
		result.add(accountingUnit);
	    }
	}
	result.add(AccountingUnit.readAccountingUnitByUnitName("10"));
	return result;
    }

}
