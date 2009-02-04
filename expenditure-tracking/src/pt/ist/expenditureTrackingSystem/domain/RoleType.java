package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.groups.IRoleEnum;

public enum RoleType implements IRoleEnum {

    TREASURY, ACQUISITION_CENTRAL, ACQUISITION_CENTRAL_MANAGER, ACCOUNTING_MANAGER, PROJECT_ACCOUNTING_MANAGER, MANAGER, SUPPLIER_MANAGER, STATISTICS_VIEWER;

    public String getRepresentation() {
	return getClass().getName() + "." + name();
    }
}
