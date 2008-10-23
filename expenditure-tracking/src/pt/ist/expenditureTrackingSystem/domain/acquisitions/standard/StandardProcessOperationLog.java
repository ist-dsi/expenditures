package pt.ist.expenditureTrackingSystem.domain.acquisitions.standard;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.StandardAcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class StandardProcessOperationLog extends StandardProcessOperationLog_Base {

    public StandardProcessOperationLog(AcquisitionProcess process, Person person, String operation, DateTime when) {
	super();
	init(process, person, operation, when);
	setState((StandardAcquisitionProcessStateType)process.getAcquisitionProcessState().getCurrentState());
    }

    @Override
    public Enum getLogState() {
	return getState();
    }
}
