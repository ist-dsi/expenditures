package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.ProcessFileSignatureHandler;
import module.workflow.domain.ProcessFileSignatureHandler.Provider;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.SigningState;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class ManagementDocument extends ManagementDocument_Base {

    private static class ManagementDocumentSignHandler extends DocumentSignHandler<ManagementDocument> {

        private ManagementDocumentSignHandler(final ManagementDocument processFile) {
            super(processFile);
        }

        @Override
        public String queue() {
            return ExpenditureConfiguration.get().queueConsultationManagementDocument();
        }

        @Override
        public boolean canSignFile() {
            final SigningState signingState = processFile.getSigningState();
            final User user = Authenticate.getUser();
            return signingState == SigningState.CREATED && ExpenditureTrackingSystem.isExpenseAuthority(user);
        }
    }

    static {
        final Provider<ManagementDocument> provider = (f) -> new ManagementDocumentSignHandler(f);
        ProcessFileSignatureHandler.register(ManagementDocument.class, provider);
    }

    @Override
    public void validateUpload(final WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        super.validateUpload(workflowProcess);
        if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()
                && !ExpenditureTrackingSystem.isExpenseAuthority(Authenticate.getUser())) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.only.available.to.acquisitionCentralGroupMember");
        }
    }

}
