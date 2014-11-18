package pt.ist.expenditureTrackingSystem.domain.util;

import module.dashBoard.domain.DashBoardPanel;
import module.dashBoard.servlet.WidgetRegistry.WidgetAditionPredicate;

import org.fenixedu.bennu.core.domain.User;

public class ExpenditureTrackingPanelPredicate implements WidgetAditionPredicate {

    public static final ExpenditureTrackingPanelPredicate instant = new ExpenditureTrackingPanelPredicate();

    @Override
    public boolean canBeAdded(DashBoardPanel panel, User userAdding) {
        return (DashBoardPanel.class.isAssignableFrom(panel.getClass()));
    }

}
