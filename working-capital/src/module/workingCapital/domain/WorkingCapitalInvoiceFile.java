package module.workingCapital.domain;

import myorg.util.ClassNameBundle;

@ClassNameBundle(bundle="resources/WorkingCapitalResources")
public class WorkingCapitalInvoiceFile extends WorkingCapitalInvoiceFile_Base {

    public WorkingCapitalInvoiceFile(String displayName, String filename, byte[] content, WorkingCapitalTransaction transaction) {
	super();
	init(displayName, filename, content);
	setTransaction(transaction);
    }

}
