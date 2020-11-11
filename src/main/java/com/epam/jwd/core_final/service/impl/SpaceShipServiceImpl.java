package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.ServiceException;
import com.epam.jwd.core_final.factory.impl.SpaceShipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpaceShipServiceImpl implements SpaceshipService {

    private static volatile SpaceShipServiceImpl instance;

    private SpaceShipServiceImpl() {

    }

    public static SpaceShipServiceImpl getInstance() {
        if (instance == null) {
            synchronized (SpaceShipServiceImpl.class) {
                if (instance == null) {
                    instance = new SpaceShipServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Spaceship> findAllSpaceships() {
        return new ArrayList(NassaContext.getSpaceships());
    }

    public int countFreeSpaceShips() {
        SpaceshipCriteria criteria = new SpaceshipCriteria.Builder()
                .withFlagReadyForNextMissions(true).build();

        return findAllSpaceshipsByCriteria(criteria).size();
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        SpaceshipCriteria spaceshipCriteria = (SpaceshipCriteria) criteria;

        List<Spaceship> result = new ArrayList(NassaContext.getSpaceships());
        result = result
                .stream()
                .filter(Spaceship -> spaceshipCriteria.getName() == null || (spaceshipCriteria.getName() != null && Spaceship.getName().toLowerCase().equals(spaceshipCriteria.getName().toLowerCase())))
                .filter(Spaceship -> spaceshipCriteria.getDistance() == null || (spaceshipCriteria.getDistance() != null && Spaceship.getFlightDistance() >= spaceshipCriteria.getDistance()))
                .filter(Spaceship -> spaceshipCriteria.getReadyForNextMissions() == null || (spaceshipCriteria.getReadyForNextMissions() != null && Spaceship.isReadyForNextMissions() == spaceshipCriteria.getReadyForNextMissions()))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        List<Spaceship> result = findAllSpaceshipsByCriteria(criteria);
        return result.size() > 0 ? Optional.of(result.get(0)) : Optional.empty();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship pspaceship) throws ServiceException {
        try {
            Spaceship spaceship = getSpaceshipById(pspaceship.getId());
            Optional<Spaceship> optional = Optional.ofNullable(spaceship);
            if (!optional.isEmpty()) {
                spaceship.setName(pspaceship.getName());
                spaceship.setFlightDistance(pspaceship.getFlightDistance());
            } else {
                System.out.println("A spaceship with Id = " + pspaceship.getId() + " was not found. The spaceshipp has not been updated!");
            }
            return spaceship;
        } catch (Exception e) {
            throw new ServiceException("SpaceshipServiceImpl: An exception when update the spaceship id = " + pspaceship.getId());
        }
    }

    @Override
    public void assignSpaceshipOnMission(FlightMission flightMission, Spaceship spaceship) throws ServiceException, RuntimeException {
        if (flightMission.getAssignedSpaceship() != null) {
            throw new ServiceException("CrewMemberServiceImpl: Unable to assign Spaceship to the Mission. The specified mission has already had an assigned spaceship.");
        }

        if (flightMission.getDistance() > spaceship.getFlightDistance()) {
            throw new ServiceException("CrewMemberServiceImpl: Unable to assign a specified spaceship to the mission, because of the unsupported distance.");
        }

        flightMission.setAssignedSpaceship(spaceship);
    }

    public Spaceship getSpaceshipById(long spaceshipId) {
        Collection<Spaceship> spaceships = NassaContext.getSpaceships();
        for(Spaceship spaceship : spaceships) {
            if (spaceship.getId() == spaceshipId) {
                return spaceship;
            }
        }
        return null;
    }

    @Override
    public Spaceship createSpaceship(Spaceship spaceship) throws RuntimeException, ServiceException {
        return createSpaceship(spaceship.getName(), spaceship.getFlightDistance(), spaceship.isReadyForNextMissions());
    }

    private Spaceship createSpaceship(String name, long distance, boolean isReadyForNextMissions) throws ServiceException {
        if (spaceshipWithNameAlreadyExists(name)) {
            throw new ServiceException("CrewMemberServiceImpl: Cannot create CrewMember. A member with such a name already exists.");
        }
        return (new SpaceShipFactory()).create(name, distance, isReadyForNextMissions);
    }

    private boolean spaceshipWithNameAlreadyExists(String name) {
        SpaceshipCriteria criteria = new SpaceshipCriteria.Builder()
                .withName(name)
                .build();
        Optional<Spaceship> optional = findSpaceshipByCriteria(criteria);
        return optional.isEmpty() ? false : true;
    }

}
