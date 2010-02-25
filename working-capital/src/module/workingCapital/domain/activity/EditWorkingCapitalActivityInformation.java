package module.workingCapital.domain.activity;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.WorkingCapitalAcquisition;
import module.workingCapital.domain.WorkingCapitalAcquisitionTransaction;
import module.workingCapital.domain.WorkingCapitalProcess;

public class EditWorkingCapitalActivityInformation extends RegisterWorkingCapitalAcquisitionActivityInformation {

    private WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction;

    public EditWorkingCapitalActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && workingCapitalAcquisitionTransaction != null;
    }

    public WorkingCapitalAcquisitionTransaction getWorkingCapitalAcquisitionTransaction() {
        return workingCapitalAcquisitionTransaction;
    }

    public void setWorkingCapitalAcquisitionTransaction(final WorkingCapitalAcquisitionTransaction workingCapitalAcquisitionTransaction) {
	if (this.workingCapitalAcquisitionTransaction != workingCapitalAcquisitionTransaction) {
	    this.workingCapitalAcquisitionTransaction = workingCapitalAcquisitionTransaction;
	    if (workingCapitalAcquisitionTransaction != null) {
		final WorkingCapitalAcquisition workingCapitalAcquisition = workingCapitalAcquisitionTransaction.getWorkingCapitalAcquisition();
		setSupplier(workingCapitalAcquisition.getSupplier());
		setDocumentNumber(workingCapitalAcquisition.getDocumentNumber());
		setDescription(workingCapitalAcquisition.getDescription());
		setAcquisitionClassification(workingCapitalAcquisition.getAcquisitionClassification());
		setMoney(workingCapitalAcquisitionTransaction.getValue());
		setValueWithoutVat(workingCapitalAcquisition.getValueWithoutVat());
	    }
        }
    }

}
