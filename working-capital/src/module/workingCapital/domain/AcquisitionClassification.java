package module.workingCapital.domain;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class AcquisitionClassification extends AcquisitionClassification_Base {

    public static final Comparator<AcquisitionClassification> COMPARATOR_BY_DESCRIPTION = new Comparator<AcquisitionClassification>() {

	@Override
	public int compare(final AcquisitionClassification o1, final AcquisitionClassification o2) {
	    final int c = o1.getDescription().compareTo(o2.getDescription());
	    return c == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : c;
	}
	
    };

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

    public static SortedSet<AcquisitionClassification> getAvailableClassifications() {
	final SortedSet<AcquisitionClassification> result = new TreeSet<AcquisitionClassification>(COMPARATOR_BY_DESCRIPTION);
	result.addAll(WorkingCapitalSystem.getInstance().getAcquisitionClassificationsSet());
	return result;
    }
    
}
