package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.ProjectFinancer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;

public class Project extends Project_Base {

    public Project(final Unit parentUnit, final String name, final String projectCode) {
        super();
    	setName(name);
    	setProjectCode(projectCode);
    	setParentUnit(parentUnit);
    }

    @Override
    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result, final boolean recurseSubUnits) {
	final String projectCode = getProjectCode();
	if (projectCode != null) {
	    for (final AcquisitionProcess acquisitionProcess : GenericProcess.getAllProcesses(RegularAcquisitionProcess.class)) {
		if (acquisitionProcess.getPayingUnits().contains(this) && acquisitionProcess.isPendingApproval())
		    result.add(acquisitionProcess);
	    }
	}
	super.findAcquisitionProcessesPendingAuthorization(result, recurseSubUnits);
    }

    @Override
    public String getPresentationName() {
	return super.getPresentationName() + " (p. " + getProjectCode() + ")";
    }

    @Override
    public String getShortIdentifier() {
	return getProjectCode();
    }

    @Override
    public Financer finance(final AcquisitionRequest acquisitionRequest) {
	return new ProjectFinancer(acquisitionRequest, this);
    }

    @Override
    public boolean isProjectAccountingEmployee(final Person person) {
	final AccountingUnit accountingUnit = getAccountingUnit();
	return accountingUnit != null && accountingUnit.hasProjectAccountants(person);
    }

    public static Project findProjectByCode(String projectCode) {
	for (Unit unit : ExpenditureTrackingSystem.getInstance().getUnits()) {
	  if (unit instanceof Project) {
	      if (((Project)unit).getProjectCode().equals(projectCode)) {
		  return (Project)unit;
	      }
	  }
	}
	return null;
    }
}
