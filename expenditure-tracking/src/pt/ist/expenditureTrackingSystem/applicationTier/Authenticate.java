package pt.ist.expenditureTrackingSystem.applicationTier;

import java.io.Serializable;

import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.util.DomainReference;

public class Authenticate {

    public static class User implements pt.ist.fenixWebFramework.security.User, Serializable {

	private final DomainReference<Person> personReference;
	
	private transient String privateConstantForDigestCalculation;
	
	private User(final String username) {
	    final Person person = findByUsername(username);
	    personReference = new DomainReference<Person>(person);
	}

	private Person findByUsername(final String username) {
	    final Person person = Person.findByUsername(username);
	    return person == null ? new Person(username) : person;
	}

	public String getUsername() {
	    return getPerson().getUsername();
	}

	public boolean hasRole(final String arg0) {
	    // TODO Auto-generated method stub
	    return false;
	}

	@Override
	public boolean equals(Object obj) {
	    return obj instanceof User && getUsername().equals(((User) obj).getUsername());
	}

	@Override
	public int hashCode() {
	    return getUsername().hashCode();
	}

	public Person getPerson() {
	    return personReference == null ? null : personReference.getObject();
	}

	// TODO do an accurate and secure method here
	public String getPrivateConstantForDigestCalculation() {
	    if (privateConstantForDigestCalculation == null) {
		final Person person = getPerson();
		privateConstantForDigestCalculation = person.getUsername() + person.getPassword();
	    }
	    return privateConstantForDigestCalculation;
	}
    }

    @Service
    public static User authenticate(final String username, final String password) {
	final User user = new User(username);
	UserView.setUser(user);
	return user;
    }

}
