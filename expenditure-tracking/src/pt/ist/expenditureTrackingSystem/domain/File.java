package pt.ist.expenditureTrackingSystem.domain;

import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import myorg.domain.util.ByteArray;
import pt.utl.ist.fenix.tools.util.FileUtils;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class File extends File_Base {

    public File() {
	super();
	setOjbConcreteClass(getClass().getName());
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	new FileContent(this);
    }

    protected String guessContentType(final String filename) {
	return new MimetypesFileTypeMap().getContentType(filename);
    }

    @Override
    public void setFilename(final String filename) {
	final String nicerFilename = FileUtils.getFilenameOnly(filename);
	final String normalizedFilename = StringNormalizer.normalizePreservingCapitalizedLetters(nicerFilename);
	super.setFilename(normalizedFilename);
	setContentType(guessContentType(normalizedFilename));
    }

    public void setContent(final byte[] bytes) {
	getFileContent().setContent(bytes);
    }

    public ByteArray getContent() {
        return getFileContent().getContent();
    }

    public void setContent(final ByteArray content) {
	getFileContent().setContent(content);
    }

    public void delete() {
	getFileContent().delete();
	removeExpenditureTrackingSystem();
	deleteDomainObject();
    }
    
    public static <T extends File> List<T> getFiles(final Class<T> clazz) {
	final List<T> files = new ArrayList<T>();
	for (final File file : ExpenditureTrackingSystem.getInstance().getFiles()) {
	    if (file.getClass().equals(clazz)) {
		files.add((T)file);
	    }
	}
	return files;
    }

}
