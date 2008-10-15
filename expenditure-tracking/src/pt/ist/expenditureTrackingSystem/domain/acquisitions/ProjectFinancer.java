package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.organization.Project;
import pt.utl.ist.fenix.tools.util.Strings;

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
	final StringBuilder financerString = new StringBuilder(super.getEffectiveFundAllocationIds());
	Strings strings = getEffectiveProjectFundAllocationId();
	if (strings != null && !strings.isEmpty()) {
	    for (String allocationId : strings) {
		financerString.append(getAllocationIds(allocationId, "financer.label.allocation.id.prefix.mgp"));
		financerString.append(' ');
	    }
	}
	return financerString.toString();
    }

    @Override
    public boolean hasAllocatedFundsForAllProject() {
	return getProjectFundAllocationId() != null && !getProjectFundAllocationId().isEmpty();
    }

    @Override
    public boolean hasAllocatedFundsPermanentlyForAllProjectFinancers() {
	return getEffectiveProjectFundAllocationId() != null && !getEffectiveProjectFundAllocationId().isEmpty();
    }

    @Override
    public boolean isProjectFinancer() {
	return true;
    }

    public void addEffectiveProjectFundAllocationId(String effectiveProjectFundAllocationId) {
	if (StringUtils.isEmpty(effectiveProjectFundAllocationId)) {
	    throw new DomainException("acquisitionProcess.message.exception.effectiveFundAllocationCannotBeNull");
	}
	Strings strings = getEffectiveProjectFundAllocationId();
	if (strings == null) {
	    strings = new Strings(effectiveProjectFundAllocationId);
	    setEffectiveProjectFundAllocationId(strings);
	} else {
	    strings.add(effectiveProjectFundAllocationId);
	}

    }

}
