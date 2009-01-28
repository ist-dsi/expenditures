package pt.ist.expenditureTrackingSystem.domain;

import java.util.ResourceBundle;

import pt.ist.expenditureTrackingSystem.applicationTier.Authenticate.User;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PendingProcessesSearch extends PendingProcessesSearch_Base {
    
    public PendingProcessesSearch() {
	super();
	setPendingOperations(Boolean.TRUE);
	setShowOnlyAcquisitionsExcludedFromSupplierLimit(Boolean.FALSE);
	setShowOnlyAcquisitionsWithAdditionalCosts(Boolean.FALSE);
	setExpenditureTrackingSystemForSystemSearch(ExpenditureTrackingSystem.getInstance());
    }

    @Override
    public Boolean getShowOnlyResponsabilities() {
	User user = UserView.getUser();
	Person person = user.getPerson();
	return !person.getAuthorizations().isEmpty();
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("message.exceptiong.notAllowedToSetPerson");
    }

    @Override
    public void setSearchName(String searchName) {
	throw new DomainException("message.exceptiong.notAllowedToSetName");
    }

    @Override
    public String getSearchName() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources/ExpenditureResources", Language.getLocale());
	return bundle.getString("label.pendingProcessesSearchName");
    }
}
