package pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities;

import module.finance.domain.SupplierContact;
import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workflow.domain.WorkflowProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class CreateAcquisitionPurchaseOrderDocumentInformation extends ActivityInformation<RegularAcquisitionProcess> {

    private SupplierContact supplierContact = null;

    public CreateAcquisitionPurchaseOrderDocumentInformation(final RegularAcquisitionProcess process,
	    final WorkflowActivity<? extends WorkflowProcess, ? extends ActivityInformation> activity) {
	super(process, activity);
	final Supplier supplier = process.getRequest().getSupplier();
	if (supplier.getSupplierContactCount() == 1) {
	    supplierContact = supplier.getSupplierContactIterator().next();
	}
    }

    @Override
    public boolean hasAllneededInfo() {
	return super.hasAllneededInfo() && supplierContact != null;
    }

    public SupplierContact getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(SupplierContact supplierContact) {
        this.supplierContact = supplierContact;
    }

}
