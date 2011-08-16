package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AcquisitionItemClassification implements IPresentableEnum {

    GOODS,

    SERVICES;

    @Override
    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureEnumerationResources",
		Language.getLocale());
	return resourceBundle.getString(this.getClass().getSimpleName() + "." + name());
    }

}
