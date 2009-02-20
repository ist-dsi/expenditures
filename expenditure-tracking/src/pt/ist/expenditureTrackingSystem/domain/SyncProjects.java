package pt.ist.expenditureTrackingSystem.domain;

import java.io.IOException;
import java.sql.SQLException;

public class SyncProjects extends SyncProjects_Base {

    public SyncProjects() {
        super();
    }

    @Override
    public void executeTask() {
	try {
	    final SyncProjectsAux syncProjectsAux = new SyncProjectsAux();
	    syncProjectsAux.syncData();
	} catch (final IOException e) {
	    throw new Error(e);
	} catch (final SQLException e) {
	    throw new Error(e);
	}
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }

}
