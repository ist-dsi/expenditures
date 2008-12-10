package pt.ist.expenditureTrackingSystem.applicationTier;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

    private static final Map<RoleType, Set<String>> roleUsernamesMap = new HashMap<RoleType, Set<String>>();

    private static Set<String> getUsernames(final RoleType roleType) {
	Set<String> usernames = roleUsernamesMap.get(roleType);
	if (usernames == null) {
	    usernames = new HashSet<String>();
	    roleUsernamesMap.put(roleType, usernames);		    
	}
	return usernames;
    }

    public static synchronized void initRole(final RoleType roleType, final String usernameStrings) {
	final Set<String> usernames = getUsernames(roleType);
	for (final String username : usernameStrings.split(",")) {
	    usernames.add(username.trim());
	}
    }

    public static class User implements pt.ist.fenixWebFramework.security.User, Serializable {

	private final DomainReference<Person> personReference;
	private DomainReference<Person> mockReference;
	
	private transient String privateConstantForDigestCalculation;

	private User(final String username) {
	    final Person person = findByUsername(username);
	    personReference = new DomainReference<Person>(person);
	    mockReference = null;
	}

	public void mockUser(final String username) {
	    final Person person = Person.findByUsername(username);
	    mockReference = new DomainReference<Person>(person);
	}
	
	public void unmockUser() {
	    mockReference = null;
	}
	
	private Person findByUsername(final String username) {
	    final Person person = Person.findByUsername(username);
	    return person == null ? new Person(username) : person;
	}

	public String getUsername() {
	    return mockReference == null ? getPerson().getUsername() : mockReference.getObject().getUsername();
	}

	public boolean hasRole(final String roleAsString) {
	    final RoleType roleType = RoleType.valueOf(roleAsString);
	    final Person person = mockReference == null ? getPerson() : mockReference.getObject();
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
	    return mockReference == null ? personReference.getObject() : mockReference.getObject();
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

	final Person person = user.getPerson();
	for (final Entry<RoleType, Set<String>> entry : roleUsernamesMap.entrySet()) {
	    if (entry.getValue().contains(username)) {
		addRoleType(person, entry.getKey());
	    }
	}

	return user;
    }

    protected static void addRoleType(final Person person, final RoleType roleType) {
	if (!person.hasRoleType(roleType)) {
	    person.addRoles(Role.getRole(roleType));
	}
    }

}
