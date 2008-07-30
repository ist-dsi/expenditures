package pt.ist.expenditureTrackingSystem.presentationTier.util;

import java.io.InputStream;
import java.io.Serializable;

public class FileUploadBean implements Serializable {

    private transient InputStream inputStream;
    private String filename;

    public FileUploadBean() {
    }

    public InputStream getInputStream() {
	return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
	this.inputStream = inputStream;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

}
