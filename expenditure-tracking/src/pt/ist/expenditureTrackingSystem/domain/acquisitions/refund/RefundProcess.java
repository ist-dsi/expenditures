package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ist.expenditureTrackingSystem.domain.ProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RefundProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess.ActivityScope;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericRefundProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.AddPayingUnit;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.CreateRefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.DeleteRefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.EditRefundItem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.activities.RemovePayingUnit;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateRefundProcessBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import pt.ist.fenixWebFramework.services.Service;

public class RefundProcess extends RefundProcess_Base {

    private static Map<ActivityScope, List<GenericRefundProcessActivity>> activityMap = new HashMap<ActivityScope, List<GenericRefundProcessActivity>>();

    static {
	List<GenericRefundProcessActivity> requestActivitites = new ArrayList<GenericRefundProcessActivity>();
	requestActivitites.add(new CreateRefundItem());
	requestActivitites.add(new AddPayingUnit());
	requestActivitites.add(new RemovePayingUnit());
	activityMap.put(ActivityScope.REQUEST_INFORMATION, requestActivitites);

	List<GenericRefundProcessActivity> itemActivities = new ArrayList<GenericRefundProcessActivity>();
	itemActivities.add(new EditRefundItem());
	itemActivities.add(new DeleteRefundItem());

	activityMap.put(ActivityScope.REQUEST_ITEM, itemActivities);
    }

    public RefundProcess(Person requestor, Person refundee, Unit requestingUnit) {
	super();
	new RefundRequest(this, requestor, refundee, requestingUnit);
	new RefundProcessState(this);
    }

    public List<GenericRefundProcessActivity> getActiveActivities() {
	List<GenericRefundProcessActivity> activities = new ArrayList<GenericRefundProcessActivity>();
	for (ActivityScope scope : ActivityScope.values()) {
	    activities.addAll(getActiveActivitiesForScope(scope));
	}
	return activities;
    }

    private List<GenericRefundProcessActivity> getActiveActivitiesForScope(ActivityScope scope) {
	List<GenericRefundProcessActivity> activities = new ArrayList<GenericRefundProcessActivity>();
	for (GenericRefundProcessActivity activity : activityMap.get(scope)) {
	    if (activity.isActive(this)) {
		activities.add(activity);
	    }
	}
	return activities;
    }

    public List<GenericRefundProcessActivity> getActiveActivitiesForRequest() {
	return getActiveActivitiesForScope(ActivityScope.REQUEST_INFORMATION);
    }

    public List<GenericRefundProcessActivity> getActiveActivitiesForItem() {
	return getActiveActivitiesForScope(ActivityScope.REQUEST_ITEM);
    }

    @Override
    public GenericRefundProcessActivity getActivityByName(String activityName) {
	for (ActivityScope scope : ActivityScope.values()) {
	    for (GenericRefundProcessActivity activity : activityMap.get(scope)) {
		if (activity.getName().equals(activityName)) {
		    return activity;
		}
	    }
	}
	return null;
    }

    @Service
    public static RefundProcess createNewRefundProcess(CreateRefundProcessBean bean) {
	RefundProcess process = new RefundProcess(bean.getRequestor(), bean.getRefundee(), bean.getRequestingUnit());
	if (bean.isRequestUnitPayingUnit()) {
	    process.getRequest().addPayingUnit(bean.getRequestingUnit());
	}
	return process;
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

    public List<Unit> getPayingUnits() {
	List<Unit> res = new ArrayList<Unit>();
	for (Financer financer : getRequest().getFinancers()) {
	    res.add(financer.getUnit());
	}
	return res;
    }

}
