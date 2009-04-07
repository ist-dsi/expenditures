package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.expenditureTrackingSystem.domain.dto.PayingUnitTotalBean;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.expenditureTrackingSystem.domain.organization.Unit;
import myorg.domain.util.ByteArray;
import myorg.domain.util.Money;
import pt.ist.fenixframework.pstm.Transaction;

public abstract class Acquisition extends Acquisition_Base {

    public void receiveInvoice(final String filename, final byte[] bytes, final String invoiceNumber, final LocalDate invoiceDate) {
	final Invoice invoice = hasInvoice() ? getInvoice() : new Invoice(this);
	invoice.setFilename(filename);
	invoice.setContent(new ByteArray(bytes));
	invoice.setInvoiceNumber(invoiceNumber);
	invoice.setInvoiceDate(invoiceDate);
    }

    public String getInvoiceNumber() {
	final Invoice invoice = getInvoice();
	return invoice == null ? null : invoice.getInvoiceNumber();
    }

    public LocalDate getInvoiceDate() {
	final Invoice invoice = getInvoice();
	return invoice == null ? null : invoice.getInvoiceDate();
    }

    public boolean isInvoiceReceived() {
	final Invoice invoice = getInvoice();
	return invoice != null && invoice.isInvoiceReceived();
    }

    public void delete() {
	if (hasInvoice()) {
	    getInvoice().delete();
	}
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
