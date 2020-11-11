package com.epam.jwd.core_final.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Expected fields:
 * <p>
 * missions name {@link String}
 * start date {@link java.time.LocalDate}
 * end date {@link java.time.LocalDate}
 * distance {@link Long} - missions distance
 * assignedSpaceShift {@link Spaceship} - not defined by default
 * assignedCrew {@link java.util.List<CrewMember>} - list of missions members based on ship capacity - not defined by default
 * missionResult {@link MissionResult}
 */
public class FlightMission extends AbstractBaseEntity {
    @JsonProperty("startDate")
    private LocalDate startDate;
    @JsonProperty("endDate")
    private LocalDate endDate;
    @JsonProperty("distance")
    private long distance;
    @JsonProperty("assignedSpaceship")
    private Spaceship assignedSpaceship;
    @JsonProperty("assignedCrewMembers")
    private List<CrewMember> assignedCrewMembers;
    @JsonProperty("missionResult")
    private MissionResult missionResult;

    public FlightMission(String name, LocalDate startDate, LocalDate endDate, long distance) {
        super.id = Math.abs(Objects.hash(name, startDate, endDate, distance));
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.distance = distance;
    }

    public FlightMission(FlightMission flightMission) {
        this.startDate = flightMission.getStartDate();
        this.endDate = flightMission.getEndDate();
        this.name = flightMission.getName();
        this.distance = flightMission.getDistance();
        super.id = Math.abs(Objects.hash(name, startDate, endDate, distance)-17);
    }

    public void setName(String name) {
        super.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public Spaceship getAssignedSpaceship() {
        return assignedSpaceship;
    }

    public void setAssignedSpaceship(Spaceship assignedSpaceship) {
        this.assignedSpaceship = assignedSpaceship;
    }

    public List<CrewMember> getAssignedCrewMembers() {
        return assignedCrewMembers;
    }

    public void setAssignedCrewMembers(List<CrewMember> assignedCrewMembers) {
        this.assignedCrewMembers = assignedCrewMembers;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    public void setMissionResult(MissionResult missionResult) {
        this.missionResult = missionResult;
    }
}
