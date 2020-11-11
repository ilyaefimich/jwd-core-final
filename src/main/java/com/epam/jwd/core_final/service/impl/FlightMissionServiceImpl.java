package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.exception.ServiceException;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.FlightMissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FlightMissionServiceImpl implements FlightMissionService {

    static final Logger logger = LoggerFactory.getLogger("JWDCourseLogger");

    private static volatile FlightMissionServiceImpl instance;

    private FlightMissionServiceImpl() {

    }

    public static FlightMissionServiceImpl getInstance() {
        if (instance == null) {
            synchronized (FlightMissionServiceImpl.class) {
                if (instance == null) {
                    instance = new FlightMissionServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public List<FlightMission> findAllMissions() {
        return new ArrayList<FlightMission>(NassaContext.getMissions());
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria) {
        FlightMissionCriteria missionCriteria = (FlightMissionCriteria) criteria;
        List<FlightMission> missions = findAllMissions();
        Optional<List<FlightMission>> optional = Optional.ofNullable(missions);
        if (!optional.isEmpty()) {
            missions = missions.stream()
                    .filter(FlightMission -> missionCriteria.getId() == null || (missionCriteria.getId() != null && FlightMission.getId().equals(missionCriteria.getId())))
                    .filter(FlightMission -> missionCriteria.getName() == null || (missionCriteria.getName() != null && FlightMission.getName().toLowerCase().equals(missionCriteria.getName().toLowerCase())))
                    .filter(FlightMission -> missionCriteria.getDistance() == null || (missionCriteria.getDistance() != null && FlightMission.getDistance() == missionCriteria.getDistance()))
                    .filter(FlightMission -> missionCriteria.getMissionStatus() == null || (missionCriteria.getMissionStatus() != null && FlightMission.getMissionResult().equals(missionCriteria.getMissionStatus())))
                    .collect(Collectors.toList());
        }
        return missions;
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        List<FlightMission> result = findAllMissionsByCriteria(criteria);
        return result.size() > 0 ? Optional.of(result.get(0)) : Optional.empty();
    }

    @Override
    public FlightMission updateSpaceshipDetails(FlightMission pflightMission) throws ServiceException {
        try {
            FlightMission flightMission = getMissionById(pflightMission.getId());
            Optional<FlightMission> optional = Optional.ofNullable(flightMission);
            if (!optional.isEmpty()) {
                flightMission.setName(pflightMission.getName());
                flightMission.setDistance(pflightMission.getDistance());
            } else {
                System.out.println("A mission with Id = " + pflightMission.getId() + " was not found. The mission has not been updated!");
            }
            return flightMission;
        } catch (Exception e) {
            throw new ServiceException("FlightMissionServiceImpl: An exeption when update the mission id = " + pflightMission.getId());
        }
    }

    @Override
    public FlightMission createMission(FlightMission flightMission) {
        return new FlightMission(flightMission);
    }

    public FlightMission createMission(String name, long distance, LocalDate startDate, LocalDate endDate) throws ServiceException {

        FlightMission mission = (new FlightMissionFactory()).create(name, startDate, endDate, distance);
        Spaceship spaceshipToAssign = getAvailableSpaceshipByDatesAndDistance(startDate, endDate, distance);
        Optional<Spaceship> optional = Optional.ofNullable(spaceshipToAssign);
        if (!optional.isEmpty()) {
            int numberOfCommanders = getNumberOfMembersByRole(spaceshipToAssign.getId(), Role.COMMANDER);
            int numberOfFlightEngineers = getNumberOfMembersByRole(spaceshipToAssign.getId(), Role.FLIGHT_ENGINEER);
            int numberOfMissionSpecialists = getNumberOfMembersByRole(spaceshipToAssign.getId(), Role.MISSION_SPECIALIST);
            int numberOfPilots = getNumberOfMembersByRole(spaceshipToAssign.getId(), Role.PILOT);

            List<CrewMember> crewMemberCommanders = getAvailableMembersByRoleAndDates(Role.COMMANDER, numberOfCommanders, startDate, endDate);
            List<CrewMember> crewMemberFlightEngineers = getAvailableMembersByRoleAndDates(Role.FLIGHT_ENGINEER, numberOfFlightEngineers, startDate, endDate);
            List<CrewMember> crewMemberMissionSpecialists = getAvailableMembersByRoleAndDates(Role.MISSION_SPECIALIST, numberOfMissionSpecialists, startDate, endDate);
            List<CrewMember> crewMemberPilots = getAvailableMembersByRoleAndDates(Role.PILOT, numberOfPilots, startDate, endDate);

            Optional<List<CrewMember>> optionalCommander = Optional.ofNullable(crewMemberCommanders);
            Optional<List<CrewMember>> optionalFlightEngineers = Optional.ofNullable(crewMemberFlightEngineers);
            Optional<List<CrewMember>> optionalMissionSpecialists = Optional.ofNullable(crewMemberMissionSpecialists);
            Optional<List<CrewMember>> optionalPilots = Optional.ofNullable(crewMemberPilots);

            if (!optionalCommander.isEmpty() && crewMemberCommanders.size() > 0
                    && !optionalFlightEngineers.isEmpty() && crewMemberFlightEngineers.size() > 0
                    && !optionalMissionSpecialists.isEmpty() && crewMemberMissionSpecialists.size() > 0
                    && !optionalPilots.isEmpty() && crewMemberPilots.size() > 0) {

                mission.setAssignedSpaceship(spaceshipToAssign); //Assign a spaceship
                logger.trace("MissionServiceImpl: Assigned a spaceship to the mission");

                List<CrewMember> assignedMembers = new ArrayList<CrewMember>();
                assignedMembers.addAll(crewMemberCommanders);
                assignedMembers.addAll(crewMemberFlightEngineers);
                assignedMembers.addAll(crewMemberMissionSpecialists);
                assignedMembers.addAll(crewMemberPilots); //Assign members to the mission
                mission.setAssignedCrewMembers(assignedMembers);
                logger.trace("MissionServiceImpl: Assigned members to the mission");

                return mission;
            } else {
                logger.error("MissionServiceImpl: There are no crew member team available for the mission!");
                throw new ServiceException();
            }

        } else {
            logger.error("MissionServiceImpl: There are no spaceships found for specified dates!");
            throw new ServiceException();
        }
    }

    private Spaceship getAvailableSpaceshipByDatesAndDistance(LocalDate startDate, LocalDate endDate, long distance) throws ServiceException {

        List<Spaceship> assignedSpaceships = new ArrayList<Spaceship>();

        List<FlightMission> missions = findAllMissions();
        Optional<List<FlightMission>> optional = Optional.ofNullable(missions);
        if (!optional.isEmpty()) {
            missions = missions.stream()
                    .filter(FlightMission ->
                            startDate.isBefore(FlightMission.getStartDate()) && endDate.isBefore(FlightMission.getEndDate())
                                    || startDate.isAfter(FlightMission.getStartDate()) && endDate.isAfter(FlightMission.getEndDate())
                                    || startDate.isAfter(FlightMission.getStartDate()) && endDate.isBefore(FlightMission.getEndDate())
                                    || startDate.isBefore(FlightMission.getStartDate()) && endDate.isAfter(FlightMission.getEndDate()))
                    .collect(Collectors.toList());

            for (FlightMission mission : missions) {
                assignedSpaceships.add(mission.getAssignedSpaceship()); //Collect already assigned spaceships
            }
        }
        SpaceshipCriteria spaceshipCriteria = new SpaceshipCriteria.Builder()
                .withFlagReadyForNextMissions(true)
                .withFlightDistance(distance)
                .build();
        List<Spaceship> spaceshipsSelected = SpaceShipServiceImpl.getInstance().findAllSpaceshipsByCriteria(spaceshipCriteria);

        spaceshipsSelected.removeAll(new HashSet<Spaceship>(assignedSpaceships));
        Optional<List<Spaceship>> optionalSpaceshipOptional = Optional.ofNullable(spaceshipsSelected);
        if (!optionalSpaceshipOptional.isEmpty() && spaceshipsSelected.size() > 0) {
            return spaceshipsSelected.get(0);
        } else {
            logger.error("MissionServiceImpl: There are no spaceships selected for specified dates!");
            throw new ServiceException();
        }
    }

    private int getNumberOfMembersByRole(long spaceshipId, Role role) throws ServiceException {
        Spaceship spaceship = SpaceShipServiceImpl.getInstance().getSpaceshipById(spaceshipId);
        Optional<Spaceship> optionalSpaceship = Optional.ofNullable(spaceship);
        if (!optionalSpaceship.isEmpty()) {
            return spaceship.getNumberOfRequiredMembersByRole(role);
        } else {
            logger.error("MissionServiceImpl: Was not able to find number of roles by spaceshipId and roleId!");
            throw new IllegalStateException();
        }
    }

    private List<CrewMember> getAvailableMembersByRoleAndDates(Role role, int numberOfMembers, LocalDate startDate, LocalDate endDate) throws ServiceException {
        List<CrewMember> crewMembers;
        try {
            CrewMemberCriteria criteria = new CrewMemberCriteria.Builder()
                    .withRole(role)
                    .withFlagReadyForNextMissions(true)
                    .build();
            crewMembers = CrewMemberServiceImpl.getInstance().findAllCrewMembersByCriteria(criteria);
        } catch (Exception e) {
            throw new ServiceException();
        }

        crewMembers.removeAll(new HashSet<CrewMember>(findMembersAlreadyAssignedToMission(startDate, endDate)));
        crewMembers = crewMembers.stream().limit(numberOfMembers).collect(Collectors.toList());

        return crewMembers;
    }

    private List<CrewMember> findMembersAlreadyAssignedToMission(LocalDate startDate, LocalDate endDate) throws ServiceException {
        List<CrewMember> members = new ArrayList<CrewMember>();
        List<FlightMission> missions = findAllMissions();
        Optional<List<FlightMission>> optional = Optional.ofNullable(missions);
        if (!optional.isEmpty()) {
            missions = missions.stream()
                    .filter(FlightMission ->
                            startDate.isBefore(FlightMission.getStartDate()) && endDate.isBefore(FlightMission.getEndDate())
                                    || startDate.isAfter(FlightMission.getStartDate()) && endDate.isAfter(FlightMission.getEndDate())
                                    || startDate.isAfter(FlightMission.getStartDate()) && endDate.isBefore(FlightMission.getEndDate())
                                    || startDate.isBefore(FlightMission.getStartDate()) && endDate.isAfter(FlightMission.getEndDate()))
                    .collect(Collectors.toList());

            for (FlightMission mission : missions) {
                members.addAll(mission.getAssignedCrewMembers());
            }
        }
        return members;
    }

    public FlightMission getMissionById(long missionId) throws ServiceException {
        Collection<FlightMission> flightMissions = NassaContext.getMissions();
        for (FlightMission flightMission : flightMissions) {
            if (flightMission.getId() == missionId) {
                return flightMission;
            }
        }
        return null;
    }

    public void updateMissionStatus(long missionId, MissionResult updatedStatus) throws ServiceException {
        try {
            FlightMission flightMission = getMissionById(missionId);
            Optional<FlightMission> optional = Optional.ofNullable(flightMission);
            if (!optional.isEmpty()) {
                if (isValidStatusModification(flightMission.getMissionResult(), updatedStatus)) {
                    flightMission.setMissionResult(updatedStatus);
                } else {
                    System.out.println("A mission update has not been done. A modification of the status from " + flightMission.getMissionResult() + " to " + updatedStatus + " is not allowed. ");
                }
            } else {
                System.out.println("A mission with Id = " + missionId + " was not found. The mission has not been updated!");
            }
        } catch (Exception e) {
            throw new ServiceException();
        }
    }

    private boolean isValidStatusModification(MissionResult statusFrom, MissionResult statusTo) {
        if (statusFrom.equals(MissionResult.PLANNED) && (statusTo.equals(MissionResult.CANCELLED) || statusTo.equals(MissionResult.IN_PROGRESS))) {
            return true;
        } else if (statusFrom.equals(MissionResult.IN_PROGRESS) && (statusTo.equals(MissionResult.FAILED) || statusTo.equals(MissionResult.COMPLETED))) {
            return true;
        } else {
            return false;
        }

    }
}
