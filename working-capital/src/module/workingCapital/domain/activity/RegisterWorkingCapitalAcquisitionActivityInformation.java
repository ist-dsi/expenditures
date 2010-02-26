package module.workingCapital.domain.activity;

import java.io.InputStream;

import module.workflow.activities.ActivityInformation;
import module.workflow.activities.WorkflowActivity;
import module.workingCapital.domain.AcquisitionClassification;
import module.workingCapital.domain.WorkingCapitalProcess;
import myorg.domain.util.Money;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;

public class RegisterWorkingCapitalAcquisitionActivityInformation extends ActivityInformation<WorkingCapitalProcess> {

    private Supplier supplier;
    private String documentNumber;
    private String description;
    private AcquisitionClassification acquisitionClassification;
    private Money money;
    private Money valueWithoutVat;
    private transient InputStream inputStream;
    private String filename;
    private String displayName;

    public RegisterWorkingCapitalAcquisitionActivityInformation(final WorkingCapitalProcess workingCapitalProcess,
	    final WorkflowActivity<WorkingCapitalProcess, ? extends ActivityInformation<WorkingCapitalProcess>> activity) {
	super(workingCapitalProcess, activity);
    }

    @Override
    public boolean hasAllneededInfo() {
	return isForwardedFromInput() && documentNumber != null && supplier != null && acquisitionClassification != null
		&& money != null && money.isPositive() && valueWithoutVat != null && valueWithoutVat.isPositive()
		&& description != null && !description.isEmpty() && getInputStream() != null;
    }

    public String getDocumentNumber() {
	return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
	this.documentNumber = documentNumber;
    }

    public Supplier getSupplier() {
	return supplier;
    }

    public void setSupplier(Supplier supplier) {
	this.supplier = supplier;
    }

    public AcquisitionClassification getAcquisitionClassification() {
	return acquisitionClassification;
    }

    public void setAcquisitionClassification(AcquisitionClassification acquisitionClassification) {
	this.acquisitionClassification = acquisitionClassification;
    }

    public Money getMoney() {
	return money;
    }

    public void setMoney(Money money) {
	this.money = money;
    }

    public Money getValueWithoutVat() {
	return valueWithoutVat;
    }

    public void setValueWithoutVat(Money valueWithoutVat) {
	this.valueWithoutVat = valueWithoutVat;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

}
