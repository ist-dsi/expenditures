package pt.ist.expenditureTrackingSystem.domain;


public class File extends File_Base {

    public File() {
        super();
        setOjbConcreteClass(getClass().getName());
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    protected String guessContentType(final String filename) {
	if (filename.endsWith("pdf")) {
	    return "application/pdf";
	}
	return "application/unknown";
    }

    @Override
    public void setFilename(final String filename) {
	super.setFilename(filename);
	setContentType(guessContentType(filename));
    }

}
