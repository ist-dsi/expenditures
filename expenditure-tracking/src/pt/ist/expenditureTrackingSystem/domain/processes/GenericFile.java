package pt.ist.expenditureTrackingSystem.domain.processes;

public class GenericFile extends GenericFile_Base {
    
    public GenericFile(String displayName, String filename, byte[] content) {
	super();
	setDisplayName(displayName);
	setFilename(filename);
	setContent(content);
    }
    
}
