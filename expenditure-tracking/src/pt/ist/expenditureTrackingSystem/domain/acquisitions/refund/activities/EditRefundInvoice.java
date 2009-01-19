package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities;

import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.dto.EditRefundInvoiceBean;

public class EditRefundInvoice extends GenericRefundProcessActivity {

    @Override
    protected boolean isAccessible(RefundProcess process) {
	return isCurrentUserProcessOwner(process) || userHasRole(RoleType.ACCOUNTING_MANAGER)
		|| userHasRole(RoleType.PROJECT_ACCOUNTING_MANAGER);
    }

    @Override
    protected boolean isAvailable(RefundProcess process) {
	return (isCurrentUserProcessOwner(process) && process.isInAuthorizedState())
		|| process.isPendingInvoicesConfirmation()
		&& ((userHasRole(RoleType.ACCOUNTING_MANAGER) && !process.hasProjectsAsPayingUnits()) || (userHasRole(RoleType.PROJECT_ACCOUNTING_MANAGER) && process
			.hasProjectsAsPayingUnits()));
    }

    @Override
    protected void process(RefundProcess process, Object... objects) {
	List<EditRefundInvoiceBean> beans = (List<EditRefundInvoiceBean>) objects[0];
	
	beans.get(0).getInvoice().resetValues();
	
	for (EditRefundInvoiceBean bean : beans) {
	    bean.getInvoice().editValues(bean.getValue(), bean.getVatValue(), bean.getRefundableValue());
	}
    }

}
