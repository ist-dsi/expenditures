package pt.ist.expenditureTrackingSystem.domain.requests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess.ActivityScope;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRequestForProposalProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.expenditureTrackingSystem.domain.processes.AbstractActivity;
import pt.ist.expenditureTrackingSystem.domain.requests.activities.ApproveRequestForProposal;
import pt.ist.expenditureTrackingSystem.domain.requests.activities.GenericRequestForProposalProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.requests.activities.RejectRequestForProposal;
import pt.ist.fenixWebFramework.services.Service;

public class RequestForProposalProcess extends RequestForProposalProcess_Base {

    private static List<GenericRequestForProposalProcessActivity> activities = new ArrayList<GenericRequestForProposalProcessActivity>();

    static {
	activities.add(new ApproveRequestForProposal());
	activities.add(new RejectRequestForProposal());
    }

    protected RequestForProposalProcess(CreateRequestForProposalProcessBean requestBean, final byte[] proposalDocument) {
	super();
	new RequestForProposalProcessState(this, RequestForProposalProcessStateType.SUBMITTED_FOR_APPROVAL);
	createRequestForProposal(requestBean, proposalDocument);
    }

    @Service
    public static RequestForProposalProcess createNewRequestForProposalProcess(
	    final CreateRequestForProposalProcessBean requestBean, final byte[] requestForProposalDocument) {
	return createProcess(requestBean, requestForProposalDocument);
    }

    protected static RequestForProposalProcess createProcess(final CreateRequestForProposalProcessBean requestBean,
	    final byte[] requestForProposalDocument) {

	if (!isCreateNewProcessAvailable()) {
	    throw new DomainException("error.requestForProposalProcess.invalid.state.to.run.createNewRequestForProposalProcess");
	}

	return new RequestForProposalProcess(requestBean, null);
    }

    @Override
    public GenericRequestForProposalProcessActivity getActivityByName(String activityName) {

	for (GenericRequestForProposalProcessActivity activity : activities) {
	    if (activity.getName().equals(activityName)) {
		return activity;
	    }
	}
	return null;
    }

    public List<GenericRequestForProposalProcessActivity> getActiveActivitiesForRequest() {
	List<GenericRequestForProposalProcessActivity> activitiesResult = new ArrayList<GenericRequestForProposalProcessActivity>();
	for (GenericRequestForProposalProcessActivity activity : activities) {
	    if (activity.isActive(this)) {
		activitiesResult.add(activity);
	    }
	}
	return activitiesResult;
    }
    
    public boolean isProcessInState(RequestForProposalProcessStateType state) {
	return getLastRequestForProposalProcessStateType().equals(state);
    }

    public RequestForProposalProcessStateType getRequestForProposalProcessStateType() {
	return getLastRequestForProposalProcessStateType();
    }

    protected RequestForProposalProcessState getLastRequestForProposalProcessState() {
	return Collections.max(getRequestForProposalProcessStates(), RequestForProposalProcessState.COMPARATOR_BY_WHEN);
    }

    protected RequestForProposalProcessStateType getLastRequestForProposalProcessStateType() {
	return getLastRequestForProposalProcessState().getRequestForProposalProcessStateType();
    }

    public boolean isPersonAbleToExecuteActivities() {
	for (AbstractActivity<RequestForProposalProcess> activity : activities) {
	    if (activity.isActive(this)) {
		return true;
	    }
	}
	return false;
    }

    private void createRequestForProposal(CreateRequestForProposalProcessBean requestBean, byte[] documentBytes) {
	RequestForProposal proposal = getRequestForProposal();
	if (proposal == null) {
	    proposal = new RequestForProposal(this, requestBean.getRequester());
	}

	proposal.setTitle(requestBean.getTitle());
	proposal.setDescription(requestBean.getDescription());
	proposal.setRequestingUnit(requestBean.getRequestingUnit());
	proposal.setPublishDate(requestBean.getPublishDate());
	proposal.setExpireDate(requestBean.getExpireDate());
    }

    public boolean isResponsibleForUnit(Person person) {
	Unit requestingUnit = getRequestForProposal().getRequestingUnit();
	for (Authorization authorization : person.getAuthorizations()) {
	    if (authorization.isValid() && requestingUnit.equals(authorization.getUnit())) {
		return true;
	    }
	}
	return false;
    }

}
