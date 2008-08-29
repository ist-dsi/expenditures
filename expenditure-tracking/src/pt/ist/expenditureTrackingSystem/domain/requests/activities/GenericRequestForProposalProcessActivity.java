package pt.ist.expenditureTrackingSystem.domain.requests.activities;

import org.joda.time.DateTime;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.requests.OperationLog;
import pt.ist.expenditureTrackingSystem.domain.requests.RequestForProposalProcess;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public abstract class GenericRequestForProposalProcessActivity extends AbstractActivity<RequestForProposalProcess> {

    @Override
    protected void logExecution(RequestForProposalProcess process, String operationName, User user) {
	new OperationLog(process, user.getPerson(), operationName, process.getRequestForProposalProcessStateType(), new DateTime());
    }

    @Override
    public String getLocalizedName() {
	return RenderUtils.getResourceString("ACQUISITION_RESOURCES", "label." + getClass().getName());
    }
}
