package pt.ist.expenditureTrackingSystem.domain.organization;

import java.math.BigDecimal;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessState;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcessStateType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionRequest;
import pt.ist.expenditureTrackingSystem.domain.authorizations.Authorization;
import pt.ist.expenditureTrackingSystem.domain.dto.CreateUnitBean;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.Transaction;

public class Unit extends Unit_Base {

    public Unit(final Unit parentUnit) {
	super();
	final ExpenditureTrackingSystem expenditureTrackingSystem = ExpenditureTrackingSystem.getInstance();
	setExpenditureTrackingSystem(expenditureTrackingSystem);
	if (parentUnit == null) {
	    setExpenditureTrackingSystemFromTopLevelUnit(expenditureTrackingSystem);
	}
	setParentUnit(parentUnit);
    }

    public Unit(final Unit parentUnit, final String name, final String costCenter) {
	this(parentUnit);
	setName(name);
	setCostCenter(costCenter);
    }

    @Service
    public static Unit createNewUnit(final CreateUnitBean createUnitBean) {
	return new Unit(createUnitBean.getParentUnit(), createUnitBean.getName(), createUnitBean.getCostCenter());
    }

    @Service
    public void delete() {
	for (final Unit unit : getSubUnitsSet()) {
	    unit.delete();
	}
	for (final Authorization authorizations : getAuthorizationsSet()) {
	    authorizations.delete();
	}
	removeExpenditureTrackingSystemFromTopLevelUnit();
	removeParentUnit();
	removeExpenditureTrackingSystem();
	Transaction.deleteObject(this);
    }

    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result, final boolean recurseSubUnits) {
	final String costCenter = getCostCenter();
	if (costCenter != null) {
	    for (final AcquisitionProcess acquisitionProcess : ExpenditureTrackingSystem.getInstance()
		    .getAcquisitionProcessesSet()) {
		if (costCenter.equals(acquisitionProcess.getAcquisitionRequest().getCostCenter())) {
		    if (acquisitionProcess.isPendingApproval()) {
			result.add(acquisitionProcess);
		    }
		}
	    }
	}
	if (recurseSubUnits) {
	    for (final Unit unit : getSubUnitsSet()) {
		unit.findAcquisitionProcessesPendingAuthorization(result, recurseSubUnits);
	    }
	}
    }

    public BigDecimal getTotalAllocated() {
	BigDecimal result = BigDecimal.ZERO;
	final String costCenter = getCostCenter();
	if (costCenter != null) {
	    for (final AcquisitionRequest acquisitionRequest : ExpenditureTrackingSystem.getInstance()
		    .getAcquisitionRequestsSet()) {
		if (costCenter.equals(acquisitionRequest.getCostCenter())) {
		    final AcquisitionProcess acquisitionProcess = acquisitionRequest.getAcquisitionProcess();
		    final AcquisitionProcessState acquisitionProcessState = acquisitionProcess.getAcquisitionProcessState();
		    final AcquisitionProcessStateType acquisitionProcessStateType = acquisitionProcessState
			    .get$acquisitionProcessStateType();
		    if (acquisitionProcessStateType.compareTo(AcquisitionProcessStateType.FUNDS_ALLOCATED) >= 0) {
			result = result.add(acquisitionRequest.getTotalItemValue());
		    }
		}
	    }
	}
	for (final Unit unit : getSubUnitsSet()) {
	    result = result.add(unit.getTotalAllocated());
	}
	return result;
    }

    public static Unit findUnitByCostCenter(final String costCenter) {
	for (final Unit unit : ExpenditureTrackingSystem.getInstance().getTopLevelUnitsSet()) {
	    final Unit result = unit.findByCostCenter(costCenter);
	    if (result != null) {
		return result;
	    }
	}
	return null;
    }

    protected Unit findByCostCenter(final String costCenter) {
	if (getCostCenter() != null && getCostCenter().equals(costCenter)) {
	    return this;
	}
	for (final Unit unit : getSubUnitsSet()) {
	    final Unit result = unit.findByCostCenter(costCenter);
	    if (result != null) {
		return unit;
	    }
	}
	return null;
    }

}
