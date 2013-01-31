package pt.ist.expenditureTrackingSystem.presentationTier.renderers.dataProvider;

import java.util.ArrayList;
import java.util.List;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;
import pt.ist.expenditureTrackingSystem.domain.SavedSearch;
import pt.ist.expenditureTrackingSystem.domain.dto.UserSearchBean;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AvailableSavedSearchesProvider implements DataProvider {

	public Converter getConverter() {
		return new DomainObjectKeyConverter();
	}

	public Object provide(Object source, Object currentValue) {
		UserSearchBean bean = (UserSearchBean) source;
		List<SavedSearch> searches = new ArrayList<SavedSearch>();
		searches.addAll(ExpenditureTrackingSystem.getInstance().getSystemSearches());
		searches.addAll(bean.getUser().getSaveSearches());
		return searches;
	}

}
