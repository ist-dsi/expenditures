package module.workingCapital.domain.activity;

import org.joda.time.DateTime;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapital;
import module.workingCapital.domain.WorkingCapitalInitialization;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

public class EditInitializationActivity extends WorkflowActivity<WorkingCapitalProcess, EditInitializationActivityInformation> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/WorkingCapitalResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final WorkingCapitalProcess missionProcess, final User user) {
	final WorkingCapital workingCapital = missionProcess.getWorkingCapital();
	return !workingCapital.isCanceledOrRejected()
		&& workingCapital.isPendingAproval()
		&& workingCapital.isRequester(user);
    }

    @Override
    protected void process(final EditInitializationActivityInformation activityInformation) {
	final WorkingCapitalInitialization workingCapitalInitialization = activityInformation.getWorkingCapitalInitialization();
	workingCapitalInitialization.getWorkingCapital().setMovementResponsible(activityInformation.getMovementResponsible());
	workingCapitalInitialization.setRequestedAnualValue(activityInformation.getRequestedAnualValue());
	workingCapitalInitialization.setFiscalId(activityInformation.getFiscalId());
	workingCapitalInitialization.setAcceptedResponsability(null);

	final String banOrIban = activityInformation.getInternationalBankAccountNumber();
	final String internationalBankAccountNumber = banOrIban == null || banOrIban.isEmpty() || !Character.isDigit(banOrIban.charAt(0))
		? banOrIban : "PT50" + banOrIban;

	workingCapitalInitialization.setInternationalBankAccountNumber(internationalBankAccountNumber);
    }

    @Override
    public ActivityInformation<WorkingCapitalProcess> getActivityInformation(final WorkingCapitalProcess process) {
        return new EditInitializationActivityInformation(process, this);
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
