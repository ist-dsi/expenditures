package module.workingCapital.domain.activity;

import java.math.BigDecimal;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.util.Money;

public class EditInitializationActivityInformation extends WorkingCapitalInitializationInformation {

    private Person movementResponsible;
    private Money requestedMonthlyValue;
    private String fiscalId;
    private String internationalBankAccountNumber;

    public EditInitializationActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && super.hasAllneededInfo();
    }

    @Override
    public void setWorkingCapitalInitialization(final WorkingCapitalInitialization workingCapitalInitialization) {
	super.setWorkingCapitalInitialization(workingCapitalInitialization);
	if (workingCapitalInitialization != null) {
	    movementResponsible = workingCapitalInitialization.getWorkingCapital().getMovementResponsible();
	    requestedMonthlyValue = workingCapitalInitialization.getRequestedAnualValue().divideAndRound(new BigDecimal(12));
	    fiscalId = workingCapitalInitialization.getFiscalId();
	    internationalBankAccountNumber = workingCapitalInitialization.getInternationalBankAccountNumber();
	}
    }

    public Person getMovementResponsible() {
	return movementResponsible;
    }

    public void setMovementResponsible(Person movementResponsible) {
	this.movementResponsible = movementResponsible;
    }

    public Money getRequestedMonthlyValue() {
	return requestedMonthlyValue;
    }

    public void setRequestedMonthlyValue(Money requestedMonthlyValue) {
	this.requestedMonthlyValue = requestedMonthlyValue;
    }

    public String getFiscalId() {
	return fiscalId;
    }

    public void setFiscalId(String fiscalId) {
	this.fiscalId = fiscalId;
    }

    public String getInternationalBankAccountNumber() {
	return internationalBankAccountNumber;
    }

    public void setInternationalBankAccountNumber(String internationalBankAccountNumber) {
	this.internationalBankAccountNumber = internationalBankAccountNumber;
    }

}
