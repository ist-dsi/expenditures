package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import java.util.ArrayList;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.Financer;
import pt.ist.expenditureTrackingSystem.domain.organization.AccountingUnit;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class UnitAccountingUnitsProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {
	List<AccountingUnit> res = new ArrayList<AccountingUnit>();
	Financer financer = (Financer) source;
	res.add(financer.getUnit().getAccountingUnit());
	// AccountingUnit.
	return null;
    }
}
