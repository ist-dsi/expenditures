package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import org.joda.time.LocalDate;

import com.google.common.base.Strings;

import module.finance.util.Money;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;

public class RegisterAdvancePaymentInformation extends ActivityInformation<SimplifiedProcedureProcess> {

    private String reference;
    private Money value;
    private Money additionalValue;
    private LocalDate date;
    private String description;

    public RegisterAdvancePaymentInformation(SimplifiedProcedureProcess process,
            WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
        super(process, activity);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    public Money getAdditionalValue() {
        return additionalValue;
    }

    public void setAdditionalValue(Money additionalValue) {
        this.additionalValue = additionalValue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean hasAllneededInfo() {
        return isForwardedFromInput() && !Strings.isNullOrEmpty(reference) && !value.isZero() && date != null;
    }
}
