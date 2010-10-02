package module.workingCapital.domain;

import java.util.ResourceBundle;

import module.workflow.util.PresentableProcessState;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum WorkingCapitalProcessState implements PresentableProcessState {

    PENDING_ACCEPT_RESPONSIBILITY,
    PENDING_APPROVAL,
    PENDING_VERIFICATION,
    PENDING_FUND_ALLOCATION,
    PENDING_AUTHORIZATION,
    PENDING_PAYMENT,
    WORKING_CAPITAL_AVAILABLE,
    SENT_FOR_TERMINATION,
    SENT_FOR_FUND_REFUND,
    TERMINATED,
    CANCELED(false) {

	@Override
	public boolean showFor(PresentableProcessState state) {
	    return false;
	}

    }
    ;

    private final boolean isVisibleState;

    private WorkingCapitalProcessState(final boolean isVisibleState) {
	this.isVisibleState = isVisibleState;
    }

    private WorkingCapitalProcessState() {
	this(true);
    }

    private String getResource(final String suffix) {
    	final ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.WorkingCapitalResources", Language.getLocale());
    	return resourceBundle.getString(WorkingCapitalProcessState.class.getSimpleName() + "." + name() + suffix);	
    }

    @Override
    public String getDescription() {
	return getResource(".description");
    }

    @Override
    public String getLocalizedName() {
	return getResource("");
    }

    @Override
    public boolean showFor(final PresentableProcessState state) {
	return true;
    }

    public boolean isVisibleState() {
        return isVisibleState;
    }

}
