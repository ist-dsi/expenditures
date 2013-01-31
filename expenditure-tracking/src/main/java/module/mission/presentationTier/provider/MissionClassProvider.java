package module.mission.presentationTier.provider;

import module.mission.domain.Mission;
import pt.ist.bennu.core.presentationTier.renderers.providers.AbstractDomainClassProvider;

public class MissionClassProvider extends AbstractDomainClassProvider {

	@Override
	protected Class getSuperClass() {
		return Mission.class;
	}

}
