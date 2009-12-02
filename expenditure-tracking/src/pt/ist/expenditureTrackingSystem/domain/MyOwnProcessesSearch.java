package pt.ist.expenditureTrackingSystem.domain;

import java.util.ResourceBundle;

import myorg.domain.exceptions.DomainException;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class MyOwnProcessesSearch extends MyOwnProcessesSearch_Base {

    public MyOwnProcessesSearch() {
	super();
	setPendingOperations(Boolean.TRUE);
	setShowOnlyAcquisitionsExcludedFromSupplierLimit(Boolean.FALSE);
	setShowOnlyAcquisitionsWithAdditionalCosts(Boolean.FALSE);
	setExpenditureTrackingSystemForSystemSearch(ExpenditureTrackingSystem.getInstance());
    }

    @Override
    public Boolean getShowOnlyResponsabilities() {
	return false;
    }

    @Override
    public void setPerson(Person person) {
	throw new DomainException("message.exceptiong.notAllowedToSetPerson");
    }

    @Override
    public Person getRequestor() {
	return Person.getLoggedPerson();
    }

    @Override
    public void setSearchName(String searchName) {
	throw new DomainException("message.exceptiong.notAllowedToSetName");
    }

    @Override
    public String getSearchName() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources/ExpenditureResources", Language.getLocale());
	return bundle.getString("label.myOwnProcessesSearchName");
    }
}
