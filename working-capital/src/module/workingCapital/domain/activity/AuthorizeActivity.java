package module.workingCapital.domain.activity;

import java.math.BigDecimal;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalInitializationReenforcement;
import module.workingCapital.domain.WorkingCapitalProcess;
import module.workingCapital.domain.WorkingCapitalRequest;
import module.workingCapital.domain.util.PaymentMethod;
import myorg.domain.User;
import myorg.domain.util.Money;
import myorg.util.BundleUtil;

public class AuthorizeActivity extends WorkflowActivity<WorkingCapitalProcess, WorkingCapitalInitializationInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected()
		&& workingCapital.isPendingAuthorization(user)
		// && workingCapital.getWorkingCapitalRequestsSet().isEmpty()
		;
    }

    @Override
    protected void process(final WorkingCapitalInitializationInformation activityInformation) {
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	final User user = getLoggedPerson();
	workingCapitalInitialization.authorize(user);

	final WorkingCapitalProcess workingCapitalProcess = activityInformation.getProcess();
	final WorkingCapital workingCapital = workingCapitalProcess.getWorkingCapital();

	final Money maxAnualValue = workingCapitalInitialization.getMaxAuthorizedAnualValue();
	Money requestedValue;
	if (workingCapitalInitialization instanceof WorkingCapitalInitializationReenforcement) {
	    final WorkingCapitalInitializationReenforcement workingCapitalInitializationReenforcement =
			(WorkingCapitalInitializationReenforcement) workingCapitalInitialization;
	    requestedValue = workingCapitalInitializationReenforcement.getAuthorizedReenforcementValue();
	} else {
	    requestedValue = workingCapitalInitialization.getAuthorizedAnualValue();
//	    requestedValue = maxAnualValue.divideAndRound(new BigDecimal(6));
	}
	final Money anualValue = workingCapitalInitialization.getMaxAuthorizedAnualValue();
	final Money possibaySpent = workingCapital.getPossibaySpent();
	final Money maxAllocatableValue = anualValue.subtract(possibaySpent);
	if (requestedValue.isGreaterThan(maxAllocatableValue)) {
	    requestedValue = maxAllocatableValue;
	}
	final PaymentMethod paymentMethod = workingCapitalInitialization.getInternationalBankAccountNumber() == null  
		|| workingCapitalInitialization.getInternationalBankAccountNumber().isEmpty() ? 
			PaymentMethod.CHECK : PaymentMethod.WIRETRANSFER;
	new WorkingCapitalRequest(workingCapital, requestedValue, paymentMethod);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new WorkingCapitalInitializationInformation(process, this);
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
	return true;
    }

    @Override
    public boolean isVisible() {
	return false;
    }

}
