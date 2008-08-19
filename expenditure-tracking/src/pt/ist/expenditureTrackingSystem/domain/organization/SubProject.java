package pt.ist.expenditureTrackingSystem.domain.organization;

public class SubProject extends SubProject_Base {
    
    public SubProject(final Project parentUnit, final String name) {
        super();
    	setName(name);
    	setParentUnit(parentUnit);
    }

    @Override
    public String getPresentationName() {
	return getParentUnit().getPresentationName() + " - " + super.getPresentationName();
    }

}
