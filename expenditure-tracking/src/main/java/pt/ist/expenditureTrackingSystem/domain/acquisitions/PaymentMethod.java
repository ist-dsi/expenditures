package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum PaymentMethod implements IPresentableEnum {

    MB_NET, WIRE_TRANSFER, CHECK, CASH;

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle("resources.ExpenditureEnumerationResources", I18N.getLocale());
        return resourceBundle.getString(PaymentMethod.class.getSimpleName() + "." + name());
    }

}
