package pt.ist.expenditureTrackingSystem.domain.acquisitions.consultation;

public enum JuryMemberRole {

    PRESIDENT(true), VOWEL(true), SUBSTITUTE(false);

    private final boolean isEffective; 

    JuryMemberRole(final boolean isEffective) {
        this.isEffective = isEffective;
    }

    public boolean isEffective() {
        return isEffective;
    }

}
