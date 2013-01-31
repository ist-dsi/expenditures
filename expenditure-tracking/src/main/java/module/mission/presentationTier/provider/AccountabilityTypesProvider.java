package module.mission.presentationTier.provider;

import java.util.ArrayList;
import java.util.List;

import module.mission.domain.MissionSystem;
import module.organization.domain.AccountabilityType;
import module.organization.domain.OrganizationalModel;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class AccountabilityTypesProvider implements DataProvider {

	@Override
	public Converter getConverter() {
		return null;
	}

	@Override
	public Object provide(Object source, Object currentValue) {
		final List<AccountabilityType> result = new ArrayList<AccountabilityType>();
		final MissionSystem missionSystem = MissionSystem.getInstance();
		final OrganizationalModel organizationalModel = missionSystem.getOrganizationalModel();
		if (organizationalModel != null) {
			result.addAll(organizationalModel.getAccountabilityTypesSet());
		}
		return result;
	}

}
