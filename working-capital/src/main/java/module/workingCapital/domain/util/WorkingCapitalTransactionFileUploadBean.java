package module.workingCapital.domain.util;

import module.workflow.domain.WorkflowProcess;
import module.workflow.util.WorkflowFileUploadBean;
import module.workingCapital.domain.TransactionFile;
import module.workingCapital.domain.WorkingCapitalTransaction;

public class WorkingCapitalTransactionFileUploadBean extends WorkflowFileUploadBean {
    private WorkingCapitalTransaction transaction;

    public WorkingCapitalTransactionFileUploadBean(WorkflowProcess process, WorkingCapitalTransaction transaction) {
        super(process);
        setTransaction(transaction);
    }

    public WorkingCapitalTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(WorkingCapitalTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public Class<? extends TransactionFile> getSelectedInstance() {
        // TODO Auto-generated method stub
        return (Class<? extends TransactionFile>) super.getSelectedInstance();
    }
}