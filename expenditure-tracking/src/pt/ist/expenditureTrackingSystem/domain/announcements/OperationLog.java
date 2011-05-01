package pt.ist.expenditureTrackingSystem.domain.announcements;

import module.workflow.domain.WorkflowProcess;
import myorg.domain.User;
import myorg.util.BundleUtil;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import myorg.domain.exceptions.DomainException;

public class OperationLog extends OperationLog_Base {

    public OperationLog(AnnouncementProcess process, User user, String operation, AnnouncementProcessStateType state) {
	super();
	init(process, user);
	super.setOperation(operation);
	super.setState(state);
    }

    // @Override
    // public AbstractActivity<GenericProcess> getActivity() {
    // AnnouncementProcess process = (AnnouncementProcess) getProcess();
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

    @Override
    public void setState(AnnouncementProcessStateType state) {
	throw new DomainException("error.unable.to.change.when.state");
    }

    @Override
    public String getDescription() {
	return BundleUtil.getFormattedStringFromResourceBundle("resources/AnnouncementsResources", "label." + getOperation());
    }

    @Override
    public boolean isConnectedToCurrentHost() {
	final GenericProcess genericProcess = (GenericProcess) getProcess();
	return genericProcess != null && genericProcess.isConnectedToCurrentHost();
    }

}
