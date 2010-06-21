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
    private Money authorizedReenforcementValue;
    private String fundAllocationId;

    public VerifyActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public void setWorkingCapitalInitialization(final WorkingCapitalInitialization workingCapitalInitialization) {
        super.setWorkingCapitalInitialization(workingCapitalInitialization);
        if (workingCapitalInitialization != null) {
            maxAuthorizedAnualValue = workingCapitalInitialization.getRequestedAnualValue();
            if (workingCapitalInitialization instanceof WorkingCapitalInitializationReenforcement) {
        	final WorkingCapitalInitializationReenforcement workingCapitalInitializationReenforcement =
        	    	(WorkingCapitalInitializationReenforcement) workingCapitalInitialization;
        	//maxAuthorizedAnualValue = maxAuthorizedAnualValue.add(workingCapitalInitializationReenforcement.getRequestedReenforcementValue());
        	authorizedAnualValue = workingCapitalInitialization.getAuthorizedAnualValue();
        	authorizedReenforcementValue = workingCapitalInitializationReenforcement.getRequestedReenforcementValue();
            } else {
        	authorizedAnualValue = maxAuthorizedAnualValue.multiply(new BigDecimal(2)).divideAndRound(new BigDecimal(12));
            }
            fundAllocationId = workingCapitalInitialization.getFundAllocationId();
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

    public Money getAuthorizedReenforcementValue() {
        return authorizedReenforcementValue;
    }

    public void setAuthorizedReenforcementValue(Money authorizedReenforcementValue) {
        this.authorizedReenforcementValue = authorizedReenforcementValue;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput()
		&& super.hasAllneededInfo()
		&& authorizedAnualValue != null
		&& maxAuthorizedAnualValue != null;
    }

    public String getFundAllocationId() {
        return fundAllocationId;
    }

    public void setFundAllocationId(String fundAllocationId) {
        this.fundAllocationId = fundAllocationId;
    }

}
