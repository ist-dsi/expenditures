package module.workingCapital.domain.util;

import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PaymentMethod implements IPresentableEnum {

    CHECK, WIRETRANSFER;

    @Override
    public String getLocalizedName() {
	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.WorkingCapitalResources", Language.getLocale());
	return resourceBundle.getString(PaymentMethod.class.getSimpleName() + "." + name());
    }

}
