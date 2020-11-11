package com.epam.jwd.core_final.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * role {@link Role} - member role
 * rank {@link Rank} - member rank
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class CrewMember extends AbstractBaseEntity {
    @JsonProperty("role")
    private Role role;
    @JsonProperty("rank")
    private Rank rank;
    @JsonProperty("isReadyForNextMissions")
    private boolean isReadyForNextMissions;

    public CrewMember(CrewMember crewMember) {
        this.name = crewMember.getName();
        this.rank = crewMember.getRank();
        this.role = crewMember.getRole();
        this.isReadyForNextMissions = crewMember.isReadyForNextMissions();
        this.id = Math.abs(Objects.hash(name))-17;
    }

    public CrewMember(String name) {
        super.id = Math.abs(Objects.hash(name));
        super.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public boolean isReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public void setReadyForNextMissions(boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
    }
}
