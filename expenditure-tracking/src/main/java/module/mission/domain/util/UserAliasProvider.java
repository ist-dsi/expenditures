package module.mission.domain.util;

import module.organization.domain.Person;

public interface UserAliasProvider {

    public String getUserAliases(final Person person);

}
