package com.epam.jwd.core_final.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * crew {@link java.util.Map<Role, Short>}
 * flightDistance {@link Long} - total available flight distance
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class Spaceship extends AbstractBaseEntity {
    @JsonProperty("crewMembers")
    private Map<Role, Short> crewMembers = new HashMap<Role, Short>();
    @JsonProperty("flightDistance")
    private Long flightDistance;
    @JsonProperty("isReadyForNextMissions")
    private Boolean isReadyForNextMissions;

    public Spaceship(String name) {
        super.id = Math.abs(Objects.hash(name, flightDistance));
        super.name = name;
        isReadyForNextMissions = true;
    }

    public Spaceship(Spaceship spaceship) {
        super.name = spaceship.getName();
        isReadyForNextMissions = spaceship.isReadyForNextMissions();
        flightDistance = spaceship.getFlightDistance();
        super.id = Math.abs(Objects.hash(name, flightDistance)-17);
    }

    public short getNumberOfRequiredMembersByRole (Role role) {
        return crewMembers.get(role);
    }

    public void addCrewRole (long roleId, short numberOfMembers) {
        Role role = Role.resolveRoleById(roleId);
        crewMembers.put(role, numberOfMembers);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFlightDistance() {
        return flightDistance;
    }

    public void setFlightDistance(Long flightDistance) {
        this.flightDistance = flightDistance;
    }

    public Boolean isReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public void setReadyForNextMissions(boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
    }
}
