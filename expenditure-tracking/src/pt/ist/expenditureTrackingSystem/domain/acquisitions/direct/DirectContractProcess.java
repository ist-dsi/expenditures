package pt.ist.expenditureTrackingSystem.domain.acquisitions.direct;

import java.util.ArrayList;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.GenericAcquisitionProcessActivity;
import pt.ist.expenditureTrackingSystem.domain.announcements.Announcement;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateAnnouncementBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.services.Service;

public class DirectContractProcess extends DirectContractProcess_Base {

    private static ArrayList<GenericAcquisitionProcessActivity> activities = new ArrayList<GenericAcquisitionProcessActivity>();

    static {
    }

    // protected DirectContractProcess(CreateRequestForProposalProcessBean
    // requestBean, byte[] proposalDocument) {
    // super();
    // new AcquisitionProcessState(this,
    // AcquisitionProcessStateType.IN_GENESIS);
    // //createAcquisitionRequest(requestBean, proposalDocument);
    // }

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
