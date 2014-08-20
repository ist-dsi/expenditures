package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import pt.ist.bennu.core.domain.User;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.SubProject;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;

public class FundAllocationExpirationDateAndPurchaseOrderDocument extends FundAllocationExpirationDate {

    @Override
    public boolean isActive(RegularAcquisitionProcess process, User user) {
        return super.isActive(process, user) && isConnectedToAuthorizedMissionProcess(process);
    }

    private boolean isConnectedToAuthorizedMissionProcess(final RegularAcquisitionProcess process) {
        final MissionProcess missionProcess = process.getMissionProcess();
        if (missionProcess != null && missionProcess.isAuthorized()) {
            final AcquisitionRequest request = process.getRequest();
            final Supplier supplier = request.getSupplier();
            if (process.getSkipSupplierFundAllocation().booleanValue()
                    && MissionSystem.getInstance().getMandatorySupplierSet().contains(supplier)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void process(final CreateAcquisitionPurchaseOrderDocumentInformation activityInformation) {
        super.process(activityInformation);
        final RegularAcquisitionProcess process = activityInformation.getProcess();
        final MissionProcess missionProcess = process.getMissionProcess();
        for (final MissionFinancer missionFinancer : missionProcess.getMission().getFinancerSet()) {
            final Financer financer = findFinancer(process, missionFinancer.getUnit());
            if (financer != null) {
                if (financer.isProjectFinancer()) {
                    final ProjectFinancer projectFinancer = (ProjectFinancer) financer;
                    projectFinancer.setProjectFundAllocationId(missionFinancer.getProjectFundAllocationId());
                }
                financer.setFundAllocationId(missionFinancer.getFundAllocationId());
                financer.setCommitmentNumber(missionFinancer.getCommitmentNumber());
            }
        }
        process.skitToAuthorizedState();
        CreateAcquisitionPurchaseOrderDocument.createPurchaseOrderDocument(activityInformation);
    }

    private Financer findFinancer(final RegularAcquisitionProcess process, final Unit unit) {
        for (final Financer financer : process.getRequest().getFinancersSet()) {
        	final Unit funit = financer.getUnit();
            if (funit == unit || (funit instanceof SubProject && funit.getParentUnit() == unit)) {
                return financer;
            }
        }
        return unit instanceof SubProject ? findFinancer(process, unit.getParentUnit()) : null;
    }

    @Override
    public boolean isDefaultInputInterfaceUsed() {
        return false;
    }

}
