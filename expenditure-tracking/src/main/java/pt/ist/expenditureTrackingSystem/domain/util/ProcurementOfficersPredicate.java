package pt.ist.expenditureTrackingSystem.domain.util;

import module.dashBoard.domain.DashBoardPanel;
import module.dashBoard.servlet.WidgetRegistry.WidgetAditionPredicate;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.expenditureTrackingSystem.domain.ExpenditureTrackingSystem;

public class ProcurementOfficersPredicate implements WidgetAditionPredicate {

    @Override
    public boolean canBeAdded(DashBoardPanel panel, User userAdding) {
        return ExpenditureTrackingPanelPredicate.instant.canBeAdded(panel, userAdding)
                && (ExpenditureTrackingSystem.isAcquisitionCentralGroupMember(userAdding)
                        || !userAdding.getExpenditurePerson().getAccountingUnits().isEmpty() || !userAdding
                        .getExpenditurePerson().getProjectAccountingUnits().isEmpty());
    }

}
