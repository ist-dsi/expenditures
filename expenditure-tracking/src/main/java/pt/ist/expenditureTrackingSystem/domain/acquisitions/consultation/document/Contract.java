package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.document;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import module.workflow.domain.ProcessFileSignatureHandler;
import module.workflow.domain.ProcessFileSignatureHandler.Provider;
import module.workflow.domain.ProcessFileValidationException;
import module.workflow.domain.SigningState;
import module.workflow.domain.WorkflowProcess;
import module.workflow.util.ClassNameBundle;
import module.workflow.util.FileUploadBeanResolver;
import module.workflow.util.WorkflowFileUploadBean;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation.MultipleSupplierConsultationProcessState;

@ClassNameBundle(bundle = "ExpenditureResources")
public class Contract extends Contract_Base {

    private static class ContractSignHandler extends DocumentSignHandler<Contract> {

        private ContractSignHandler(final Contract processFile) {
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
            return signingState == SigningState.CREATED && ExpenditureTrackingSystem.isExpenseAuthority(user);
        }
    }

    static {
        FileUploadBeanResolver.registerBeanForProcessFile(Contract.class, WorkflowFileUploadBean.class);
        final Provider<Contract> provider = (f) -> new ContractSignHandler(f);
        ProcessFileSignatureHandler.register(Contract.class, provider);
    }

    public Contract(final String displayName, final String filename, final byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void validateUpload(final WorkflowProcess workflowProcess) throws ProcessFileValidationException {
        final MultipleSupplierConsultationProcess process = (MultipleSupplierConsultationProcess) workflowProcess;
        if (process.getState() != MultipleSupplierConsultationProcessState.PENDING_SUPPLIER_SELECTION) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.not.in.phase.PENDING_SUPPLIER_SELECTION");
        }
        super.validateUpload(workflowProcess);
        if (!ExpenditureTrackingSystem.isAcquisitionCentralGroupMember()
                && !ExpenditureTrackingSystem.isExpenseAuthority(Authenticate.getUser())) {
            throw new ProcessFileValidationException("resources/ExpenditureResources", "error.only.available.to.acquisitionCentralGroupMember");
        }
    }
    
}
