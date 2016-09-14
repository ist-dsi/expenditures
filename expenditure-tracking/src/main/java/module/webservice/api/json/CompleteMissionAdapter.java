package module.webservice.api.json;

import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Stream;

import org.fenixedu.bennu.core.annotation.DefaultJsonAdapter;
import org.fenixedu.bennu.core.json.JsonBuilder;
import org.joda.time.DateTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import module.mission.domain.Mission;
import module.mission.domain.MissionFinancer;
import module.mission.domain.MissionItem;
import module.mission.domain.MissionItemFinancer;
import module.mission.domain.MissionProcess;
import module.mission.domain.MissionProcessLateJustification;
import module.mission.domain.PersonMissionAuthorization;
import module.organization.domain.Person;
import module.workflow.domain.ProcessFile;
import module.workflow.domain.WorkflowLog;
import module.workflow.domain.WorkflowProcessComment;
import pt.ist.expenditureTrackingSystem.domain.acquisitions.PaymentProcess;

@DefaultJsonAdapter(Mission.class)
public class CompleteMissionAdapter extends AbbreviatedMissionAdapter {

    @Override
    public JsonElement view(Mission obj, JsonBuilder ctx) {
        return completeArchive(obj.getMissionProcess());
    }

    private DateTime getLastAuthorizationDate(MissionProcess missionProcess) {
        return missionProcess.getExecutionLogStream().filter(l -> l.getDescription().startsWith("Autorizar Despesa"))
                .max(WorkflowLog.COMPARATOR_BY_WHEN).map(l -> l.getWhenOperationWasRan()).orElse(null);
    }

    public JsonObject completeArchive(MissionProcess missionProcess) {
        final JsonObject result = super.abreviatedArchive(missionProcess);

        result.add("paymentProcesses", getPaymentProcesses(missionProcess));
        result.add("associatedMissionsProcesses", getAssociatedMissionsProcesses(missionProcess));
        result.add("financers", getFinanciers(missionProcess));
        result.add("items", getItems(missionProcess));
        result.add("participantAuthorizations", getParticipantAuthorizations(missionProcess));
        result.add("justifications", getJustifications(missionProcess));
        result.add("files", getFiles(missionProcess));
        result.add("comments", getComments(missionProcess));
        result.add("logs", getLogs(missionProcess));

        return result;
    }

    public JsonArray getLogs(MissionProcess missionProcess) {
        return missionProcess.getExecutionLogStream()
                .filter(l -> !l.getWhenOperationWasRan().isAfter(getLastAuthorizationDate(missionProcess)))
                .map(CompleteMissionAdapter::toJson).collect(toJsonArray());
    }

    public JsonArray getComments(MissionProcess missionProcess) {
        return missionProcess.getCommentsSet().stream()
                .filter(c -> c.getDate().isBefore(getLastAuthorizationDate(missionProcess))).map(CompleteMissionAdapter::toJson)
                .collect(toJsonArray());
    }

    public Stream<ProcessFile> getFilesStream(MissionProcess missionProcess) {
        return Stream.concat(missionProcess.getFilesSet().stream(), missionProcess.getDeletedFilesSet().stream())
                .filter(f -> f.getCreationDate().isBefore(getLastAuthorizationDate(missionProcess)));
    }

    public JsonArray getFiles(MissionProcess missionProcess) {
        return getFilesStream(missionProcess).map(CompleteMissionAdapter::toJson).collect(toJsonArray());
    }

    public JsonArray getJustifications(MissionProcess missionProcess) {
        return missionProcess.getOrderedMissionProcessLateJustificationsSet().stream()
                .filter(j -> j.getJustificationDateTime().isBefore(getLastAuthorizationDate(missionProcess)))
                .map(CompleteMissionAdapter::toJson).collect(toJsonArray());
    }

    public JsonArray getParticipantAuthorizations(MissionProcess missionProcess) {
        return missionProcess.getMission().getParticipantAuthorizations().entrySet().stream().map(CompleteMissionAdapter::toJson)
                .collect(toJsonArray());
    }

    public JsonArray getItems(MissionProcess missionProcess) {
        return missionProcess.getMission().getMissionVersion().getMissionItemsSet().stream().map(CompleteMissionAdapter::toJson)
                .collect(toJsonArray());
    }

    public JsonArray getFinanciers(MissionProcess missionProcess) {
        final JsonArray financers = missionProcess.getMission().getMissionVersion().getFinancerSet().stream()
                .map(CompleteMissionAdapter::toJson).collect(toJsonArray());
        return financers;
    }

    public JsonArray getPaymentProcesses(MissionProcess missionProcess) {
        final JsonArray paymentProcesses =
                missionProcess.getPaymentProcessSet().stream().map(CompleteMissionAdapter::toJson).collect(toJsonArray());
        return paymentProcesses;
    }

    public JsonArray getAssociatedMissionsProcesses(MissionProcess missionProcess) {
        final JsonArray associatedMissionsProcesses = missionProcess.getAssociatedMissionProcesses().stream()
                .map(CompleteMissionAdapter::toJson).collect(toJsonArray());
        return associatedMissionsProcesses;
    }

    private static JsonObject toJson(final MissionProcess missionProcess) {
        final JsonObject o = new JsonObject();
        o.addProperty("processNumber", missionProcess.getProcessNumber());
        return o;
    }

