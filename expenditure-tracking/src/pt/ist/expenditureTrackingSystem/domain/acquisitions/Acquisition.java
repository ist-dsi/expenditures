package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.joda.time.LocalDate;

public abstract class Acquisition extends Acquisition_Base {

    public abstract <T extends Invoice> T receiveInvoice(final String filename, final byte[] bytes, final String invoiceNumber,
	    final LocalDate invoiceDate);

}
