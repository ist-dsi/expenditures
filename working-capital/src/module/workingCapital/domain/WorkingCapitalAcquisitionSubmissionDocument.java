package module.workingCapital.domain;

public class WorkingCapitalAcquisitionSubmissionDocument extends WorkingCapitalAcquisitionSubmissionDocument_Base {

    public WorkingCapitalAcquisitionSubmissionDocument(final WorkingCapitalAcquisitionSubmission submissionTransaction,
	    final byte[] contents, final String fileName) {
	super();
	setTransaction(submissionTransaction);
	setContent(contents);
	setFilename(fileName);
    }

}
