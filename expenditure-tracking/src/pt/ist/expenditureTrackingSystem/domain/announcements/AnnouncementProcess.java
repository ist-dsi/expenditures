package pt.ist.expenditureTrackingSystem.domain.announcements;

import java.util.ArrayList;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;

public class AnnouncementProcess extends AnnouncementProcess_Base {

    private static ArrayList<GenericAcquisitionProcessActivity> activities = new ArrayList<GenericAcquisitionProcessActivity>();

    static {
	//activities.add(new CreateAnnouncement());
	//activities.add(new EditAnnouncement());
	//activities.add(new SubmitForApproval());
	//activities.add(new ApproveAnnouncement());
	//activities.add(new RejectAnnouncement());
	//activities.add(new CloseAnnouncement());
    }


    @Override
    public GenericAcquisitionProcessActivity getActivityByName(String activityName) {

	for (GenericAcquisitionProcessActivity activity : activities) {
	    if (activity.getName().equals(activityName)) {
		return activity;
	    }
	}
	return null;
    }
    
}
