package pt.ist.expenditureTrackingSystem.domain;

import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import pt.ist.expenditureTrackingSystem.domain.util.ByteArray;

public class File extends File_Base {

    public File() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    protected String guessContentType(final String filename) {
	return new MimetypesFileTypeMap().getContentType(filename);
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

    public static <T extends File> List<T> getFiles(Class<T> clazz) {
	List<T> files = new ArrayList<T>();
	for (File file : ExpenditureTrackingSystem.getInstance().getFiles()) {
	    if (file.getClass().equals(clazz)) {
		files.add((T)file);
	    }
	}
	return files;
    }
}
