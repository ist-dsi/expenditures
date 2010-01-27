package pt.ist.expenditureTrackingSystem.domain;

import java.io.IOException;
import java.sql.SQLException;

import pt.ist.fenixWebFramework.services.Service;

public class SyncSuppliers extends SyncSuppliers_Base {

    public SyncSuppliers() {
	super();
    }

    @Override
    public void executeTask() {
	try {
	    syncData();
	} catch (final Exception e) {
	    throw new Error(e);
	}
    }

    @Service
    private void syncData() throws IOException, SQLException {
	SyncSuppliersAux.syncData();
    }

    @Override
    public String getLocalizedName() {
	return getClass().getName();
    }

}
