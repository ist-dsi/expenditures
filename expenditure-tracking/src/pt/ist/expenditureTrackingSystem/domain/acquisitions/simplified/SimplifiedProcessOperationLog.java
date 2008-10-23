package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.SimplifiedAcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class SimplifiedProcessOperationLog extends SimplifiedProcessOperationLog_Base {

    public SimplifiedProcessOperationLog(AcquisitionProcess process, Person person, String operation, DateTime when) {
	super();
	init(process, person, operation, when);

	setState((SimplifiedAcquisitionProcessStateType) process.getAcquisitionProcessState().getCurrentState());
    }

    @Override
    public Enum getLogState() {
	return getState();
    }
}
