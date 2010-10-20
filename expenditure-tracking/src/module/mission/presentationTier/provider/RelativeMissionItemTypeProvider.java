package module.mission.presentationTier.provider;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import module.mission.domain.MissionProcess;
import module.mission.domain.activity.ItemActivityInformation;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;
import dml.DomainClass;
import dml.DomainModel;

public class RelativeMissionItemTypeProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(final Object source, final Object currentValue) {
	final ItemActivityInformation itemActivityInformation = (ItemActivityInformation) source;
	final MissionProcess missionProcess = itemActivityInformation.getProcess();
	final Class topLevelMissionItemType = itemActivityInformation.getTopLevelMissionItemType();
	return getMissionItemTypes(topLevelMissionItemType);
    }

    public Collection<Class> getMissionItemTypes(final Class topLevelMissionItemType) {
	final Set<Class> missionItemTypes = new TreeSet<Class>(TopLevelMissionItemTypeProvider.CLASS_COMPARATOR_BY_NAME);
	if (topLevelMissionItemType != null) {
	    final DomainModel domainModel = FenixWebFramework.getDomainModel();
	    for (final DomainClass domainClass : domainModel.getDomainClasses()) {
		if (isMissionItemSubclass(topLevelMissionItemType, domainClass)) {
		    try {
			final Class clazz = Class.forName(domainClass.getFullName());
			if (!Modifier.isAbstract(clazz.getModifiers())) {
			    missionItemTypes.add(clazz);
			}
		    } catch (final ClassNotFoundException e) {
			throw new Error(e);
		    }
		}
	    }
	}
	return missionItemTypes;
    }

    private boolean isMissionItemSubclass(final Class topLevelMissionItemType, final DomainClass domainClass) {
	return domainClass != null && (domainClass.getFullName().equals(topLevelMissionItemType.getName())
		|| isMissionItemSubclass(topLevelMissionItemType, (DomainClass) domainClass.getSuperclass()));
    }

}
