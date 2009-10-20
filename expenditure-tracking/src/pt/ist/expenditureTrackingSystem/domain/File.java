package pt.ist.expenditureTrackingSystem.domain;

import java.util.ArrayList;
import java.util.List;

public class File extends File_Base {

    public File() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public void delete() {
	removeExpenditureTrackingSystem();
	super.delete();
    }

    public static <T extends File> List<T> getFiles(final Class<T> clazz) {
	final List<T> files = new ArrayList<T>();
	for (final File file : ExpenditureTrackingSystem.getInstance().getFiles()) {
	    if (file.getClass().equals(clazz)) {
		files.add((T) file);
	    }
	}
	return files;
    }

}
