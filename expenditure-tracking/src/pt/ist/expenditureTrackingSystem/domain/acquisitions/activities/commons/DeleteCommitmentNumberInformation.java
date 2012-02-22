package pt.ist.expenditureTrackingSystem.domain.acquisitions.activities.commons;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;

public class DeleteCommitmentNumberInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private Financer financer;

    public DeleteCommitmentNumberInformation(final RegularAcquisitionProcess process,
	    final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && financer != null;
    }

    public Financer getFinancer() {
        return financer;
    }

    public void setFinancer(Financer financer) {
        this.financer = financer;
    }

}
