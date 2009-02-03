package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.util.ByteArray;
import pt.ist.fenixframework.pstm.Transaction;

public class FileContent extends FileContent_Base {
    
    public FileContent(final File file) {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
	setFile(file);
    }

    public void setContent(final byte[] bytes) {
	final ByteArray byteArray = new ByteArray(bytes);
	setContent(byteArray);
    }

    public void delete() {
	removeFile();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

}
