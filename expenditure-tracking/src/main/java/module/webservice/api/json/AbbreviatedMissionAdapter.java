package module.webservice.api.json;

import org.fenixedu.bennu.core.json.JsonBuilder;
import org.fenixedu.bennu.core.json.JsonViewer;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import module.mission.domain.Mission;
import module.mission.domain.MissionProcess;
import module.mission.domain.util.MissionState;

public class AbbreviatedMissionAdapter implements JsonViewer<Mission> {

    @Override
    public JsonElement view(Mission obj, JsonBuilder ctx) {
        return abreviatedArchive(obj.getMissionProcess());
    }

    public JsonObject abreviatedArchive(MissionProcess missionProcess) {
        final JsonObject result = getBasicInformation(missionProcess);

        result.add("missionStates", getMissionStates(missionProcess));
        result.addProperty("numberOfComments", missionProcess.getCommentsSet().size());
        result.addProperty("processUrl",
                CoreConfiguration.getConfiguration().applicationUrl() + "/ForwardToProcess/" + missionProcess.getExternalId());

        return result;
    }

    private JsonElement getMissionStates(MissionProcess missionProcess) {
        final JsonArray jsonArray = new JsonArray();
        for (final MissionState missionState : missionProcess.getMissionStates()) {
            jsonArray.add(this.toJson(missionState, missionProcess));
        }
        return jsonArray;
    }

    public JsonObject getBasicInformation(MissionProcess missionProcess) {
        final JsonObject result = new JsonObject();

        result.addProperty("processNumber", missionProcess.getProcessNumber());
        result.addProperty("processName", missionProcess.getPresentationName());
        result.addProperty("processIdentification", missionProcess.getProcessIdentification());
        result.addProperty("createdBy", missionProcess.getProcessCreator().getUsername());
        result.addProperty("createdOn", missionProcess.getCreationDate().toString("yyyy-MM-dd"));
        result.addProperty("destination", missionProcess.getMission().getDestinationDescription());
        result.addProperty("responsibleParticipant", missionProcess.getMission().getMissionResponsibleName());
        result.addProperty("requester", missionProcess.getMission().getRequestingPerson().getPresentationName());
        result.addProperty("objective", missionProcess.getMission().getObjective());
        result.addProperty("departure", missionProcess.getMission().getDaparture().toString("yyyy-MM-dd HH:mm"));
        result.addProperty("arrival", missionProcess.getMission().getArrival().toString("yyyy-MM-dd HH:mm"));
        result.addProperty("numberOfDays", missionProcess.getMission().getDurationInDays());
        result.addProperty("totalCost", missionProcess.getMission().getTotalPrevisionaryCost().toFormatString());
        result.addProperty("isGrantOwnerEquivalence", missionProcess.getMission().getGrantOwnerEquivalence());
        if (missionProcess.getMission().getServiceGaranteePerson() != null) {
            result.addProperty("whoCheckedLocalServiceConditions",
                    missionProcess.getMission().getServiceGaranteePerson().getPresentationName());
            result.addProperty("serviceConditionsCheckedOn",
                    missionProcess.getMission().getServiceGaranteeInstante().toString("yyyy-MM-dd HH:mm"));
        }

        return result;

    }

    private JsonObject toJson(final MissionState missionState, MissionProcess missionProcess) {
        final JsonObject o = new JsonObject();
        o.addProperty("name", missionState.getLocalizedName());
        o.addProperty("description", missionState.getLocalizedDescription());

        o.addProperty("idle", missionState.isIdle(missionProcess));
        o.addProperty("pending", missionState.isPending(missionProcess));
        o.addProperty("completed", missionState.isCompleted(missionProcess));
        return o;
    }
}
