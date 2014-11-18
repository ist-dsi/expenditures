package module.mission.presentationTier.provider;

import module.mission.domain.Mission;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import pt.ist.fenixframework.DomainModelUtil;

public class MissionClassProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(Object arg0, Object arg1) {
        return DomainModelUtil.getDomainClassHierarchy(getSuperClass(), shouldContainSuperClass(),
                shouldContainerAbstractClasses());
    }

    protected Class getSuperClass() {
        return Mission.class;
    }

    protected boolean shouldContainSuperClass() {
        return false;
    }

    protected boolean shouldContainerAbstractClasses() {
        return true;
    }

}
