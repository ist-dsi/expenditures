package pt.ist.expenditureTrackingSystem.domain.organization;

import java.util.Set;

import module.organization.domain.Party;
import module.organization.domain.PartyType;
import module.organizationIst.domain.IstPartyType;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.AcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RegularAcquisitionProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.RequestWithPayment;
import pt.ist.expenditureTrackingSystem.domain.processes.GenericProcess;
import pt.ist.fenixframework.plugins.luceneIndexing.domain.IndexDocument;
import dml.runtime.RelationAdapter;

public class CostCenter extends CostCenter_Base {

    public static class CostCenterPartyTypeListener extends RelationAdapter<Party, PartyType> {

	@Override
	public void afterAdd(final Party party, final PartyType partyType) {
	    if (party.isUnit() && partyType != null && partyType == PartyType.readBy(IstPartyType.COST_CENTER.getType())) {
		new CostCenter((module.organization.domain.Unit) party);
	    }
	}

    }

    static {
	Party.PartyTypeParty.addListener(new CostCenterPartyTypeListener());
    }

    public CostCenter(final module.organization.domain.Unit unit) {
	super();
	setUnit(unit);
    }

    public CostCenter(final Unit parentUnit, final String name, final String costCenter) {
	super();
	createRealUnit(this, parentUnit, IstPartyType.COST_CENTER, costCenter, name);

	// TODO : After this object is refactored to retrieve the name and
	// parent from the real unit,
	// the following three lines may be deleted.
	setName(name);
	setCostCenter(costCenter);
	setParentUnit(parentUnit);
    }

    public void setCostCenter(final String costCenter) {
	getUnit().setAcronym("CC. " + costCenter);
    }

    public String getCostCenter() {
	return getUnit().getAcronym().substring(4);
    }

    @Override
    public void findAcquisitionProcessesPendingAuthorization(final Set<AcquisitionProcess> result, final boolean recurseSubUnits) {
	final String costCenter = getCostCenter();
	if (costCenter != null) {
	    for (final AcquisitionProcess acquisitionProcess : GenericProcess.getAllProcesses(RegularAcquisitionProcess.class)) {
		if (acquisitionProcess.getPayingUnits().contains(this) && acquisitionProcess.isPendingApproval())
		    result.add(acquisitionProcess);
	    }
	}
    }

    @Override
    public String getPresentationName() {
	return "(CC. " + getCostCenter() + ") " + super.getPresentationName();
    }

    @Override
    public String getShortIdentifier() {
	return getCostCenter();
    }

    @Override
    public boolean isAccountingEmployee(final Person person) {
	final AccountingUnit accountingUnit = getAccountingUnit();
	return accountingUnit != null && accountingUnit.hasPeople(person);
    }

    @Override
    public Financer finance(final RequestWithPayment acquisitionRequest) {
	return new Financer(acquisitionRequest, this);
    }

    @Override
    public CostCenter getCostCenterUnit() {
	return this;
    }

    @Override
    public IndexDocument getDocumentToIndex() {
	IndexDocument document = super.getDocumentToIndex();
	document.indexField(UnitIndexFields.NUMBER_INDEX, getCostCenter());
	return document;
    }
}
