package pt.ist.expenditureTrackingSystem.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact.AfterTheFactAcquisitionType;
import pt.ist.expenditureTrackingSystem.domain.dto.Issue.IssueType;
import pt.ist.expenditureTrackingSystem.domain.dto.Issue.IssueTypeLevel;
import pt.ist.expenditureTrackingSystem.domain.organization.Supplier;
import pt.ist.expenditureTrackingSystem.domain.util.Money;
import pt.ist.expenditureTrackingSystem.presentationTier.util.FileUploadBean;
import pt.ist.fenixWebFramework.services.Service;

public class AfterTheFactAcquisitionsImportBean extends FileUploadBean implements Serializable {

    private AfterTheFactAcquisitionType afterTheFactAcquisitionType;

    private String contents;

    private List<Issue> issues = new ArrayList<Issue>();

    private int importedLines = 0;
    private int errorCount = 0;
    private int warningCount = 0;

    public AfterTheFactAcquisitionsImportBean() {
    }

    public AfterTheFactAcquisitionType getAfterTheFactAcquisitionType() {
        return afterTheFactAcquisitionType;
    }

    public void setAfterTheFactAcquisitionType(AfterTheFactAcquisitionType afterTheFactAcquisitionType) {
        this.afterTheFactAcquisitionType = afterTheFactAcquisitionType;
    }

    public void setFileContents(final byte[] contents) {
	this.contents = new String(contents);
    }

    public String getContents() {
        return contents;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void registerIssue(final IssueType issueType, final int lineNumber, final String... messageArgs) {
	if (issueType.getIssueTypeLevel() == IssueTypeLevel.ERROR) {
	    errorCount++;
	} else if (issueType.getIssueTypeLevel() == IssueTypeLevel.WARNING) {
	    warningCount++;
	}
	issues.add(new Issue(issueType, lineNumber, messageArgs));
    }

    @Service
    public void importAcquisitions(final AfterTheFactAcquisitionsImportBean afterTheFactAcquisitionsImportBean) {
	final String[] lines = afterTheFactAcquisitionsImportBean.getContents().split("\n");
	for (int i = 0; i < lines.length; i++) {
	    final String line = lines[i];
	    if (line.isEmpty()) {
		registerIssue(IssueType.EMPTY_LINE, i);
	    } else {
		final String[] parts = line.split("\t");
		if (parts.length == 5) {
		    final String supplierNif = parts[0];
		    final String supplierName = parts[1];
		    final Supplier supplier = findSupplier(supplierNif, supplierName);
		    if (supplier == null) {
			registerIssue(IssueType.SUPPLIER_DOES_NOT_EXIST, i, supplierNif, supplierName);
		    }
		    final String description = parts[2];
		    final String valueString = parts[3];
		    Money value;
		    try {
			value = new Money(valueString);
		    } catch (DomainException ex) {
			value = null;
			registerIssue(IssueType.BAD_MONEY_VALUE_FORMAT, i, valueString);			
		    } catch (NumberFormatException ex) {
			value = null;
			registerIssue(IssueType.BAD_MONEY_VALUE_FORMAT, i, valueString);						
		    }
		    final String vatValueString = parts[4];
		    BigDecimal vatValue;
		    try {
			vatValue = new BigDecimal(vatValueString);
		    } catch (NumberFormatException ex) {
			vatValue = null;
			registerIssue(IssueType.BAD_VAT_VALUE_FORMAT, i, vatValueString);			
		    }
		    if (supplier != null && value != null && vatValue != null) {
			importAcquisition(supplier, description, value, vatValue);
			importedLines++;
		    }
		} else {
		    registerIssue(IssueType.WRONG_NUMBER_LINE_COLUMNS, i);
		}
	    }
	}
    }

    public void importAcquisition(final Supplier supplier, final String description, final Money value, final BigDecimal vatValue) {
	final AfterTheFactAcquisitionProcessBean afterTheFactAcquisitionProcessBean = new AfterTheFactAcquisitionProcessBean();
	afterTheFactAcquisitionProcessBean.setAfterTheFactAcquisitionType(getAfterTheFactAcquisitionType());
	afterTheFactAcquisitionProcessBean.setDescription(description);
	afterTheFactAcquisitionProcessBean.setSupplier(supplier);
	afterTheFactAcquisitionProcessBean.setValue(value);
	afterTheFactAcquisitionProcessBean.setVatValue(vatValue);
	AfterTheFactAcquisitionProcess.createNewAfterTheFactAcquisitionProcess(afterTheFactAcquisitionProcessBean);
    }

    private Supplier findSupplier(final String supplierNif, final String supplierName) {
	final Supplier supplier = Supplier.readSupplierByFiscalIdentificationCode(supplierNif);
	return supplier == null ? Supplier.readSupplierByName(supplierName) : supplier;
    }

    public int getErrorCount() {
	return errorCount;
    }

    public int getWarningCount() {
	return warningCount;
    }

    public boolean hasError() {
	return getErrorCount() > 0;
    }

    public int getImportedLines() {
        return importedLines;
    }

}
