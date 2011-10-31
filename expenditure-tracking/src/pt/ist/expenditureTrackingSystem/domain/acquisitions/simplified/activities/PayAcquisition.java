package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.Set;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class PayAcquisition<P extends RegularAcquisitionProcess>
	extends WorkflowActivity<P, PayAcquisitionActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& process.getAcquisitionProcessState().isAllocatedPermanently()
		&& (ExpenditureTrackingSystem.isTreasuryMemberGroupMember(user)
			|| process.isTreasuryMember(person));
    }

    @Override
    protected void process(PayAcquisitionActivityInformation<P> activityInformation) {
	P process = activityInformation.getProcess();
	process.getAcquisitionRequest().setPaymentReference(activityInformation.getPaymentReference());
//	for (final PaymentReferenceBean bean : activityInformation.getBeans()) {
//	    final Financer financer = bean.getFinancer();
//	    final String diaryNumber = bean.getDiaryNumber();
//	    financer.addPaymentDiaryNumber(diaryNumber);
//	}
//	if (hasAllDiaryNumbers(process)) {
	    process.acquisitionPayed();
//	}
    }

    private boolean hasAllDiaryNumbers(final P process) {
	final Set<Financer> financers = process.getFinancersWithFundsAllocated();
	if (financers.isEmpty()) {
	    return false;
	}
	for (final Financer financer : financers) {
	    if (financer.getPaymentDiaryNumber() == null || financer.getPaymentDiaryNumber().isEmpty()) {
		return false;
	    }
	    if (financer.getTransactionNumber() == null || financer.getTransactionNumber().isEmpty()) {
		return false;
	    }
	    
	}
	return true;
    }

    @Override
    public PayAcquisitionActivityInformation<P> getActivityInformation(P process) {
	return new PayAcquisitionActivityInformation<P>(process, this);
    }

    @Override
    public String getLocalizedName() {
	return BundleUtil.getStringFromResourceBundle(getUsedBundle(), "label." + getClass().getName());
    }

    @Override
    public String getUsedBundle() {
	return "resources/AcquisitionResources";
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
