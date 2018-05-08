package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.ProcessFileSignatureHandler;
import module.workflow.domain.ProcessFileSignatureHandler.Provider;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.SigningState;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;

public class JuryDocument extends JuryDocument_Base {

    private static class JuryDocumentSignHandler extends DocumentSignHandler<JuryDocument> {

        private JuryDocumentSignHandler(final JuryDocument processFile) {
            super(processFile);
        }

        @Override
        public String queue() {
            return Authenticate.getUser().getUsername();
        }

        @Override
        public boolean canSignFile() {
            final SigningState signingState = processFile.getSigningState();
            final User user = Authenticate.getUser();
            MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) processFile.getProcess();
            return signingState == SigningState.CREATED && process.isJuryMember(user);
        }
    }

    static {
        final Provider<JuryDocument> provider = (f) -> new JuryDocumentSignHandler(f);
        ProcessFileSignatureHandler.register(JuryDocument.class, provider);
    }

    @Override
    public void validateUpload(final WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        super.validateUpload(workflowProcess);
        if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()
                && !((MultipleSupplierConsultationProcess) workflowProcess).isJuryMember(Authenticate.getUser())) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.only.available.to.acquisitionCentralGroupMember");
        }
    }

}
