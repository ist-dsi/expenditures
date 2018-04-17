package pt.ist.expenditureTrackingSystem.domain.acquisitions.refund;

import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class RefundItemNature extends RefundItemNature_Base implements Comparable<RefundItemNature> {
    
    public RefundItemNature(final LocalizedString type, final Boolean shouldAllocateFundsToSupplier) {
        setExpenditureTrackingSystem(ExpenditureTrackingSystem.getInstance());
        setType(type);
        setShouldAllocateFundsToSupplier(shouldAllocateFundsToSupplier);
    }

    @Override
    public int compareTo(final RefundItemNature n) {
        if (n == null) {
            return 1;
        }
        final int c = getType().compareTo(n.getType());
        return c == 0 ? getExternalId().compareTo(n.getExternalId()) : c;
    }
    
}
