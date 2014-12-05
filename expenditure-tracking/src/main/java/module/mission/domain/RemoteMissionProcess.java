package module.mission.domain;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;

import pt.ist.expenditureTrackingSystem._development.ExpenditureConfiguration;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.representation.Form;

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
        final String post =
                new Client().resource(getRemoteMissionSystem().getConnectUrl()).queryParams(getParams()).post(String.class);
        JsonParser parser = new JsonParser();
        final JsonElement element = parser.parse(post);
        final JsonObject object = (JsonObject) element;
        setRemoteOid(object.get("externalId").getAsString());
    }

    public void disconnect() {
        final String post =
                new Client().resource(getRemoteMissionSystem().getDisconnectUrl()).queryParams(getParams()).get(String.class);
        System.out.println(post);
    }

    private Form getParams() {
        final User user = Authenticate.getUser();
        final Form params = new Form();
        params.add("processNumber", getMissionProcess().getProcessNumber());
        params.add("externalId", getMissionProcess().getExternalId());
        params.add("hostname", CoreConfiguration.getConfiguration().applicationUrl());
        params.add("remoteProcessNumber", getProcessNumber());
        params.add("username", user == null ? null : user.getUsername());
        params.add("access_token", ExpenditureConfiguration.get().apiToken());
        return params;
    }

    public void delete() {
        setMissionProcess(null);
        setRemoteMissionSystem(null);
        super.deleteDomainObject();
    }

}
