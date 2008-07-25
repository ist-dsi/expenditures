package pt.ist.expenditureTrackingSystem.applicationTier;

import java.io.Serializable;

import pt.ist.fenixWebFramework.security.UserView;

public class Authenticate {

    public static class User implements pt.ist.fenixWebFramework.security.User, Serializable {

	private final String username;

	private User(final String username) {
	    this.username = username;
	}

	@Override
	public String getUsername() {
	    return username;
	}

	@Override
	public boolean hasRole(final String arg0) {
	    // TODO Auto-generated method stub
	    return false;
	}

	@Override
	public boolean equals(Object obj) {
	    return obj instanceof User && username.equals(((User) obj).username);
	}

	@Override
	public int hashCode() {
	    return username.hashCode();
	}

    }

    public static User authenticate(final String username, final String password) {
	final User user = new User(username);
	UserView.setUser(user);
	return user;
    }

}
