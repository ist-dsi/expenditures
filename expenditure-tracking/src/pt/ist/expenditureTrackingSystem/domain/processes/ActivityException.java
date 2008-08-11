package pt.ist.expenditureTrackingSystem.domain.processes;

public class ActivityException extends RuntimeException {

    private String activityName;
    
    public ActivityException(String message, String activityName) {
	super(message);
	this.activityName = activityName;
    }
    
    public String getActivityName() {
	return activityName;
    }
}
