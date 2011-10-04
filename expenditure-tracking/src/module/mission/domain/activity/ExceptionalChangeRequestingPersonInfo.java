package module.mission.domain.activity;

import java.io.Serializable;

import module.mission.domain.MissionProcess;
import module.organization.domain.Person;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;

public class ExceptionalChangeRequestingPersonInfo extends ActivityInformation<MissionProcess> implements Serializable {

    private Person requester;
    private String comment;

    public ExceptionalChangeRequestingPersonInfo(final MissionProcess missionProcess,
	    final WorkflowActivity<MissionProcess, ? extends ActivityInformation<MissionProcess>> activity) {
	super(missionProcess, activity);
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
