package pt.ist.expenditureTrackingSystem.domain;

import java.util.ResourceBundle;

import myorg.domain.groups.IRoleEnum;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum RoleType implements IRoleEnum, IPresentableEnum {

    TREASURY, ACQUISITION_CENTRAL, ACQUISITION_CENTRAL_MANAGER, ACCOUNTING_MANAGER, PROJECT_ACCOUNTING_MANAGER, MANAGER, SUPPLIER_MANAGER, STATISTICS_VIEWER;

    public String getRepresentation() {
	return getClass().getName() + "." + name();
    }

    @Override
    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
	return resourceBundle.getString(RoleType.class.getSimpleName() + "." + name());
    }

}
