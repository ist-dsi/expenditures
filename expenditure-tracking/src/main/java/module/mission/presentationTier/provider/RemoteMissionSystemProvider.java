package module.mission.presentationTier.provider;

import module.mission.domain.MissionSystem;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class RemoteMissionSystemProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        return MissionSystem.getInstance().getRemoteMissionSystemSet();
    }

}
