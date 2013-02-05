package module.mission.presentationTier.provider;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import module.mission.domain.MissionItem;
import module.mission.domain.MissionProcess;
import module.mission.domain.activity.ItemActivityInformation;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import dml.DomainClass;
import dml.DomainModel;

public class TopLevelMissionItemTypeProvider implements DataProvider {

    public static Comparator<Class> CLASS_COMPARATOR_BY_NAME = new Comparator<Class>() {

        @Override
        public int compare(final Class o1, final Class o2) {
            return o1.getName().compareTo(o2.getName());
        }

    };

    @Override
    public Converter getConverter() {
        return null;
    }

    @Override
    public Object provide(final Object source, final Object currentValue) {
        final ItemActivityInformation itemActivityInformation = (ItemActivityInformation) source;
        final MissionProcess missionProcess = itemActivityInformation.getProcess();
        final Class clazz = (Class) currentValue;
        return getAvailableTopLevelMissionItemTypes();
    }

    public final Collection<Class> getAvailableTopLevelMissionItemTypes() {
        final Set<Class> missionItemTypes = new TreeSet<Class>(CLASS_COMPARATOR_BY_NAME);
        final DomainModel domainModel = FenixWebFramework.getDomainModel();
        for (final DomainClass domainClass : domainModel.getDomainClasses()) {
            if (isMissionItemDirectSubclass(domainClass)) {
                try {
                    final Class clazz = Class.forName(domainClass.getFullName());
                    missionItemTypes.add(clazz);
                } catch (final ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new Error(e);
                }
            }
        }
        return missionItemTypes;
    }

    private boolean isMissionItemDirectSubclass(final DomainClass domainClass) {
        return domainClass != null && isMissionItem((DomainClass) domainClass.getSuperclass());
    }

    private boolean isMissionItem(final DomainClass domainClass) {
        return domainClass != null && domainClass.getFullName().equals(MissionItem.class.getName());
    }

}
