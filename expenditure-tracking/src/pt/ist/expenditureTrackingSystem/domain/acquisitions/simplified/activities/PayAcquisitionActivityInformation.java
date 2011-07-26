package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.util.ArrayList;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate.UserView;
import myorg.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.PaymentReferenceBean;

public class PayAcquisitionActivityInformation<P extends PaymentProcess>
	extends ActivityInformation<P> {

    String paymentReference;
    protected List<PaymentReferenceBean> beans;

    public PayAcquisitionActivityInformation(P process,
	    WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	beans = new ArrayList<PaymentReferenceBean>();
	for (final Financer financer : process.getFinancersWithFundsAllocated()) {
	    final User currentUser = UserView.getCurrentUser();
	    if (financer.isTreasuryMember(currentUser.getExpenditurePerson())) {
		beans.add(new PaymentReferenceBean(financer));
	    }
	}
    }

    public String getPaymentReference() {
	return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
	this.paymentReference = paymentReference;
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getPaymentReference() != null /* && hasPaymentRefences() */ ;
    }

    private boolean hasPaymentRefences() {
	for (final PaymentReferenceBean referenceBean : beans) {
	    if (referenceBean.getDiaryNumber() != null && !referenceBean.getDiaryNumber().isEmpty()) {
		return true;
	    }
	}
	return false;
    }

    public List<PaymentReferenceBean> getBeans() {
        return beans;
    }

    public void setBeans(List<PaymentReferenceBean> beans) {
        this.beans = beans;
    }

}
