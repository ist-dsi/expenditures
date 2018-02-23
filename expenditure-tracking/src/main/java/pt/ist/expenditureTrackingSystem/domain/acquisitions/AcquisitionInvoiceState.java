package pt.ist.expenditureTrackingSystem.domain.acquisitions;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum AcquisitionInvoiceState implements IPresentableEnum {

    RECEIVED, AWAITING_CONFIRMATION, CONFIRMED, PROCESSED, PAYED, CANCELED(false);

    private boolean isActive = true;

    private AcquisitionInvoiceState() {        
    }

    private AcquisitionInvoiceState(final boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureResources", I18N.getLocale());
        return resourceBundle.getString(this.getClass().getSimpleName() + "." + name());
    }

}
