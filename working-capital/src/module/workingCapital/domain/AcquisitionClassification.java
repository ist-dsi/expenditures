package module.workingCapital.domain;

public class AcquisitionClassification extends AcquisitionClassification_Base {
    
    public AcquisitionClassification() {
        super();
        setWorkingCapitalSystem(WorkingCapitalSystem.getInstance());
    }

    public AcquisitionClassification(final String description, final String economicClassification, final String pocCode) {
	this();
	setDescription(description);
	setEconomicClassification(economicClassification);
	setPocCode(pocCode);
    }
    
}
