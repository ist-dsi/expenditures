package module.mission.domain;

import javax.ws.rs.client.ClientBuilder;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RemoteMissionProcess extends RemoteMissionProcess_Base {

    protected RemoteMissionProcess() {
        super();
    }

    public RemoteMissionProcess(final MissionProcess missionProcess, final RemoteMissionSystem remoteMissionSystem,
            final String processNumber, final String externalId) {
        setMissionProcess(missionProcess);
        setProcessNumber(processNumber);
        setRemoteOid(externalId);
        setRemoteMissionSystem(remoteMissionSystem);
    }

    public void connect() {
//        try {
        final User user = Authenticate.getUser();
        final String post =
                ClientBuilder.newClient().target(getRemoteMissionSystem().getConnectUrl())
                        .queryParam("processNumber", getMissionProcess().getProcessNumber())
                        .queryParam("externalId", getMissionProcess().getExternalId())
                        .queryParam("hostname", CoreConfiguration.getConfiguration().applicationUrl())
                        .queryParam("remoteProcessNumber", getProcessNumber())
                        .queryParam("username", user == null ? null : user.getUsername())
                        .queryParam("access_token", ExpenditureConfiguration.get().apiToken()).request()
                        .header("X-Requested-With", "XMLHttpRequest")
                        .post(null, String.class);
        JsonParser parser = new JsonParser();
        final JsonElement element = parser.parse(post);
        final JsonObject object = (JsonObject) element;
        setRemoteOid(object.get("externalId").getAsString());
//        } catch (final UniformInterfaceException ex) {
//            final ClientResponse response = ex.getResponse();
//            if (response != null) {
//                if (response.getStatus() == 400) {
//                    throw new DomainException(Bundle.EXPENDITURE, "bad.mission.number");
//                }
//            }
//            throw new Error(ex);
//        }
    }

    public void disconnect() {
        final User user = Authenticate.getUser();
        ClientBuilder.newClient().target(getRemoteMissionSystem().getConnectUrl())
                .queryParam("processNumber", getMissionProcess().getProcessNumber())
                .queryParam("externalId", getMissionProcess().getExternalId())
                .queryParam("hostname", CoreConfiguration.getConfiguration().applicationUrl())
                .queryParam("remoteProcessNumber", getProcessNumber())
                .queryParam("username", user == null ? null : user.getUsername())
                .queryParam("access_token", ExpenditureConfiguration.get().apiToken()).request()
                .header("X-Requested-With", "XMLHttpRequest")
                .get(String.class);
    }

    public void delete() {
        setMissionProcess(null);
        setRemoteMissionSystem(null);
        super.deleteDomainObject();
    }

}
