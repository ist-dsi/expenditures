package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import com.google.common.base.Strings;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class SetFinancialProcessIdentificationInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private String financialProcessIdentification;

    public SetFinancialProcessIdentificationInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
        setFinancialProcessIdentification(process.getFinancialProcessId());
    }

    public String getFinancialProcessIdentification() {
        return financialProcessIdentification;
    }

    public void setFinancialProcessIdentification(String financialProcessIdentification) {
        this.financialProcessIdentification = financialProcessIdentification;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && !Strings.isNullOrEmpty(financialProcessIdentification);
    }

}
