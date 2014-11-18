package module.mission.presentationTier.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

public class MissionProcessFromOtherSystemsProvider implements AutoCompleteProvider<MissionProcess> {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
        final String currentValue = StringUtils.trim(value);

        final List<MissionProcess> result = new ArrayList<MissionProcess>();
        for (final MissionProcess missionProcess : MissionSystem.getInstance().getMissionProcessesSet()) {
            String[] processIdParts = missionProcess.getProcessNumber().split("/M");
            if (missionProcess.getProcessIdentification().equals(currentValue)
                    || processIdParts[processIdParts.length - 1].equals(currentValue)) {
                result.add(missionProcess);
            }
        }
        return result;
    }

}
