package pt.ist.expenditureTrackingSystem.domain;

import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;


public class File extends File_Base {

    public static final String CONTENT_TYPE_UNKNOWN = "application/unknown";

    public static final String EXTENSION_PDF = "pdf";
    public static final String CONTENT_TYPE_PDF = "application/pdf";

    public File() {
        super();
        setOjbConcreteClass(getClass().getName());
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    protected String guessContentType(final String filename) {
	if (filename.endsWith(EXTENSION_PDF)) {
	    return CONTENT_TYPE_PDF;
	}
	return CONTENT_TYPE_UNKNOWN;
    }

    @Override
    public void setFilename(final String filename) {
	super.setFilename(filename);
	setContentType(guessContentType(filename));
    }

    public void setContent(final byte[] bytes) {
	final ByteArray byteArray = new ByteArray(bytes);
	setContent(byteArray);
    }

}
