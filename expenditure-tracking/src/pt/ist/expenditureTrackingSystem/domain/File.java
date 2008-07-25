package pt.ist.expenditureTrackingSystem.domain;

public class File extends File_Base {
    
    public File() {
        super();
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }
    
}
