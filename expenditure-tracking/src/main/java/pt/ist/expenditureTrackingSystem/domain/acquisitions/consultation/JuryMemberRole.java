package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;

public enum JuryMemberRole implements IPresentableEnum {

    PRESIDENT(true), VOWEL(true), SUBSTITUTE(false);

    private final boolean isEffective; 

    JuryMemberRole(final boolean isEffective) {
        this.isEffective = isEffective;
    }

    public boolean isEffective() {
        return isEffective;
    }

    @Override
    public String getLocalizedName() {
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.ExpenditureResources", I18N.getLocale());
        return resourceBundle.getString(JuryMemberRole.class.getSimpleName() + "." + name());        
    }

}
