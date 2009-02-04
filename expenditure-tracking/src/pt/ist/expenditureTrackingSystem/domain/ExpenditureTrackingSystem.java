package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.MyOrg;
import pt.ist.fenixWebFramework.services.Service;

public class ExpenditureTrackingSystem extends ExpenditureTrackingSystem_Base {

    private static ExpenditureTrackingSystem instance = null;

    public static ExpenditureTrackingSystem getInstance() {
	if (instance == null) {
	    initialize();
	}
	return instance;
    }

    @Service
    public synchronized static void initialize() {
	if (instance == null) {
	    final MyOrg myOrg = MyOrg.getInstance();
	    instance = myOrg.getExpenditureTrackingSystem();
	    if (instance == null) {
		instance = new ExpenditureTrackingSystem();
	    }
	}
    }

    private ExpenditureTrackingSystem() {
	super();
	setMyOrg(MyOrg.getInstance());
	setAcquisitionRequestDocumentCounter(0);
    }

    public String nextAcquisitionRequestDocumentID() {
	return "D" + getAndUpdateNextAcquisitionRequestDocumentCountNumber();
    }

    public Integer nextAcquisitionRequestDocumentCountNumber() {
	return getAndUpdateNextAcquisitionRequestDocumentCountNumber();
    }

    private Integer getAndUpdateNextAcquisitionRequestDocumentCountNumber() {
	setAcquisitionRequestDocumentCounter(getAcquisitionRequestDocumentCounter().intValue() + 1);
	return getAcquisitionRequestDocumentCounter();
    }

}
