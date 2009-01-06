package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.CreateRefundItem;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class RefundProcess extends RefundProcess_Base {

    private static List<GenericRefundProcessActivity> refundActivitites = new ArrayList<GenericRefundProcessActivity>();

    static {
	refundActivitites.add(new CreateRefundItem());
    }

    public RefundProcess(Person requestor, Person refundee, Unit requestingUnit) {
	super();
	new RefundRequest(this, requestor, refundee, requestingUnit);
	new RefundProcessState(this);
    }

    public List<GenericRefundProcessActivity> getActiveActivities() {
	List<GenericRefundProcessActivity> activities = new ArrayList<GenericRefundProcessActivity>();

	for (GenericRefundProcessActivity activity : refundActivitites) {
	    if (activity.isActive(this)) {
		activities.add(activity);
	    }
	}
	return activities;
    }

    @Override
    public GenericRefundProcessActivity getActivityByName(String activityName) {
	for (GenericRefundProcessActivity activity : refundActivitites) {
	    if (activity.getName().equals(activityName)) {
		return activity;
	    }
	}
	return null;
    }

    @Service
    public static RefundProcess createNewRefundProcess(CreateRefundProcessBean bean) {
	return new RefundProcess(bean.getRequestor(), bean.getRefundee(), bean.getRequestingUnit());
    }

    protected RefundProcessState getLastProcessState() {
	return (RefundProcessState) Collections.max(getProcessStates(), ProcessState.COMPARATOR_BY_WHEN);
    }

    public RefundProcessState getProcessState() {
	return getLastProcessState();
    }

    public boolean isInGenesis() {
	return getProcessState().isInGenesis();
    }
    
    @Override
    public boolean hasAnyAvailableActivitity() {
	return !getActiveActivities().isEmpty();
    }

}
