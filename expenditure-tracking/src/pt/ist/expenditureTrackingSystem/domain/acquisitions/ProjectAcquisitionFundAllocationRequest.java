package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import myorg.applicationTier.Authenticate;
import myorg.domain.User;
import myorg.domain.exceptions.DomainException;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ProjectFundAllocation;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons.ProjectFundAllocationActivityInformation;
import pt.ist.expenditureTrackingSystem.domain.dto.FundAllocationBean;
import pt.ist.fenixWebFramework.security.UserView;

public class ProjectAcquisitionFundAllocationRequest extends ProjectAcquisitionFundAllocationRequest_Base {
    
    public ProjectAcquisitionFundAllocationRequest(final UnitItem unitItem,
	    final String processId, final String processUrl,
	    final Integer payingUnitNumber, final String payingAccountingUnit,
	    final Money totalValue, final Boolean finalFundAllocation) {
        super();
        setUnitItem(unitItem);
        setProcessId(processId);
        setProcessUrl(processUrl);
        setPayingUnitNumber(payingUnitNumber);
        setPayingAccountingUnit(payingAccountingUnit);
        setTotalValue(totalValue);
        setFinalFundAllocation(finalFundAllocation);
    }

    @Override
    public void registerFundAllocation(final String fundAllocationNumber, final String operatorUsername) {
	if (hasCancelFundAllocationRequest()) {
	    throw new DomainException("error.cannot.allocate.funds.because.request.has.been.canceled");
	}

	super.registerFundAllocation(fundAllocationNumber, operatorUsername);

	final UnitItem unitItem = getUnitItem();
	final ProjectFinancer financer = (ProjectFinancer) unitItem.getFinancer();

	final String projectFundAllocationIdsForAllUnitItems = financer.getProjectFundAllocationIdsForAllUnitItems();

        final PaymentProcess process = financer.getProcess();

        final String activityName = ProjectFundAllocation.class.getSimpleName();
        final WorkflowActivity activity = getActivity(process, activityName);

        final ActivityInformation activityInformation = activity.getActivityInformation(process);

        final ProjectFundAllocationActivityInformation information = (ProjectFundAllocationActivityInformation) activity.getActivityInformation(process);

        final List<FundAllocationBean> args;
        if (projectFundAllocationIdsForAllUnitItems == null) {
            args = Collections.emptyList();
        } else {
            final FundAllocationBean fundAllocationBean = new FundAllocationBean(financer);
            fundAllocationBean.setFundAllocationId(projectFundAllocationIdsForAllUnitItems);
            args = Collections.singletonList(fundAllocationBean);
        }
        information.setBeans(args);

        final myorg.applicationTier.Authenticate.UserView currentUserView = UserView.getUser();
        final User user = User.findByUsername(operatorUsername);
        final myorg.applicationTier.Authenticate.UserView userView = Authenticate.authenticate(user);
	try {
	    UserView.setUser(userView);
	    activity.execute(information);
	} finally {
	    UserView.setUser(currentUserView);
	}
    }

    private <T extends WorkflowProcess> WorkflowActivity<T, ActivityInformation<T>> getActivity(
	    final WorkflowProcess process, final String activityName) {
	return process.getActivity(activityName);
    }

    @Override
    public String getQueryString() {
	if (getExternalRegistrationDate() == null) {
	    // TODO insert info into remote table
	    return "select 1";	    
	}
	// TODO check fund allocation value
	return "select 1";
    }

    @Override
    public void processResultSet(final ResultSet resultSet) throws SQLException {
	if (getExternalRegistrationDate() == null) {
	    // TODO check if insert went well
	} else {
	    // TODO register fund allocation value
	}
    }

}
