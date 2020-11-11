package com.epam.jwd.core_final.service;

import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.ServiceException;

import java.util.List;
import java.util.Optional;

/**
 * All its implementations should be a singleton
 * You have to use streamAPI for filtering, mapping, collecting, iterating
 */
public interface SpaceshipService {

    List<Spaceship> findAllSpaceships();

    List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria);

    Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria);

    Spaceship updateSpaceshipDetails(Spaceship spaceship) throws ServiceException;

    // todo create custom exception for case, when spaceship is not able to be assigned
    public void assignSpaceshipOnMission(FlightMission flightMission, Spaceship spaceship) throws ServiceException, RuntimeException;

    // todo create custom exception for case, when crewMember is not able to be created (for example - duplicate.
    // spaceship unique criteria - only name!
    Spaceship createSpaceship(Spaceship spaceship) throws RuntimeException, ServiceException;
}
