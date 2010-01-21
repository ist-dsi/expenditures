package pt.ist.expenditureTrackingSystem.domain;

import myorg.domain.User;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ExpenditureUserDashBoardPanel extends ExpenditureUserDashBoardPanel_Base {

    public ExpenditureUserDashBoardPanel() {
	super();
    }

    public ExpenditureUserDashBoardPanel(MultiLanguageString name, User user) {
	super();
	setName(name);
	setUser(user);
    }

}
