package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import java.util.ArrayList;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.acquisitions.refund.RefundProcess;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.simplified.SimplifiedProcedureProcess;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AcquisitionProcessesTypesProvider implements DataProvider {

	public Converter getConverter() {
		return new BiDirectionalConverter() {

			@Override
			public Object convert(Class type, Object value) {
				try {
					return Class.forName((String) value);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			public String deserialize(Object object) {
				return ((Class) object).getName();
			}

		};
	}

	public Object provide(Object arg0, Object arg1) {
		List<Class> classes = new ArrayList<Class>();
		classes.add(RefundProcess.class);
		classes.add(SimplifiedProcedureProcess.class);
		return classes;
	}

}
