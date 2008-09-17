package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import org.apache.commons.lang.StringUtils;

import pt.ist.expenditureTrackingSystem.domain.DomainException;
import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;

public class AcquisitionProcessState extends AcquisitionProcessState_Base {

    protected AcquisitionProcessState() {
	super();
	setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
    }

    public AcquisitionProcessState(final AcquisitionProcess process, final AcquisitionProcessStateType processStateType) {
	this();
	final Person person = getPerson();
	checkArguments(process, processStateType, person);
	super.initFields(process, person);
	setAcquisitionProcessStateType(processStateType);
    }

    public AcquisitionProcessState(final AcquisitionProcess process, final AcquisitionProcessStateType processStateType, final String justification) {
	this(process, processStateType);
	if (!StringUtils.isEmpty(justification)) {
	    setJustification(justification);
	}
    }
    
    private void checkArguments(AcquisitionProcess acquisitionProcess, AcquisitionProcessStateType acquisitionProcessStateType,
	    Person person) {
	if (acquisitionProcessStateType == null) {
	    throw new DomainException("error.wrong.AcquisitionProcessState.arguments");
	}
	super.checkArguments(acquisitionProcess, person);
    }

    public String getLocalizedName() {
	return getAcquisitionProcessStateType().getLocalizedName();
    }
}
