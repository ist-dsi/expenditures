package pt.ist.expenditureTrackingSystem.domain.processes;

import java.util.HashSet;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public abstract class GenericProcess extends GenericProcess_Base {
    
    public  GenericProcess() {
        super();
        setOjbConcreteClass(getClass().getName());
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

     public static <T extends GenericProcess> Set<T> getAllProcesses(Class<T> processClass) {
	Set<T> classes = new HashSet<T>();
	for (GenericProcess process : ExpenditureTrackingSystem.getInstance().getProcessesSet()) {
	    if (process.getClass().equals(processClass)) {
		classes.add((T)process);
	    }
	}
	return classes;
    }
     
     public abstract <T extends GenericProcess> AbstractActivity<T> getActivityByName(String name);
    
}
