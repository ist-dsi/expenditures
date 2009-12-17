package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import java.util.List;

import module.workflow.activities.WorkflowActivity;
import myorg.domain.User;
import myorg.util.BundleUtil;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess.ProcessClassification;
import pt.ist.expenditureTrackingSystem.domain.dto.UnitItemBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class GenericAssignPayingUnitToItem<P extends PaymentProcess> extends
	WorkflowActivity<P, GenericAssignPayingUnitToItemActivityInformation<P>> {

    @Override
    public boolean isActive(P process, User user) {
	Person person = user.getExpenditurePerson();
	return isUserProcessOwner(process, user)
		&& (person == process.getRequestor() && process.isInGenesis())
		|| ((process instanceof SimplifiedProcedureProcess)
			&& ((SimplifiedProcedureProcess) process).getProcessClassification() == ProcessClassification.CT75000
			&& person.hasRoleType(RoleType.ACQUISITION_CENTRAL) && process.isAuthorized());
    }

    @Override
    protected void process(GenericAssignPayingUnitToItemActivityInformation<P> activityInformation) {
	RequestItem item = activityInformation.getItem();
	List<UnitItemBean> beans = activityInformation.getBeans();

	for (; !item.getUnitItems().isEmpty(); item.getUnitItems().get(0).delete())
	    ;

	for (UnitItemBean bean : beans) {
	    if (bean.getAssigned()) {
		item.createUnitItem(bean.getUnit(), bean.getShareValue());
	    }
	}
    }

    public GenericAssignPayingUnitToItemActivityInformation<P> getActivityInformation(P process) {
	return new GenericAssignPayingUnitToItemActivityInformation<P>(process, this);
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

    @Override
    public boolean isVisible() {
	return false;
    }
}
