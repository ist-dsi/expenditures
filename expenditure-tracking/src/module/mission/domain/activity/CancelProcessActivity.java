package module.mission.domain.activity;

import java.util.ResourceBundle;

import module.mission.domain.MissionProcess;
import module.workflow.activities.ActivityInformation;
import myorg.domain.RoleType;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class CancelProcessActivity extends MissionProcessActivity<MissionProcess, ActivityInformation<MissionProcess>> {

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle("resources/MissionResources", "activity." + getClass().getSimpleName());
    }

    @Override
    public boolean isActive(final MissionProcess missionProcess, final User user) {
	return super.isActive(missionProcess, user)
		// && missionProcess.isUnderConstruction()
		&& (missionProcess.isRequestor(user) || user.hasRoleType(RoleType.MANAGER))
		&& !missionProcess.isCanceled();
    }

    @Override
    protected void process(final ActivityInformation<MissionProcess> activityInformation) {
	final MissionProcess missionProcess = activityInformation.getProcess();

	if (hasConnectedPaymentProcess(missionProcess)) {
	    final String processes = getConnectedPaymentProcess(missionProcess);
	    throw new DomainException("error.mission.has.active.payment.processes.cannot.cancel", ResourceBundle.getBundle("resources/MissionResources", Language.getLocale()), processes);
	}

	missionProcess.cancel();
	missionProcess.addToProcessParticipantInformationQueue();
    }

    private boolean hasConnectedPaymentProcess(final MissionProcess missionProcess) {
	for (final PaymentProcess paymentProcess : missionProcess.getPaymentProcessSet()) {
	    if (!paymentProcess.isCanceled()) {
		return true;
	    }
	}
	return false;
    }

    private String getConnectedPaymentProcess(final MissionProcess missionProcess) {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final PaymentProcess paymentProcess : missionProcess.getPaymentProcessSet()) {
	    if (!paymentProcess.isCanceled()) {
		if (stringBuilder.length() > 0) {
		    stringBuilder.append(", ");
		}
		stringBuilder.append(paymentProcess.getAcquisitionProcessId());
	    }
	}
	return stringBuilder.toString();
    }

    @Override
    public ActivityInformation<MissionProcess> getActivityInformation(final MissionProcess process) {
	return new ActivityInformation<MissionProcess>(process, this);
    }

    @Override
    public boolean isUserAwarenessNeeded(MissionProcess process, User user) {
        return false;
    }

    @Override
    public boolean isConfirmationNeeded(final MissionProcess process) {
	return true;
    }

    @Override
    public String getLocalizedConfirmationMessage() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/MissionResources",
		"label.module.mission.cancel.process.confirm");
    }

    @Override
    public String getUsedBundle() {
	return "resources/MissionResources";
    }

}
