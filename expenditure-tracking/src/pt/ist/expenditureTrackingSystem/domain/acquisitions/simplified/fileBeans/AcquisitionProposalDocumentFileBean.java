package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.fileBeans;

import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;

public class AcquisitionProposalDocumentFileBean extends WorkflowFileUploadBean {

    private String proposalID;

    public AcquisitionProposalDocumentFileBean(WorkflowProcess process) {
	super(process);
    }

    public String getProposalID() {
	return proposalID;
    }

    public void setProposalID(String proposalID) {
	this.proposalID = proposalID;
    }

}
