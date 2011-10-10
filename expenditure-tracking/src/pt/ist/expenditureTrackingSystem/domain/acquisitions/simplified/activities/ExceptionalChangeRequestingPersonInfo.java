package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import java.io.Serializable;

import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class ExceptionalChangeRequestingPersonInfo extends ActivityInformation<RegularAcquisitionProcess> implements Serializable {

    private Person requester;
    private String comment;

    public ExceptionalChangeRequestingPersonInfo(final RegularAcquisitionProcess process,
	    final WorkflowActivity<RegularAcquisitionProcess, ? extends ActivityInformation<RegularAcquisitionProcess>> activity) {
	super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && getRequester() != null && getComment() != null;
    }

    public void setRequester(Person requester) {
	this.requester = requester;
    }

    public Person getRequester() {
	return requester;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getComment() {
	return comment;
    }

}
