package security;

import be.objectify.deadbolt.core.models.Role;

/**
 * User: jbonini
 * Date: 16/07/13
 */
public class SecurityRole implements Role {

    private String name;

    public SecurityRole(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
