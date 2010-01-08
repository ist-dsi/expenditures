package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.activities.CreateAcquisitionRequestItemActivityInformation;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class DeliveryInfoProvider implements DataProvider {

    public Converter getConverter() {
	return new DomainObjectKeyConverter();
    }

    public Object provide(Object source, Object currentValue) {
	CreateAcquisitionRequestItemActivityInformation information = (CreateAcquisitionRequestItemActivityInformation) source;
	return information.getAcquisitionRequest().getRequester().getDeliveryInfosSet();
    }

}
