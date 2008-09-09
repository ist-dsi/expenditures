package pt.ist.expenditureTrackingSystem.applicationTier;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import pt.ist.expenditureTrackingSystem.domain.Role;
import pt.ist.expenditureTrackingSystem.domain.RoleType;
import pt.ist.expenditureTrackingSystem.domain.organization.Person;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.util.DomainReference;

public class Authenticate implements Serializable {

    private static final String randomValue;

    static {
	SecureRandom random = null;

	try {
	    random = SecureRandom.getInstance("SHA1PRNG");
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}

	random.setSeed(System.currentTimeMillis());
	randomValue = String.valueOf(random.nextLong());
    }

    private static final Set<String> managerUsernames = new HashSet<String>();

    public static void init(final String managerUsernameStrings) {
	for (final String username : managerUsernameStrings.split(",")) {
	    managerUsernames.add(username.trim());
	}
    }

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

	public boolean hasRole(final String roleAsString) {
	    final RoleType roleType = RoleType.valueOf(roleAsString);
	    final Person person = getPerson();
	    return person != null && person.hasRoleType(roleType);
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
		privateConstantForDigestCalculation = person.getUsername() + person.getPassword() + randomValue;
	    }
	    return privateConstantForDigestCalculation;
	}
    }

    @Service
    public static User authenticate(final String username, final String password) {
	final User user = new User(username);
	UserView.setUser(user);

	if (managerUsernames.contains(username)) {
	    final Person person = user.getPerson();
	    if (!person.hasRoleType(RoleType.MANAGER)) {
		person.addRoles(Role.getRole(RoleType.MANAGER));
	    }
	}

	return user;
    }

}
