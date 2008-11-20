package pt.ist.expenditureTrackingSystem.domain.processes;

public class GenericFile extends GenericFile_Base {
    
    public GenericFile(String filename, byte[] content) {
	super();
	setFilename(filename);
	setContent(content);
    }
    
}
