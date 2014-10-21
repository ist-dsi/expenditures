package module.mission.domain;

public class RemoteMissionSystem extends RemoteMissionSystem_Base {
    
    protected RemoteMissionSystem() {
        super();
        setMissionSystem(MissionSystem.getInstance());
    }

    public RemoteMissionSystem(final String name, final String hostname, final String connectUrl, final String disconnectUrl, final String processUrl) {
        this();
        setName(name);
        setHostname(hostname);
        setConnectUrl(connectUrl);
        setDisconnectUrl(disconnectUrl);
        setProcessUrl(processUrl);
    }

    public static RemoteMissionSystem find(final String hostname) {
        for (final RemoteMissionSystem system : MissionSystem.getInstance().getRemoteMissionSystemSet()) {
            if (system.getHostname().equals(hostname)) {
                return system;
            }
        }
        return null;
    }
    
}
