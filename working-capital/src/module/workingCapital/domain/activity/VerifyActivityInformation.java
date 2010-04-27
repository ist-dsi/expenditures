package module.workingCapital.domain.activity;

import java.math.BigDecimal;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalInitializationReenforcement;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.util.Money;

public class VerifyActivityInformation extends WorkingCapitalInitializationInformation {

    private Money authorizedAnualValue;
    private Money maxAuthorizedAnualValue;

    public VerifyActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public void setWorkingCapitalInitialization(final WorkingCapitalInitialization workingCapitalInitialization) {
        super.setWorkingCapitalInitialization(workingCapitalInitialization);
        if (workingCapitalInitialization != null) {
            maxAuthorizedAnualValue = workingCapitalInitialization.getRequestedAnualValue();
            authorizedAnualValue = maxAuthorizedAnualValue.multiply(new BigDecimal(2)).divideAndRound(new BigDecimal(12));
            if (workingCapitalInitialization instanceof WorkingCapitalInitializationReenforcement) {
        	final WorkingCapitalInitializationReenforcement workingCapitalInitializationReenforcement =
        	    	(WorkingCapitalInitializationReenforcement) workingCapitalInitialization;
        	maxAuthorizedAnualValue = maxAuthorizedAnualValue.add(workingCapitalInitializationReenforcement.getRequestedReenforcementValue());
            }
        }
    }

    public Money getAuthorizedAnualValue() {
        return authorizedAnualValue;
    }

    public void setAuthorizedAnualValue(Money authorizedAnualValue) {
        this.authorizedAnualValue = authorizedAnualValue;
    }

    public Money getMaxAuthorizedAnualValue() {
        return maxAuthorizedAnualValue;
    }

    public void setMaxAuthorizedAnualValue(Money maxAuthorizedAnualValue) {
        this.maxAuthorizedAnualValue = maxAuthorizedAnualValue;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput()
		&& super.hasAllneededInfo()
		&& authorizedAnualValue != null
		&& maxAuthorizedAnualValue != null;
    }

}
