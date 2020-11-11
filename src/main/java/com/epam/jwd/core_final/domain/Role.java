package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.exception.UnknownEntityException;

public enum Role implements BaseEntity {
    MISSION_SPECIALIST(1L),
    FLIGHT_ENGINEER(2L),
    PILOT(3L),
    COMMANDER(4L);

    private final Long id;

    Role(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return resolveRoleById(id).getName();
    }

    /**
     * @throws UnknownEntityException if such id does not exist
     */
    public static Role resolveRoleById(long id) {
        for (Role role : Role.values()) {
            if (role.id.equals(id)) {
                return role;
            }
        }
        throw new UnknownEntityException("Such role Id does not exist: " + id);
    }
}
