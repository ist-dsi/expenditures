package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;

public class ProjectFinancer extends ProjectFinancer_Base {
    
    protected ProjectFinancer() {
        super();
    }

    public ProjectFinancer(final AcquisitionRequest acquisitionRequest, final Project project) {
	this();
	if (acquisitionRequest == null || project == null) {
	    throw new DomainException("error.financer.wrong.initial.arguments");
	}
	if (acquisitionRequest.hasPayingUnit(project)) {
	    throw new DomainException("error.financer.acquisition.request.already.has.paying.unit");
	}

	setFundedRequest(acquisitionRequest);
	setUnit(project);
    }

    @Override
    public String getFundAllocationIds() {
	final String financerString = super.getFundAllocationIds();
	return financerString + " " + getAllocationIds(getProjectFundAllocationId(), "financer.label.allocation.id.prefix.mgp");
    }

    @Override
    public String getEffectiveFundAllocationIds() {
	final String financerString = super.getEffectiveFundAllocationIds();
	return financerString + " " + getAllocationIds(getEffectiveProjectFundAllocationId(), "financer.label.allocation.id.prefix.mgp");
    }

}
