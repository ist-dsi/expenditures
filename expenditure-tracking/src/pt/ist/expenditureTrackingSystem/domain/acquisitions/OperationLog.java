package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

import myorg.domain.exceptions.DomainException;

public class OperationLog extends OperationLog_Base {

    public OperationLog(AcquisitionProcess process, User user, String operation, AcquisitionProcessStateType type) {

	super();
	init(process, user);
	super.setOperation(operation);
	super.setState(type);
    }

    // TODO: Should we have this or not?

    // @Override
    // public <T extends GenericProcess> AbstractActivity<T> getActivity() {
    // AcquisitionProcess process = (AcquisitionProcess) getProcess();
    // return process.getActivityByName(getOperation());
    // }

    @Override
    public void setOperation(String operation) {
	throw new DomainException("error.unable.to.change.operation");
    }

    @Override
    public void setProcess(WorkflowProcess process) {
	throw new DomainException("error.unable.to.change.process");
    }

    @Override
    public void setActivityExecutor(User executor) {
	throw new DomainException("error.unable.to.change.executor");
    }

    @Override
    public void setWhenOperationWasRan(DateTime when) {
	throw new DomainException("error.unable.to.change.when.operation.was.executed");
    }

    public void delete() {
	super.setActivityExecutor(null);
	super.setProcess(null);
	deleteDomainObject();
    }

    @Override
    public String getDescription() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources.AcquisitionResources", "label." + getOperation());
    }
}
