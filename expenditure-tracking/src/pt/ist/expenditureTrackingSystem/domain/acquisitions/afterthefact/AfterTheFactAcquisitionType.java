package pt.ist.expenditureTrackingSystem.domain.acquisitions.afterthefact;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum AfterTheFactAcquisitionType implements IPresentableEnum {

    REFUND, WORKING_CAPITAL, PURCHASE;

    @Override
    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", Language.getLocale());
	return resourceBundle.getString(AfterTheFactAcquisitionType.class.getSimpleName() + "." + name());
    }

}
