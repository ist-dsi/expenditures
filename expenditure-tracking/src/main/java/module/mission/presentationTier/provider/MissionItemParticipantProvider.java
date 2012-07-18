package module.mission.presentationTier.provider;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.domain.activity.ItemActivityInformation;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class MissionItemParticipantProvider implements DataProvider {

    @Override
    public Converter getConverter() {
	return null;
    }

    @Override
    public Object provide(final Object source, final Object currentValue) {
	final ItemActivityInformation itemActivityInformation = (ItemActivityInformation) source;
	final MissionProcess missionProcess = itemActivityInformation.getProcess();
	final Mission mission = missionProcess.getMission();
	return mission.getOrderedParticipants();
    }

}
