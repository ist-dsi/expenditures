package module.mission.presentationTier.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;

import org.apache.commons.lang.StringUtils;

import pt.ist.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class MissionProcessFromOtherSystemsProvider implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String currentValue = StringUtils.trim(value);

        final List<MissionProcess> result = new ArrayList<MissionProcess>();
        final Set<MissionSystem> otherSystems = MissionSystem.readAllMissionSystems();
        otherSystems.remove(MissionSystem.getInstance());
        for (MissionSystem otherSystem : otherSystems) {
            for (final MissionProcess missionProcess : otherSystem.getMissionProcessesSet()) {
                String[] processIdParts = missionProcess.getProcessNumber().split("/M");
                if (missionProcess.getProcessIdentification().equals(currentValue)
                        || processIdParts[processIdParts.length - 1].equals(currentValue)) {
                    result.add(missionProcess);
                }
            }
        }
        return result;
    }

}