    private static JsonObject toJson(final PaymentProcess paymentProcess) {
        final JsonObject o = new JsonObject();
        o.addProperty("processNumber", paymentProcess.getAcquisitionProcessId());
        return o;
    }

    private static JsonObject toJson(final ProcessFile processFile) {
        final JsonObject o = new JsonObject();
        o.addProperty("name", processFile.getDisplayName());
        o.addProperty("filename", processFile.getFilename());
        o.addProperty("contentType", processFile.getContentType());
        o.addProperty("size", processFile.getSize());
        o.addProperty("createdOn", processFile.getCreationDate().toString("yyyy-MM-dd HH:mm"));
        o.addProperty("checksum", processFile.getChecksum());
        o.addProperty("checksumAlgorithm", processFile.getChecksumAlgorithm());
        o.addProperty("isDeleted", processFile.isArchieved());
        return o;
    }

    private static JsonObject toJson(final Entry<Person, PersonMissionAuthorization> entry) {
        final JsonObject o = new JsonObject();
        o.addProperty("participant", entry.getKey().getPresentationName());
        final JsonArray authorizations = new JsonArray();
        for (PersonMissionAuthorization auth = entry.getValue(); auth != null; auth = auth.getNext()) {
            authorizations.add(toJson(auth));
        }
        o.add("authorizations", authorizations);
        return o;
    }

    private static JsonObject toJson(final PersonMissionAuthorization auth) {
        final JsonObject o = new JsonObject();
        o.addProperty("unit", auth.getUnit().getPresentationName());
        if (auth.getAuthority() != null) {
            o.addProperty("authority", auth.getAuthority().getPresentationName());
        }
        if (auth.getAuthorizationDateTime() != null) {
            o.addProperty("authorizedOn", auth.getAuthorizationDateTime().toString("yyyy-MM-dd HH:mm"));
        }
        return o;
    }

    private static JsonObject toJson(final WorkflowLog log) {
        final JsonObject o = new JsonObject();
        o.addProperty("when", log.getWhenOperationWasRan().toString("yyyy-MM-dd HH:mm"));
        o.addProperty("who", log.getActivityExecutor().getUsername());
        o.addProperty("what", log.getDescription());
        return o;
    }

    private static JsonObject toJson(final WorkflowProcessComment comment) {
        final JsonObject o = new JsonObject();
        o.addProperty("when", comment.getDate().toString("yyyy-MM-dd HH:mm"));
        o.addProperty("who", comment.getCommenter().getUsername());
        o.addProperty("comment", comment.getComment());
        return o;
    }

    private static JsonObject toJson(final MissionProcessLateJustification justification) {
        final JsonObject o = new JsonObject();
        o.addProperty("when", justification.getJustificationDateTime().toString());
        o.addProperty("who", justification.getPerson().getPresentationName());
        o.addProperty("justification", justification.getJustification());
        return o;
    }

    private static JsonObject toJson(final MissionFinancer financer) {
        final JsonObject o = new JsonObject();
        o.addProperty("unit", financer.getUnit().getPresentationName());
        o.addProperty("accountingUnit", financer.getAccountingUnit().getName());
        o.addProperty("amount", financer.getAmount().toFormatString());
        o.addProperty("fundAllocations", financer.getFundAllocationId());
        o.addProperty("projectFundAllocations", financer.getProjectFundAllocationId());
        o.addProperty("commitmentNumber", financer.getCommitmentNumber());
        if (financer.getApproval() != null) {
            o.addProperty("approvedBy", financer.getApproval().getPerson().getFirstAndLastName() + "("
                    + financer.getApproval().getPerson().getUsername() + ")");
        }

        if (financer.getAuthorization() != null) {
            o.addProperty("authorizedBy", financer.getAuthorization().getPerson().getFirstAndLastName() + "("
                    + financer.getAuthorization().getPerson().getUsername() + ")");
        }
        return o;
    }

    private static JsonObject toJson(final MissionItem item) {
        final JsonObject o = new JsonObject();
        o.addProperty("item", item.getLocalizedName());
        o.addProperty("description", item.getItemDescription());
        o.addProperty("cost", item.getPrevisionaryCosts().toFormatString());

        final JsonArray participants = item.getPeopleSet().stream().map(CompleteMissionAdapter::toJson).collect(toJsonArray());
        o.add("participants", participants);

        final JsonArray financers =
                item.getMissionItemFinancersSet().stream().map(CompleteMissionAdapter::toJson).collect(toJsonArray());
        o.add("financers", financers);

        return o;
    }

    private static JsonElement toJson(final Person p) {
        return new JsonPrimitive(p.getPresentationName());
    }

    private static JsonObject toJson(final MissionItemFinancer mif) {
        final JsonObject o = new JsonObject();
        o.addProperty("unit", mif.getMissionFinancer().getUnit().getPresentationName());
        o.addProperty("amount", mif.getAmount().toFormatString());
        o.addProperty("fundAllocationId", mif.getFundAllocationId());
        return o;
    }

    private static <T extends JsonElement> Collector<T, JsonArray, JsonArray> toJsonArray() {
        return Collector.of(JsonArray::new, (array, element) -> array.add(element), (one, other) -> {
            one.addAll(other);
            return one;
        }, Characteristics.IDENTITY_FINISH);
    }
}
